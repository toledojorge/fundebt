/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest.data.response;

import lombok.Data;

@Data
public class UserResponseMin {
    private String fullName;
    private String email;
    private String avatarUrl;
}
