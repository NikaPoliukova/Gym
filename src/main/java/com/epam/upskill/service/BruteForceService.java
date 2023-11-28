package com.epam.upskill.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class BruteForceService {

  public boolean isAccountLocked(HttpServletRequest request, String username) {
    HttpSession session = request.getSession();
    var isLocked = (Long) session.getAttribute(username + "_locked");
    Long unlockTime = (Long) session.getAttribute(username + "_unlockTime");
    if (isLocked == null && unlockTime == null) {
      return false;
    }
    if (unlockTime > System.currentTimeMillis()) {
      return false;
    } else return true;
  }
}
