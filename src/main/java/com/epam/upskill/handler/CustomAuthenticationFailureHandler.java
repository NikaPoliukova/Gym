package com.epam.upskill.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final int MAX_ATTEMPTS = 3;
  private static final int LOCK_TIME = 300000;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
    String username = request.getParameter("username");
    HttpSession session = request.getSession();
    int attempts = getAttemptsFromSession(session, username) + 1;
    if (attempts > MAX_ATTEMPTS) {
      lockAccount(session, username);
      response.sendRedirect("/account-locked");
    } else {
      setAttemptsToSession(session, username, attempts);
    }
  }

  private int getAttemptsFromSession(HttpSession session, String username) {
    Integer attempts = (Integer) session.getAttribute(username + "_attempts");
    return (attempts != null) ? attempts : 0;
  }

  private void setAttemptsToSession(HttpSession session, String username, int attempts) {
    session.setAttribute(username + "_attempts", attempts);
  }

  private void lockAccount(HttpSession session, String username) {
    session.setAttribute(username + "_locked", System.currentTimeMillis());
    session.setAttribute(username + "_unlockTime", System.currentTimeMillis() + (LOCK_TIME));
  }
}
