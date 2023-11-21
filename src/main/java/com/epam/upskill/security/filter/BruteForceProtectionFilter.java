//package com.epam.upskill.security.filter;
//
//import com.epam.upskill.exception.AuthenticationException;
//import com.epam.upskill.util.BruteForceProtectionService;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class BruteForceProtectionFilter extends OncePerRequestFilter {
//
//  private final AuthenticationManager authenticationManager;
//  private final BruteForceProtectionService bruteForceProtectionService;
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//      throws ServletException, IOException {
//    try {
//      String username = obtainUsername(request);
//      if (username != null) {
//        checkBruteForceProtection(username);
//      }
//
//      chain.doFilter(request, response);
//
//    } catch (AuthenticationException e) {
//      // Handle authentication failure
//      String username = obtainUsername(request);
//      if (username != null) {
//        bruteForceProtectionService.registerLoginFailure(username);
//      }
//      throw e;
//    }
//  }
//
//  private void checkBruteForceProtection(String username) {
//    int loginAttempts = bruteForceProtectionService.getLoginAttempts(username);
//    if (loginAttempts >= 3) {
//      throw new LockedException("User account is locked due to multiple authentication failures.");
//    }
//  }
//
//  private String obtainUsername(HttpServletRequest request) {
//    // Extract username from the request, e.g., request.getParameter("username")
//    return null;
//  }
//}
