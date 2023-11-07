package com.epam.upskill.interceptor;

import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
  private final UserService userService;


  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String requestUri = request.getRequestURI();
    if (requestUri.startsWith("/api/v1/registration")|| requestUri.contains("/swagger-ui.html")
        || requestUri.startsWith("/v3/api-docs") || requestUri.startsWith("/swagger-ui")
        || requestUri.contains("/swagger-ui/index.css")) {
      return true;
    }
    String username = request.getHeader("username");
    String password = request.getHeader("password");
    try {
      userService.findByUsernameAndPassword(username, password);
      return true;
    } catch (UserNotFoundException ex) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
  }
}
