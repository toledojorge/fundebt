/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import dev.carlosmolero.fundebt.domain.user.UserEntity;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "custom")
@Data
public class CustomConfig {
    private List<UserEntity> users = new ArrayList<>();
    private String accessTokenSecret;
    private int accessTokenExpirationDays;
    private String[] allowedOrigins;
}
