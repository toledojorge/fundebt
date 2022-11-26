/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import dev.carlosmolero.fundebt.domain.user.UserServiceImpl;

@SpringBootApplication
@EnableCaching
public class FundebtApplication {
	// ! Needs to be initialized on startup to create
	// ! users from the .yaml file
	@Autowired
	UserServiceImpl userService;

	public static void main(String[] args) {
		SpringApplication.run(FundebtApplication.class, args);
	}
}