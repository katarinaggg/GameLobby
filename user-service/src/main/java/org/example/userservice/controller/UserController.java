package org.example.userservice.controller;

import org.example.userservice.dto.*;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.security.CheckSecurity;
import org.example.userservice.security.SecurityContext;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.userservice.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // vraca listu svih usera
    // samo admin ima pristup -> zbog block/unblock
    @GetMapping
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestHeader("Authorization") String authorization) {

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        UserResponseDto user = userService.registerPlayer(registerRequestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        TokenResponseDto token = userService.login(tokenRequestDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/me")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<UserResponseDto> getProfile(
            @RequestHeader("Authorization") String authorization) {

        Long userId = SecurityContext.getUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }


    @PutMapping("/profile")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UpdateProfileDto dto) {

        Long userId = SecurityContext.getUserId();
        return ResponseEntity.ok(userService.updateProfile(userId, dto));
    }

    @PutMapping("/password")
    @CheckSecurity(roles = {"PLAYER", "ADMIN"})
    public ResponseEntity<Void> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChangePasswordDto dto) {

        Long userId = SecurityContext.getUserId();
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/block")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Void> blockUser(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {

        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unblock")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Void> unblockUser(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {

        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    // putanja -> link za aktivaciju (aktivacini token)
    // klikom na aktivacioni link okinuce se ova metoda
    @GetMapping("/activate/{token}")
    public ResponseEntity<Void> activateUser(@PathVariable String token) {
        userService.activateUser(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // session service callbacks

    @PostMapping("/{id}/increment-sessions-applied")
    public ResponseEntity<Void> incrementSessionsApplied(
            @PathVariable Long id
    ) {
        userService.incrementSessionsApplied(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decrement-sessions-applied")
    public ResponseEntity<Void> decrementSessionsApplied(
            @PathVariable Long id
    ) {
        userService.decrementSessionsApplied(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/update-attendance")
    public ResponseEntity<Void> updateAttendance(
            @PathVariable Long id,
            @RequestParam boolean attended
    ) {
        userService.updateAttendance(id, attended);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/increment-organized-sessions")
    public ResponseEntity<Void> incrementOrganizedSessions(
            @PathVariable Long id
    ) {
        userService.incrementOrganizedSessions(id);
        return ResponseEntity.ok().build();
    }


}
