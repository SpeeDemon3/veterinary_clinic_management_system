package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for handling user-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    private final UserServiceImpl userService;

    /**
     * Handles HTTP POST requests for user signup.
     *
     * @param userRequest The request body containing user signup information.
     * @return ResponseEntity containing the result of the signup operation.
     */
    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest userRequest) {
        try {
            log.info("Received signup request: {}", userRequest);
            JwtResponse response = authenticationService.signup(userRequest);
            log.info("User signed up successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during signup: ", e);
            log.info("user Request: {}", userRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Handles HTTP POST requests for user login.
     *
     * @param loginRequest The request body containing user login information.
     * @return ResponseEntity containing the result of the login operation.
     */
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Received login request: {}", loginRequest);
            JwtResponse response = authenticationService.login(loginRequest);
            log.info("User loged up successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during login: ", e);
            log.info("user Login: {}", loginRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());        }
    }

    /**
     * Handles HTTP GET requests to find a user by ID.
     *
     * @param id The ID of the user to find.
     * @return ResponseEntity containing the result of the find operation.
     */
    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN', 'ROLE_ADMIN')")
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

    /**
     * Retrieves a user by their DNI (Documento Nacional de Identidad).
     *
     * @param dni The DNI of the user to find.
     * @return ResponseEntity with user information if found, otherwise appropriate error response.
     */
    @GetMapping("/findByDni/{dni}")
    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    public ResponseEntity<?> findByDni(@PathVariable String dni) {
        try {
            // Attempt to find the user by DNI using the userService
            return ResponseEntity.ok(userService.findByDni(dni));
        } catch (EntityNotFoundException e) {
            // Handle case where user with given DNI is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            // Handle case where DNI format or input is invalid
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Handle any other unexpected exceptions with a generic server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles HTTP GET requests to find all users.
     *
     * @return ResponseEntity containing the result of the find all operation.
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN', 'ROLE_ADMIN')")
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
     * Updates the user information by the given ID.
     *
     * @param id the ID of the user to be updated.
     * @param userRequest the updated user information.
     * @return ResponseEntity containing the updated user information or an appropriate error status.
     */
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.deleteById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Bad request");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_VETERINARIAN', 'ROLE_ADMIN')")
    public ResponseEntity<String> addUserImg(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_VETERINARIAN', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> downloadFileCsvUsers() throws IOException {
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    /**
     * Updates the role of a user identified by their DNI.
     *
     * @param dni    The DNI (Documento Nacional de Identidad) of the user.
     * @param idRole The ID of the role to be assigned to the user.
     * @return ResponseEntity containing the updated user with the new role if successful.
     *         Returns a bad request response if the request is invalid,
     *         a not found response if the user or role is not found,
     *         or an internal server error response if an unexpected error occurs.
     */
    @PutMapping("/updateRoleUser/{dni}/{idRole}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateRoleUser(@PathVariable String dni, @PathVariable Long idRole) {
        try{
            return ResponseEntity.ok(userService.updateRoleByDni(dni, idRole));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
