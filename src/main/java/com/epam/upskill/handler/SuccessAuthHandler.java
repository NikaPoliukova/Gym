package com.epam.upskill.handler;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.service.BruteForceService;
import com.epam.upskill.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SuccessAuthHandler extends SimpleUrlAuthenticationSuccessHandler {
  private static final String TOKEN_NAME = "JWT";
  private static final String REFRESH_TOKEN_NAME = "JWT-REFRESH";
  private static final long EXPIRATION = Duration.ofHours(10).toSeconds();
  private final BruteForceService bruteForceService;
  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    if (bruteForceService.isAccountLocked(request, authentication.getName())) {
      response.sendRedirect("/account-locked");
    } else {
      var token = jwtUtils.getAccessTokenFromRequest(request);
      var refreshToken = jwtUtils.getRefreshTokenFromRequest(request);
      if (token == null && refreshToken == null) {
        var user = userRepository.findByUsername(authentication.getName());
        String tokenNew = jwtUtils.generateAccessToken(user.get());
        String refreshTokenNew = jwtUtils.generateRefreshToken(user.get().getUsername());
        deleteCookies(request, response, TOKEN_NAME);
        deleteCookies(request, response, REFRESH_TOKEN_NAME);
        final Cookie cookieAccess = new Cookie(TOKEN_NAME, tokenNew);
        final Cookie cookieRefresh = new Cookie(REFRESH_TOKEN_NAME, refreshTokenNew);
        Arrays.asList(cookieRefresh, cookieAccess)
            .forEach(cookie -> {
              cookie.setPath("/");
              cookie.setHttpOnly(true);
              cookie.setMaxAge((int) EXPIRATION);
            });
        response.addCookie(cookieAccess);
        response.addCookie(cookieRefresh);
        response.setStatus(HttpStatus.OK.value());
        setAuthentication(tokenNew);
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:8091/home");
      }
    }
  }

  private void setAuthentication(String token) {
    String userName = jwtUtils.getUsernameFromToken(token);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... cookieNames) {
    for (String cookieName : cookieNames) {
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
  }
}
