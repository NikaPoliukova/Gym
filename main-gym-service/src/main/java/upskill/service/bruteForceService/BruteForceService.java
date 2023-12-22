package upskill.service.bruteForceService;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BruteForceService {

  public boolean isAccountLocked(HttpServletRequest request, String username) {
    var session = request.getSession();
    var unlockTime = (Long) session.getAttribute(username + "_unlockTime");
    return unlockTime != null;
  }
}
