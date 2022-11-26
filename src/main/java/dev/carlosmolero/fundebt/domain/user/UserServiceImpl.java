/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.carlosmolero.fundebt.config.CustomConfig;
import dev.carlosmolero.fundebt.domain.user.rest.data.exception.InvalidSignInCredentialsException;
import dev.carlosmolero.fundebt.domain.user.rest.data.exception.UserNotFoundException;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        long count = userRepository.count();
        if (count < customConfig.getUsers().size()) {
            for (UserEntity u : customConfig.getUsers()) {
                u.setPassword(BCrypt.withDefaults().hashToString(12, u.getPassword().toCharArray()));
                userRepository.save(u);
            }
        }
    }

    @Override
    public UserEntity getPrincipal(Long id) {
        UserEntity principal = userRepository.findById(id).orElse(null);
        if (principal == null) {
            throw new UserNotFoundException();
        }
        return principal;
    }

    @Override
    @Cacheable("nonPrincipalUsers")
    public List<UserEntity> getNonPrincipal(String email) {
        return userRepository.findNonPrincipal(email);
    }

    @Override
    public UserEntity signIn(String email, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        UserEntity user = optionalUser.get();
        if (user.isPasswordOk(password)) {
            return user;
        }
        throw new InvalidSignInCredentialsException();
    }
}
