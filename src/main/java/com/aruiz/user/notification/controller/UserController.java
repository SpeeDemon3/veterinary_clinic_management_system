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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    /**
     * Handles HTTP POST requests for user signup.
     *
     * @param userRequest The request body containing user signup information.
     * @return ResponseEntity containing the result of the signup operation.
     */
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

    /**
     * Handles HTTP POST requests for user login.
     *
     * @param loginRequest The request body containing user login information.
     * @return ResponseEntity containing the result of the login operation.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Handles HTTP GET requests to find a user by ID.
     *
     * @param id The ID of the user to find.
     * @return ResponseEntity containing the result of the find operation.
     */
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

    /**
     * Handles HTTP GET requests to find all users.
     *
     * @return ResponseEntity containing the result of the find all operation.
     */
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

    /**
     * Handles HTTP DELETE requests to delete a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity containing the result of the delete operation.
     */
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

    /**
     * Handles HTTP POST requests to add an image to a user profile.
     *
     * @param id The ID of the user.
     * @param imageFile The image file to be added.
     * @return ResponseEntity indicating the status of the operation.
     */
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

    /**
     * Handles HTTP GET requests to retrieve a user's profile image.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the user's profile image.
     */
    @GetMapping("/userImg/{id}")
    public ResponseEntity<byte[]> getUserImg(@PathVariable Long id) {
        try {

            byte[] imgBytes = userService.getUserImg(id);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);

            log.info("Recovering image....");

            return new ResponseEntity<>(imgBytes, httpHeaders, HttpStatus.OK);

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles HTTP GET requests to download a CSV file containing user information.
     *
     * @return ResponseEntity containing the CSV file to download.
     * @throws IOException if an I/O error occurs while creating the CSV file.
     */
    @GetMapping(value= "/downloadFileCsvUsers")
    public ResponseEntity<?> downloadFileUsers() throws IOException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users-data.csv");

        byte[] csvBytes = userService.usersInfoDownloadCsv().getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

    }

    /**
     * Handles HTTP GET requests to download a JSON file containing information about users.
     *
     * @return ResponseEntity containing the JSON file with appropriate headers for download.
     */
    @GetMapping("/downloadFileJsonUsers")
    public ResponseEntity<?> downloadFileJsonPets() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "users.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(userService.usersInfoDownloadJson());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading pets JSON!!!!");
        }

    }



}
