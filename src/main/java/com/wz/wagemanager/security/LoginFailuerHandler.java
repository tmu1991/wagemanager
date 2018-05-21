package com.wz.wagemanager.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WindowsTen
 * @date 2018/2/24 12:41
 * @description
 */

public class LoginFailuerHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding ("utf-8");
        exception.printStackTrace();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getCause().getMessage ());
    }
}
