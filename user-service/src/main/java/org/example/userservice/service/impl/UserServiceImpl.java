package org.example.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.userservice.domain.Role;
import org.example.userservice.domain.Title;
import org.example.userservice.domain.User;
import org.example.userservice.dto.*;
import org.example.userservice.exception.BadRequestException;
import org.example.userservice.exception.NotFoundException;
import org.example.userservice.helper.MessageHelper;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.security.service.TokenService;
import org.example.userservice.service.UserService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;

    @Value("${notification.queue.name}")
    private String notificationQueueName;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, TokenService tokenService, JmsTemplate jmsTemplate, MessageHelper messageHelper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
    }

    @Override
    public UserResponseDto registerPlayer(RegisterRequestDto registerRequestDto) {

        // provera jedinstvenosti
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new BadRequestException("Email već postoji");
        }
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new BadRequestException("Korisničko ime već postoji");
        }

        // mapiranje DTO -> Entity
        User user = userMapper.fromRegisterDto(registerRequestDto);

        // generisemo activation token
        String activationToken = UUID.randomUUID().toString();
        user.setActivationToken(activationToken);
        // on ce se poslai kao link u aktivacionom mejlu

        // cuvanje u bazi
        User saved = userRepository.save(user);

        // pravimo dto objekat koji cemo da saljemo notif-servisu
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto(
                saved.getEmail(),
                NotificationMessageDto.NotificationType.ACTIVATION_EMAIL,
                Map.of(
                        "activationLink",
                        "http://localhost:8081/api/user/activate/" + activationToken
                )
        );

        // saljemo u red za notif-service, kreiramo poruku preko helper metode
        jmsTemplate.convertAndSend(
                notificationQueueName,
                messageHelper.createTextMessage(notificationMessageDto)
        );

        return userMapper.toUserResponseDto(saved);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository
                .findByEmailAndPassword(
                        tokenRequestDto.getEmail(),
                        tokenRequestDto.getPassword()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Pogrešan email ili lozinka"
                        )
                );


        // ako je nalog aktiviran i nije blokiran, generise se token
        if (!user.isActive()) {
            throw new BadRequestException("Nalog nije aktiviran");
        }

        if (user.isBlocked()) {
            throw new BadRequestException("Nalog je blokiran");
        }

        // pravljenje JWT payload-a
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole().name());

        // generisanje tokena
        String token = tokenService.generate(claims);

        return new TokenResponseDto(token);
    }


    // kada dobije mejl moze da aktivira nalog
    @Override
    public void activateUser(String token) {
        // aktivacioni token
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() ->
                        new NotFoundException("Nevažeći ili istekao aktivacioni link")
                );

        // ako je već aktivan
        if (user.isActive()) {
            throw new BadRequestException("Nalog je već aktiviran");
        }

        // AKTIVACIJA
        user.setActive(true);
        // POTROŠI TOKEN (jednom aktiviran -> zauvek vazi)
        user.setActivationToken(null);

        userRepository.save(user);
    }


    // admin moze da blokira/odblokira korisnika
    @Override
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new NotFoundException("Korisnik ne postoji"));
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new NotFoundException("Korisnik ne postoji"));
        user.setBlocked(false);
        userRepository.save(user);
    }


    // ne moze da se menja email i korisnicko ime zbog kompleksnosti
    @Override
    public UserResponseDto updateProfile(Long userId, UpdateProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji"));

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getDateOfBirth() != null) {
            user.setDateOfBirth(dto.getDateOfBirth());
        }

        User saved = userRepository.save(user);
        return userMapper.toUserResponseDto(saved);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new BadRequestException("Pogrešna stara lozinka");
        }

        // updatujemo sifru i u bazi
        user.setPassword(newPassword);
        userRepository.save(user);

        // pravimo dto objekat za notif-service
        NotificationMessageDto notificationDto =
                new NotificationMessageDto(
                        user.getEmail(),
                        NotificationMessageDto.NotificationType.PASSWORD_CHANGED,
                        Map.of()
                );

        // saljemo ga u active mq
        jmsTemplate.convertAndSend(
                notificationQueueName,
                messageHelper.createTextMessage(notificationDto)
        );
    }


    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji"));
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji"));
        return userMapper.toUserResponseDto(user);
    }


    @Override
    public UserResponseDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji"));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllByRoleNot(Role.ADMIN)
                .stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public void incrementSessionsApplied(Long userId) {
        User user = findById(userId);
        user.setTotalSessionsApplied(user.getTotalSessionsApplied() + 1);
        recalculateAttendance(user);
        userRepository.save(user);
    }

    @Override
    public void updateAttendance(Long userId, boolean attended) {
        User user = findById(userId);

        if (attended) {
            user.setTotalSessionsAttended(user.getTotalSessionsAttended() + 1);
        } else {
            user.setTotalSessionsAttended(user.getTotalSessionsMissed() + 1);
        }

        recalculateAttendance(user);
        userRepository.save(user);
    }

    @Override
    public void incrementOrganizedSessions(Long userId) {
        User user = findById(userId);

        user.setOrganizedSessionsCount(user.getOrganizedSessionsCount() + 1);
        // updatujemo titulu ako je presao odredjenu granicu
        updateOrganizerTitle(user);

        userRepository.save(user);
    }

    @Override
    public void decrementSessionsApplied(Long userId) {
        User user = findById(userId);

        if (user.getTotalSessionsApplied() > 0) {
            user.setTotalSessionsApplied(user.getTotalSessionsApplied() - 1);
        }

        recalculateAttendance(user);
        userRepository.save(user);
    }


    // helperi
    private void updateOrganizerTitle(User user) {
        int count = user.getOrganizedSessionsCount();

        if (count >= 100) user.setOrganizerTitle(Title.KNEZ);
        else if (count >= 50) user.setOrganizerTitle(Title.VOJVODA);
        else if (count >= 25) user.setOrganizerTitle(Title.HAJDUK);
        else if (count >= 10) user.setOrganizerTitle(Title.BARJAKTAR);
        else user.setOrganizerTitle(Title.NONE);
    }

    // svaki put updatujemo
    private void recalculateAttendance(User user) {
        int applied = user.getTotalSessionsApplied();
        if (applied == 0) {
            user.setAttendancePercentage(100.0);
            return;
        }

        double percentage =
                (double) user.getTotalSessionsApplied() / applied * 100;

        user.setAttendancePercentage(percentage);
    }
}
