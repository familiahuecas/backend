package com.familiahuecas.backend.securiry;

import java.io.IOException;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Excluir rutas específicas, como /auth/login
        return path.startsWith("/auth/login") || path.startsWith("/authWeb");
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           log.debug("Encabezado de autorización no válido o ausente");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userName = jwtService.extractUsername(jwt);
            log.debug("Token recibido: " + jwt);
            log.debug("Usuario extraído: " + userName);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userName != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                log.debug("Detalles del usuario cargados: " + userDetails);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                	log.debug("Token JWT es válido");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // Establecer el usuario en el ThreadContext
                    ThreadContext.put("user", userName);
                   
                    log.debug("Autenticación establecida para el usuario: " + userName);
                } else {
                	log.error("Token JWT no es válido");
                }
            } else {
            	log.warn("Usuario no extraído o ya autenticado");
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
        	log.error("El token JWT ha expirado: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception exception) {
        	log.error("Se produjo una excepción durante la autenticación: " + exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }finally {
            // Limpiar el ThreadContext al final de la solicitud
            ThreadContext.clearAll();
        }
    }

}
