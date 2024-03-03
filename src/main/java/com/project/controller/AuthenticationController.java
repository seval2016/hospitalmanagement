package com.project.controller;


import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.authentication.LoginRequest;

import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;

import com.project.payload.response.user.UserResponse;

import com.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login") // http://localhost:8080/auth/login  + POST
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @GetMapping("/user") // http://localhost:8080/auth/user + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<UserResponse> findByUsername(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        UserResponse userResponse = authenticationService.findByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/updatePassword") // http://localhost:8080/auth/updatePassword + Patch + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
                                                 HttpServletRequest request){
        authenticationService.updatePassword(updatePasswordRequest, request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE;
        return ResponseEntity.ok(response);
    }

}
