//package com.epam.upskill.interceptor;
//
//import com.epam.upskill.exception.UserNotFoundException;
//import com.epam.upskill.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//@RequiredArgsConstructor
//@Component
//public class AuthInterceptor implements HandlerInterceptor {
//
//  private final UserService userService;
//
//  @Override
//  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//    String requestUri = request.getRequestURI();
//    if (requestUri.contains("/createTrainee") || requestUri.contains("/createTrainer")
//        || requestUri.contains("/swagger-ui.html")) {
//      return true;
//    }
//    String username = request.getHeader("username");
//    String password = request.getHeader("password");
//    try {
//      userService.findByUsernameAndPassword(username, password);
//      return true;
//    } catch (UserNotFoundException ex) {
//      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      return false;
//    }
//  }
//}
