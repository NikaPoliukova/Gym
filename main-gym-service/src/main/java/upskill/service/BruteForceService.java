package upskill.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class BruteForceService {

  public boolean isAccountLocked(HttpServletRequest request, String username) {
    var session = request.getSession();
    var unlockTime = (Long) session.getAttribute(username + "_unlockTime");
    return unlockTime != null;
  }
}
