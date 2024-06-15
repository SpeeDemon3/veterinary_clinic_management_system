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
public class RoleController {

    private final RoleServicesImpl roleServices;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@RequestBody RoleRequest roleRequest) {
        try {
            log.info("ROLE: {}" + roleRequest);
            return ResponseEntity.ok(roleServices.save(roleRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error with the arguments of the inserted role!!!!");
        }
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(roleServices.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found!!!");
        }
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(roleServices.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There are no roles saved in the database!!!");
        }
    }

    @GetMapping("/findAllByName/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAllByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(roleServices.findAllByName(name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role name not found!!!!");
        }
    }

    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {
        try {
            return ResponseEntity.ok(roleServices.updateById(id, roleRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with ID " + id + "  not found!!!");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(roleServices.deleteById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with ID " + id + "  not found!!!");
        }
    }


}
