/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user;

import java.util.List;

public interface IUserService {
    public UserEntity signIn(String email, String password);

    public UserEntity getPrincipal(Long id);

    public List<UserEntity> getNonPrincipal(String email);
}
