package org.example.sessionservice.service.impl;

import org.example.sessionservice.domain.*;
import org.example.sessionservice.dto.*;
import org.example.sessionservice.exception.BadRequestException;
import org.example.sessionservice.exception.NotFoundException;
import org.example.sessionservice.mapper.SessionMapper;
import org.example.sessionservice.messaging.MessageHelper;
import org.example.sessionservice.repository.GameRepository;
import org.example.sessionservice.repository.SessionInviteRepository;
import org.example.sessionservice.repository.SessionParticipantRepository;
import org.example.sessionservice.repository.SessionRepository;
import org.example.sessionservice.security.CheckSecurity;
import org.example.sessionservice.service.SessionService;
import org.example.sessionservice.service.UserClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final GameRepository gameRepository;
    private final SessionMapper sessionMapper;
    private final UserClientService userClientService;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;
    private final SessionInviteRepository inviteRepository;

    @Value("${session.notification.queue}")
    private String notificationQueueName;

    public SessionServiceImpl(SessionRepository sessionRepository, SessionParticipantRepository participantRepository, GameRepository gameRepository, SessionMapper sessionMapper, UserClientService userClientService, JmsTemplate jmsTemplate, MessageHelper messageHelper, SessionInviteRepository sessionInviteRepository) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.gameRepository = gameRepository;
        this.sessionMapper = sessionMapper;
        this.userClientService = userClientService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.inviteRepository = sessionInviteRepository;
    }

    @Override
    public SessionResponseDto createSession(CreateSessionDto dto, Long organizerId) {
        // provera da li igra postoji
        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(() -> new NotFoundException("Igra ne postoji"));

        // SINHRONA PROVERA USER-A
        UserResponseDto user = userClientService.getUserById(organizerId);

        // ne sme da je blokiran
        if (user.isBlocked()) {
            throw new BadRequestException("Korisnik je blokiran i ne može da kreira sesiju");
        }
        // % prisustva mora da bude veci od 90
        // salje se mejl da je odbijeno kreiranje
        if (user.getAttendancePercentage() < 90) {

            NotificationMessageDto notification =
                    new NotificationMessageDto(
                            user.getEmail(),
                            NotificationMessageDto.NotificationType.SESSION_CREATION_REJECTED,
                            Map.of(
                                    "attendance", user.getAttendancePercentage()
                            )
                    );

            jmsTemplate.convertAndSend(
                    notificationQueueName,
                    messageHelper.createTextMessage(notification)
            );

            throw new BadRequestException("Procenat prisustva mora biti najmanje 90%");
        }

        Session session = new Session(game, dto.getName(), dto.getDescription(), dto.getMaxPlayers(), SessionType.valueOf(dto.getSessionType()), SessionStatus.SCHEDULED, dto.getStartTime(), organizerId);

        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Override
    public List<SessionResponseDto> getAllSessions() {
        return sessionRepository.findAll().stream().map(this::mapToSessionResponseWithUsers).collect(Collectors.toList());
    }

    @Override
    public SessionResponseDto getSessionById(Long sessionId) {
        return mapToSessionResponseWithUsers(getSession(sessionId));
    }

    @Override
    public SessionResponseDto getSessionByName(String name) {
        Session session = sessionRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Sesija ne postoji"));
        return mapToSessionResponseWithUsers(session);
    }

    // ne treba nam dodatno komplikovanje
    /*
    @Override
    @CheckSecurity(roles = {"ADMIN"})
    public SessionResponseDto updateSession(Long sessionId, CreateSessionDto dto) {
        Session session = getSession(sessionId);

        if (dto.getName() != null) session.setName(dto.getName());
        if (dto.getDescription() != null) session.setDescription(dto.getDescription());
        if (dto.getMaxPlayers() > 0) session.setMaxPlayers(dto.getMaxPlayers());
        if (dto.getStartTime() != null) session.setStartTime(dto.getStartTime());

        return mapToSessionResponseWithUsers(sessionRepository.save(session));
    }


    @Override
    @CheckSecurity(roles = {"ADMIN"})
    public void deleteSession(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new NotFoundException("Sesija ne postoji");
        }
        sessionRepository.deleteById(sessionId);
    }
    */

    private boolean isAdmin(Long userId) {
        UserResponseDto user = userClientService.getUserById(userId);
        return user.getRole() == UserResponseDto.Role.ADMIN;
    }


    @Override
    public void cancelSession(Long sessionId, Long currentUserId) {

        Session session = getSession(sessionId);

        boolean isAdmin = isAdmin(currentUserId);

        if (!isAdmin && !session.getOrganizerId().equals(currentUserId)) {
            throw new BadRequestException("Nemate pravo da otkažete ovu sesiju");
        }

        session.setStatus(SessionStatus.CANCELED);
        sessionRepository.save(session);

        // asinhrono obavestavamo sve prijavljene
        List<SessionParticipant> participants =
                participantRepository.findBySessionId(session.getId());

        for (SessionParticipant participant : participants) {
            UserResponseDto user =
                    userClientService.getUserById(participant.getUserId());

            NotificationMessageDto notificationMessageDto =
                    new NotificationMessageDto(
                            user.getEmail(),
                            NotificationMessageDto.NotificationType.SESSION_CANCELED,
                            Map.of(
                                    "sessionName", session.getName(),
                                    "startTime", session.getStartTime().toString()
                            )
                    );

            jmsTemplate.convertAndSend(
                    notificationQueueName,
                    messageHelper.createTextMessage(notificationMessageDto)
            );
        }

    }

    @Override
    public SessionResponseDto joinSession(Long sessionId, Long userId) {
        Session session = getSession(sessionId);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Sesija nije otvorena");
        }

        // provera da li je korisnik blokiran
        UserResponseDto user = userClientService.getUserById(userId);
        if (user.isBlocked()) {
            throw new BadRequestException("Korisnik je blokiran i ne može da se prijavi na sesiju");
        }

        // provera da li je sesija zatvorena -> ne moze bez pozivnice
        if (session.getType() == SessionType.PRIVATE) {
            throw new BadRequestException("Pristup zatvorenoj sesiji je moguć samo preko pozivnice");
        }

        if (participantRepository.findBySessionIdAndUserId(sessionId, userId).isPresent()) {
            throw new BadRequestException("Korisnik je već prijavljen");
        }

        if (participantRepository.countBySessionId(sessionId) >= session.getMaxPlayers()) {
            throw new BadRequestException("Sesija je puna");
        }

        participantRepository.save(new SessionParticipant(session, userId, false, false));

        // poziv ka user-service za incrementSessionsApplied
        userClientService.incrementSessionsApplied(userId);

        // salje se mejl potvrde o uspesnoj prijavi

        NotificationMessageDto notification =
                new NotificationMessageDto(
                        user.getEmail(),
                        NotificationMessageDto.NotificationType.SESSION_JOIN_CONFIRMATION,
                        Map.of(
                                "sessionName", session.getName(),
                                "startTime", session.getStartTime().toString()
                        )
                );

        jmsTemplate.convertAndSend(
                notificationQueueName,
                messageHelper.createTextMessage(notification)
        );

        return mapToSessionResponseWithUsers(session);
    }


    // slanje pozivnice
    @Override
    public void inviteUser(Long sessionId, Long organizerId, Long invitedUserId) {

        Session session = getSession(sessionId);

        // samo organizator
        if (!session.getOrganizerId().equals(organizerId)) {
            throw new BadRequestException("Samo organizator može slati pozivnice");
        }

        // samo za PRIVATE sesije
        if (session.getType() != SessionType.PRIVATE) {
            throw new BadRequestException("Pozivnice su moguće samo za privatne sesije");
        }

        // generišemo token
        String token = UUID.randomUUID().toString();

        SessionInvite invite = new SessionInvite(
                session.getId(),
                invitedUserId,
                token
        );

        inviteRepository.save(invite);

        // dohvat emaila
        UserResponseDto user = userClientService.getUserById(invitedUserId);

        NotificationMessageDto notification = new NotificationMessageDto(
                user.getEmail(),
                NotificationMessageDto.NotificationType.SESSION_INVITATION,
                Map.of(
                        "sessionName", session.getName(),
                        "inviteLink", "http://localhost:8084/api/sessions/invite/accept/" + token
                )
        );

        jmsTemplate.convertAndSend(
                notificationQueueName,
                messageHelper.createTextMessage(notification)
        );
    }


    //prihvatanje pozivnice -> join private session
    @Override
    public SessionResponseDto acceptInvite(String token) {

        SessionInvite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Pozivnica ne postoji"));

        if (invite.isUsed()) {
            throw new BadRequestException("Pozivnica je već iskorišćena");
        }

        Long userId = invite.getInvitedUserId();
        Session session = getSession(invite.getSessionId());

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Sesija nije zakazana");
        }

        if (participantRepository.countBySessionId(session.getId()) >= session.getMaxPlayers()) {
            throw new BadRequestException("Sesija je puna");
        }

        // join
        participantRepository.save(
                new SessionParticipant(session, userId, false, false)
        );

        userClientService.incrementSessionsApplied(userId);

        invite.setUsed(true);
        inviteRepository.save(invite);

        // isto dobija mejl potvrde o uspesnom prijavljivanju
        UserResponseDto user = userClientService.getUserById(userId);

        NotificationMessageDto notification =
                new NotificationMessageDto(
                        user.getEmail(),
                        NotificationMessageDto.NotificationType.SESSION_JOIN_CONFIRMATION,
                        Map.of(
                                "sessionName", session.getName(),
                                "startTime", session.getStartTime().toString()
                        )
                );

        jmsTemplate.convertAndSend(
                notificationQueueName,
                messageHelper.createTextMessage(notification)
        );


        return mapToSessionResponseWithUsers(session);
    }



    @Override
    public SessionResponseDto leaveSession(Long sessionId, Long userId) {

        Session session = getSession(sessionId);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Sesija nije zakazana");
        }

        if (session.getOrganizerId().equals(userId)) {
            throw new BadRequestException("Organizator ne može napustiti sopstvenu sesiju");
        }

        SessionParticipant participant = participantRepository.findBySessionIdAndUserId(sessionId, userId).orElseThrow(() -> new NotFoundException("Korisnik nije u sesiji"));

        // označavamo da je napustio pre završetka
        participant.setLeftEarly(true);
        participant.setAttended(false);
        participantRepository.save(participant);

        // sinhroni update user-servisa
        userClientService.decrementSessionsApplied(userId);

        return mapToSessionResponseWithUsers(getSession(sessionId));
    }

    @Override
    public void closeSession(CloseSessionDto dto, Long currentUserId) {
        Session session = getSession(dto.getSessionId());

        if (!session.getOrganizerId().equals(currentUserId)) {
            throw new BadRequestException("Samo organizator može zatvoriti sesiju");
        }

        // bar 1 ucesnik
        boolean hasParticipantAttended = dto.getParticipantsAttendance().entrySet().stream().anyMatch(entry -> !entry.getKey().equals(session.getOrganizerId()) && entry.getValue());

        if (!hasParticipantAttended) {
            throw new BadRequestException("Sesija se ne može zatvoriti bez barem jednog prisutnog učesnika");
        }


        // prolazimo kroz sve ucesnike da bi im updatovali polja
        dto.getParticipantsAttendance().forEach((userId, attended) -> {
            SessionParticipant participant = participantRepository.findBySessionIdAndUserId(session.getId(), userId).orElseThrow(() -> new NotFoundException("Učesnik ne postoji"));

            participant.setAttended(attended);
            participant.setLeftEarly(!attended);
            participantRepository.save(participant);

            // poziv ka user-service za updateAttendance
            userClientService.updateAttendance(userId, attended);
        });

        session.setStatus(SessionStatus.CLOSED);
        sessionRepository.save(session);

        // poziv ka user-servisu za update organizatora
        userClientService.incrementOrganizedSessions(session.getOrganizerId());
    }

    @Override
    public long countParticipants(Long sessionId) {
        return participantRepository.countBySessionId(sessionId);
    }

    @Override
    public long countAttendedParticipants(Long sessionId) {
        Session session = getSession(sessionId);
        if (session.getStatus() != SessionStatus.CLOSED) {
            throw new BadRequestException("Sesija još nije završena");
        }
        return participantRepository.countBySessionIdAndAttendedTrue(sessionId);
    }

    // pomocna metoda za mapiranje ucesnika (participants)
    private SessionResponseDto mapToSessionResponseWithUsers(Session session) {
        SessionResponseDto dto = sessionMapper.toResponse(session);

        List<Long> participantIds = participantRepository.findBySessionId(session.getId()).stream().map(SessionParticipant::getUserId).collect(Collectors.toList());

        dto.setParticipantIds(participantIds);

        List<UserResponseDto> participants = participantIds.stream().map(userClientService::getUserById).collect(Collectors.toList());

        dto.setParticipants(participants);

        return dto;
    }

    private Session getSession(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new NotFoundException("Sesija ne postoji"));
    }

    @Override
    public List<SessionResponseDto> getFilteredSessions(Long gameId, String type, Integer maxPlayers, String description, Long userId, String sortBy) {

        SessionType sessionType = null;
        if (type != null && !type.isBlank()) {
            sessionType = SessionType.valueOf(type);
        }

        // prvo dohvatamo sesije koje zadovoljavaju filtere
        List<Session> sessions = sessionRepository.findFilteredSessions(gameId, sessionType, maxPlayers, description, userId);

        // mapiranje u DTO i dodavanje broja prijavljenih
        List<SessionResponseDto> sessionDtos = sessions.stream()
                .map(session -> {
                    SessionResponseDto dto = sessionMapper.toResponse(session);
                    dto.setParticipantIds(participantRepository.findBySessionId(session.getId())
                            .stream().map(SessionParticipant::getUserId)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        // sortiranje
        if (sortBy != null) {
            switch (sortBy) {
                case "startTime":
                    sessionDtos.sort(Comparator.comparing(SessionResponseDto::getStartTime));
                    break;
                case "participantsCount":
                    sessionDtos.sort(Comparator.comparingInt(a -> a.getParticipantIds().size()));
                    break;
                default:
                    // ne radimo ništa ako sortBy nije validan
                    break;
            }
        }

        return sessionDtos;
    }
}