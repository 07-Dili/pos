package org.dilip.first.pos_backend.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dilip.first.pos_backend.constants.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class SecurityFilter implements Filter {

    private static final long INACTIVE_LIMIT_SECONDS = 3600; //1hour

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();


        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "User not logged in");
            return;
        }

        Instant lastAccess = (Instant) session.getAttribute("lastAccess");
        Instant now = Instant.now();

        if (lastAccess == null || now.minusSeconds(INACTIVE_LIMIT_SECONDS).isAfter(lastAccess)) {
            session.invalidate();
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Session expired");
            return;
        }

        session.setAttribute("lastAccess", now);

        UserRole role = (UserRole) session.getAttribute("role");

        if (isUploadApi(path) && role == UserRole.OPERATOR) {
            res.sendError(HttpStatus.FORBIDDEN.value(), "Operator not allowed to upload");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return path.startsWith("/pos/login")
                || path.startsWith("/pos/signup")
                || path.startsWith("/pos/logout")
                || path.startsWith("/pos/swagger-ui")
                || path.startsWith("/pos/v3/api-docs");
    }


    private boolean isUploadApi(String path) {
        return path.contains("/products/upload") || path.contains("/inventory/upload");
    }
}

