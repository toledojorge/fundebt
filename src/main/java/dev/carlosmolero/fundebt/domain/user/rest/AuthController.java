/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.carlosmolero.fundebt.domain.user.UserEntity;
import dev.carlosmolero.fundebt.domain.user.UserServiceImpl;
import dev.carlosmolero.fundebt.domain.user.rest.data.request.UserSignInRequest;
import dev.carlosmolero.fundebt.domain.user.rest.data.response.UserSignInResponse;
import dev.carlosmolero.fundebt.utils.JWTUtils;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest) {
        UserEntity signedUser = userService.signIn(userSignInRequest.getEmail(),
                userSignInRequest.getPassword());
        return ResponseEntity
                .ok(UserSignInResponse.builder().accessToken(jwtUtils.generateAccessToken(signedUser.getId())).build());
    }
}
