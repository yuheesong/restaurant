package com.restaurant.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = request.getParameter("email");
        String rememberMe = request.getParameter("rememberEmail");

        if ("on".equals(rememberMe)) {
            Cookie emailCookie = new Cookie("rememberedEmail", email);
            emailCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유지
            response.addCookie(emailCookie);
        } else {
            Cookie emailCookie = new Cookie("rememberedEmail", "");
            emailCookie.setMaxAge(0); // 쿠키 삭제
            response.addCookie(emailCookie);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
