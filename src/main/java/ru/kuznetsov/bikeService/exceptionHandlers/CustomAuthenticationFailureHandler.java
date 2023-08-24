package ru.kuznetsov.bikeService.exceptionHandlers;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        setDefaultFailureUrl("/login?error=true");

        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = "Неправильные имя и пароль.";

        if (exception.getMessage().contains("User is disabled")) {
            errorMessage = "Пользователь не активирован.";
        } else if (exception.getMessage().contains("User account has expired")) {
            errorMessage = "Аккаунт пользователя просрочен.";
        } else if (exception.getMessage().contains("User not found")) {
            errorMessage = "Аккаунт пользователя не найден.";
        } else if (exception.getMessage().contains("Token not found")) {
            errorMessage = "Токен не найден.";
        } else if (exception.getMessage().contains("Token expired")) {
            errorMessage = "Токен просрочен.";
        } else if (exception.getMessage().contains("JWT expired")) {
            errorMessage = "JWT токен просрочен.";
        }

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }

    @ExceptionHandler({ExpiredJwtException.class, MissingCsrfTokenException.class, InvalidCsrfTokenException.class, SessionAuthenticationException.class})
    public ErrorInfo handleAuthenticationException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), "error.authorization");
    }

    public static class ErrorInfo {
        private final String url;
        private final String info;

        ErrorInfo(String url, String info) {
            this.url = url;
            this.info = info;
        }

        public String getUrl() {
            return url;
        }

        public String getInfo() {
            return info;
        }
    }
}
