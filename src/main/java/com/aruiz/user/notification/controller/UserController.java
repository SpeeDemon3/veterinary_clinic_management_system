package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.LoginRequest;
import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.controller.dto.UserRequest;
import com.aruiz.user.notification.controller.dto.UserRequestUpdate;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    private final UserServiceImpl userService;

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
    public ResponseEntity<?> signup(@RequestBody SignUpRequest userRequest) {
        try {
            log.info(userRequest.toString());
            return ResponseEntity.ok(authenticationService.signup(userRequest));
        } catch (Exception e) {
            log.error(e.toString());
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

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody UserRequestUpdate userRequest) {
        try {
            return ResponseEntity.ok(userService.updateById(id, userRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.deleteById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/userImg/{id}/add")
    public ResponseEntity<String> addPetImg(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
        try {

            if (!imageFile.getOriginalFilename().contains(".png")) {
                log.error("The image must be in PNG format!!!!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            userService.addUserImg(id, imageFile);

            log.info("Image name: {}", imageFile.getOriginalFilename());
            log.info("Image size: {}", imageFile.getSize());

            return ResponseEntity.ok("Image successfully saved!!!");
        } catch (Exception e) {
            log.error("The image could not be added to the user.");
            log.info(imageFile.getOriginalFilename());
            log.info(String.valueOf(imageFile.getSize()));
            log.info(imageFile.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }






}
