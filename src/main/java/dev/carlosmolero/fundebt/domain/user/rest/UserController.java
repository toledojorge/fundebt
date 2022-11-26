/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.carlosmolero.fundebt.domain.user.UserRepository;
import dev.carlosmolero.fundebt.domain.user.UserServiceImpl;
import dev.carlosmolero.fundebt.domain.user.rest.data.request.UsersRequest;
import dev.carlosmolero.fundebt.domain.user.rest.data.response.UserResponseMin;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserRestMapper userRestMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/principal")
    public ResponseEntity<UserResponseMin> get(@RequestAttribute("userId") Long userId) {
        return ResponseEntity
                .ok(userRestMapper.fromUserEntityToUserResponseMin(userRepository.findById(userId).orElse(null)));
    }

    @PostMapping("/all")
    public ResponseEntity<List<?>> getAll(@RequestBody(required = false) UsersRequest usersRequest) {
        if (usersRequest != null && usersRequest.getPrincipalEmail() != null) {
            return ResponseEntity
                    .ok(userRestMapper
                            .fromUserEntityListToUserResponseList(
                                    userService.getNonPrincipal(usersRequest.getPrincipalEmail())));
        }
        return ResponseEntity.ok(userRestMapper.fromUserEntityListToUserResponseMinList(userRepository.findAll()));
    }

}
