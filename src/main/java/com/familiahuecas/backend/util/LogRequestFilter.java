package com.familiahuecas.backend.util;

import com.familiahuecas.backend.securiry.JwtService;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LogRequestFilter implements Filter {

    private final JwtService jwtService;

    public LogRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización si es necesario
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;

                // Obtener la IP del cliente
                String clientIp = httpServletRequest.getHeader("X-Forwarded-For");
                if (clientIp == null || clientIp.isEmpty()) {
                    clientIp = httpServletRequest.getRemoteAddr();
                }
                ThreadContext.put("addr", clientIp);


                // Obtener el token JWT del encabezado de autorización
                String authHeader = httpServletRequest.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String jwt = authHeader.substring(7);
                    String userName = jwtService.extractUsername(jwt); // Usa el servicio JWT para obtener el usuario

                    if (userName != null) {
                        ThreadContext.put("user", userName); // Añadir el usuario al contexto del log
                    }
                }
            }

            // Continuar con el procesamiento de la solicitud
            chain.doFilter(request, response);
        } finally {
            // Limpiar el contexto del log después de procesar la solicitud
            ThreadContext.clearAll();
        }
    }

    @Override
    public void destroy() {
        // Limpieza si es necesario
    }
}
