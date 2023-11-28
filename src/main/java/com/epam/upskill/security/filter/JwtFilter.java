package com.epam.upskill.security.filter;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;
  private static final String TOKEN_NAME = "JWT";


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var token = jwtUtils.getAccessTokenFromRequest(request);
    var refreshToken = jwtUtils.getRefreshTokenFromRequest(request);
    if (token != null && jwtUtils.validateToken(token)) {
      setAuthentication(token);
    } else if (token != null && refreshToken != null && jwtUtils.isTokenExpired(token)
        && jwtUtils.validateToken(refreshToken)) {
      final String newAccessToken = jwtUtils.refreshAccessToken(token);
      setAuthentication(newAccessToken);
      setAccessTokenInCookie(response, newAccessToken);
    }
    filterChain.doFilter(request, response);

  }

  private void setAccessTokenInCookie(HttpServletResponse response, String token) {
    Cookie accessToken = new Cookie(TOKEN_NAME, token);
    accessToken.setPath("/");
    accessToken.setSecure(true);
    accessToken.setHttpOnly(true);
    response.addCookie(accessToken);
  }

  private void setAuthentication(String token) {
    String userName = jwtUtils.getUsernameFromToken(token);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}