/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.carlosmolero.fundebt.domain.user.UserEntity;
import dev.carlosmolero.fundebt.domain.user.UserRepository;
import dev.carlosmolero.fundebt.utils.JWTUtils;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Optional<String> accessToken = parseAccessToken(request);
            if (accessToken.isPresent() && jwtUtils.validateAccessToken(accessToken.get())) {
                String userId = jwtUtils.getUserIdFromAccessToken(accessToken.get());
                UserEntity userEntity = userRepository.findById(Long.valueOf(userId)).orElse(null);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEntity,
                        null,
                        userEntity.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("userId", userId);
                filterChain.doFilter(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Optional<String> parseAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(PREFIX)) {
            return Optional.of(authHeader.replace(PREFIX, ""));
        }
        return Optional.empty();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().contains("/api/") || request.getServletPath().contains("/auth/signin")
                || request.getServletPath().contains("/user/all");
    }

}
