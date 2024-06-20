package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.service.impl.RoleServicesImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling role-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

    private final RoleServicesImpl roleServices;

    /**
     * Adds a new role. Requires ROLE_ADMIN authorization.
     *
     * @param roleRequest The request body containing details of the role to be added.
     * @return ResponseEntity with OK status and the saved role if successful, otherwise BAD_REQUEST status with an error message.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@RequestBody RoleRequest roleRequest) {
        try {
            log.info("ROLE: {}" + roleRequest);
            // Call the service method to save the role
            return ResponseEntity.ok(roleServices.save(roleRequest));
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error with the arguments of the inserted role!!!!");
        }
    }

    /**
     * Finds a role by its ID. Requires ROLE_ADMIN authorization.
     *
     * @param id The ID of the role to find.
     * @return ResponseEntity with OK status and role if found, otherwise NOT_FOUND status with an error message.
     */
    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            // Call the service method to find role by ID
            return ResponseEntity.ok().body(roleServices.findById(id));
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found!!!");
        }
    }

    /**
     * Finds all roles. Requires ROLE_ADMIN authorization.
     *
     * @return ResponseEntity with OK status and list of roles if found, otherwise appropriate error response.
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAll() {
        try {
            // Call the service method to find all roles
            return ResponseEntity.ok(roleServices.findAll());
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There are no roles saved in the database!!!");
        }
    }

    /**
     * Finds all roles by name. Requires ROLE_ADMIN authorization.
     *
     * @param name The name of the roles to find.
     * @return ResponseEntity with OK status and list of roles if found, otherwise appropriate error response.
     */
    @GetMapping("/findAllByName/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAllByName(@PathVariable String name) {
        try {
            // Call the service method to find all roles by name
            return ResponseEntity.ok(roleServices.findAllByName(name));
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role name not found!!!!");
        }
    }

    /**
     * Updates a role by its ID. Requires ROLE_ADMIN authorization.
     *
     * @param id The ID of the role to update.
     * @param roleRequest The updated role details.
     * @return ResponseEntity with OK status and updated role if successful, otherwise appropriate error response.
     */
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {
        try {
            // Call the service method to update the role by ID
            return ResponseEntity.ok(roleServices.updateById(id, roleRequest));
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with ID " + id + "  not found!!!");
        }
    }

    /**
     * Deletes a role by its ID. Requires ROLE_ADMIN authorization.
     *
     * @param id The ID of the role to delete.
     * @return ResponseEntity with OK status if deletion is successful, otherwise appropriate error response.
     */
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            // Call the service method to delete the role by ID
            return ResponseEntity.ok(roleServices.deleteById(id));
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with ID " + id + "  not found!!!");
        }
    }


}
