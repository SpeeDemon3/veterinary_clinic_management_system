package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.LoginRequest;
import com.aruiz.user.notification.controller.dto.UserRequest;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        try {
            return ResponseEntity.ok("Hello world");
        } catch (Exception e) {
            log.info("Estoy aqui");
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest userRequest) {
        try {
            return ResponseEntity.ok(authenticationService.signup(userRequest));
        } catch (Exception e) {
            log.info("user Request: {}", userRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
