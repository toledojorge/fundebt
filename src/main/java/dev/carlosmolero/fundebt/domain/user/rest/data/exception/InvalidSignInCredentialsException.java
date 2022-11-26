/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest.data.exception;

public class InvalidSignInCredentialsException extends RuntimeException {
    public InvalidSignInCredentialsException() {
        super("Invalid sign in credentials");
    }
}
