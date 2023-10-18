package ru.kuznetsov.bikeService.config.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.kuznetsov.bikeService.customExceptions.JwtAuthenticationException;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (MalformedJwtException ex) {
            throw new JwtAuthenticationException("Invalid JWT token");

        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("Expired JWT token");

        }
//        catch (UnsupportedJwtException ex) {
//            throw new JwtAuthenticationException("Expired JWT token");
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported JWT token");
//
//        } catch (IllegalArgumentException ex) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT claims string is empty");
//
//        }


//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//
//            if (auth != null) {
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
    }
}
