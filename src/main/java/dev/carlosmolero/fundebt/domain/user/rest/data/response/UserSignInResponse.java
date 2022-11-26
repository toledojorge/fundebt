/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest.data.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSignInResponse {
    private String accessToken;
}
