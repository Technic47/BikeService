package ru.kuznetsov.bikeService.exceptionHandlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
}
