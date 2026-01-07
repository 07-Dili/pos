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

    private static final long INACTIVE_LIMIT_SECONDS = 3600;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

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
        return path.startsWith("/pos/auth/login")
                || path.startsWith("/pos/auth/signup")
                || path.startsWith("/pos/auth/logout")
                || path.startsWith("/pos/swagger-ui")
                || path.startsWith("/pos/v3/api-docs");
    }

    private boolean isUploadApi(String path) {
        return path.contains("/products/upload") || path.contains("/inventory/upload");
    }
}
