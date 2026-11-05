package org.example.sessionservice.controller;

import org.example.sessionservice.dto.CloseSessionDto;
import org.example.sessionservice.dto.CreateSessionDto;
import org.example.sessionservice.dto.SessionResponseDto;
import org.example.sessionservice.security.CheckSecurity;
import org.example.sessionservice.security.SecurityContext;
import org.example.sessionservice.service.GameService;
import org.example.sessionservice.domain.Game;
import org.example.sessionservice.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final GameService gameService;

    public SessionController(SessionService sessionService, GameService gameService) {
        this.sessionService = sessionService;
        this.gameService = gameService;
    }

    // create
    @PostMapping
    @CheckSecurity(roles = {"PLAYER","ADMIN"})
    public ResponseEntity<SessionResponseDto> createSession(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid CreateSessionDto dto
    ) {
        Long organizerId = SecurityContext.getUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createSession(dto, organizerId));
    }

    // read
    @GetMapping
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<List<SessionResponseDto>> getAllSessions(
            @RequestHeader("Authorization") String authorization
    ) {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<SessionResponseDto> getSessionById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @GetMapping("/by-name")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<SessionResponseDto> getSessionByName(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(sessionService.getSessionByName(name));
    }

    // za filtiranje i sortiranje
    // npr. : /sessions/filtered?type=OPEN&sortBy=startTime&description=adventure
    @GetMapping("/filtered")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<List<SessionResponseDto>> getFilteredSessions(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer maxPlayers,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String sortBy
    ) {
        return ResponseEntity.ok(
                sessionService.getFilteredSessions(gameId, type, maxPlayers, description, userId, sortBy)
        );
    }


    /*
    // update
    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDto> updateSession(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid CreateSessionDto dto
    ) {
        return ResponseEntity.ok(sessionService.updateSession(id, dto));
    }



    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
    */

    // join
    @PostMapping("/{id}/join")
    @CheckSecurity(roles = {"PLAYER"})
    public ResponseEntity<SessionResponseDto> joinSession(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        Long userId = SecurityContext.getUserId();
        return ResponseEntity.ok(sessionService.joinSession(id, userId));
    }

    // leave
    @PostMapping("/{id}/leave")
    @CheckSecurity(roles = {"PLAYER"})
    public ResponseEntity<SessionResponseDto> leaveSession(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        Long userId = SecurityContext.getUserId();
        return ResponseEntity.ok(sessionService.leaveSession(id, userId));
    }

    // close session
    @PostMapping("/close")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<Void> closeSession(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid CloseSessionDto dto
    ) {
        Long currentUserId = SecurityContext.getUserId();
        sessionService.closeSession(dto, currentUserId);
        return ResponseEntity.ok().build();
    }

    // stats
    @GetMapping("/{id}/participants/count")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<Long> countParticipants(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(sessionService.countParticipants(id));
    }

    @GetMapping("/{id}/participants/attended/count")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<Long> countAttendedParticipants(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(sessionService.countAttendedParticipants(id));
    }

    @PostMapping("/{id}/cancel")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<Void> cancelSession(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        Long userId = SecurityContext.getUserId();
        sessionService.cancelSession(id, userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/invite")
    @CheckSecurity(roles = {"PLAYER"})
    public ResponseEntity<Void> inviteUser(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestParam Long invitedUserId
    ) {
        Long organizerId = SecurityContext.getUserId();
        sessionService.inviteUser(id, organizerId, invitedUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invite/accept/{token}")
    public ResponseEntity<SessionResponseDto> acceptInvite(
            @PathVariable String token
    ) {
        return ResponseEntity.ok(
                sessionService.acceptInvite(token)
        );
    }

    // game
    @GetMapping("/games")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @PostMapping("/games/create")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Game> createGame(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid Game game
    ) {
        Game created = gameService.createGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @PutMapping("/games/update/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Game> updateGame(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid Game game
    ) {
        return ResponseEntity.ok(gameService.updateGame(id, game));
    }

    @DeleteMapping("/games/delete/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Void> deleteGame(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        gameService.deleteGame(id);
        return ResponseEntity.ok().build();
    }
}