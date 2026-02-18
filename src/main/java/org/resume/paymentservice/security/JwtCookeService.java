package org.resume.paymentservice.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.properties.JwtProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static org.resume.paymentservice.contants.SecurityConstants.COOKIE_NAME;

@Service
@RequiredArgsConstructor
public class JwtCookeService {

    private final JwtProperties jwtProperties;

    public void setAuthCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = buildCookie(token, jwtProperties.getExpirationSeconds());
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearAuthCookie(HttpServletResponse response) {
        ResponseCookie cookie = buildCookie("", 0);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private ResponseCookie buildCookie(String value, long maxAge) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(jwtProperties.isCookieSecure())
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }
}
