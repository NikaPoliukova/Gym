//package upskill.handler;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import upskill.dao.UserRepository;
//import upskill.security.JwtUtils;
//import upskill.service.BruteForceService;
//
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Arrays;
//
//@Component
//@RequiredArgsConstructor
//public class SuccessAuthHandler extends SimpleUrlAuthenticationSuccessHandler {
//  private static final String TOKEN_NAME = "JWT";
//  private static final String REFRESH_TOKEN_NAME = "JWT-REFRESH";
//  private static final long EXPIRATION = Duration.ofHours(10).toSeconds();
//  public static final String REDIRECT_URL = "http://localhost:8091/home";
//
//  private final BruteForceService bruteForceService;
//  private final JwtUtils jwtUtils;
//  private final UserRepository userRepository;
//
//  @Override
//  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                      Authentication authentication) throws IOException {
//    if (bruteForceService.isAccountLocked(request, authentication.getName())) {
//      response.sendRedirect("/account-locked");
//    } else {
//      var token = jwtUtils.getAccessTokenFromRequest(request);
//      var refreshToken = jwtUtils.getRefreshTokenFromRequest(request);
//      checkTokens(response, authentication, token, refreshToken);
//      getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);
//    }
//  }
//
//  private void checkTokens(HttpServletResponse response, Authentication authentication, String token,
//                           String refreshToken) {
//    if (token == null && refreshToken == null) {
//      var user = userRepository.findByUsername(authentication.getName());
//      var tokenNew = jwtUtils.generateAccessToken(user.get());
//      var refreshTokenNew = jwtUtils.generateRefreshToken(user.get().getUsername());
//      cleanCookies(response);
//      final Cookie cookieAccess = new Cookie(TOKEN_NAME, tokenNew);
//      final Cookie cookieRefresh = new Cookie(REFRESH_TOKEN_NAME, refreshTokenNew);
//      addTokensInCookies(cookieAccess, cookieRefresh);
//      addTokensToResponse(response, cookieAccess, cookieRefresh);
//      setAuthentication(tokenNew);
//    }
//  }
//
//  private static void addTokensInCookies(Cookie cookieAccess, Cookie cookieRefresh) {
//    Arrays.asList(cookieRefresh, cookieAccess)
//        .forEach(cookie -> {
//          cookie.setPath("/");
//          cookie.setHttpOnly(true);
//          cookie.setMaxAge((int) EXPIRATION);
//        });
//  }
//
//  private static void addTokensToResponse(HttpServletResponse response, Cookie cookieAccess, Cookie cookieRefresh) {
//    response.addCookie(cookieAccess);
//    response.addCookie(cookieRefresh);
//    response.setStatus(HttpStatus.OK.value());
//  }
//
//  private void cleanCookies(HttpServletResponse response) {
//    deleteCookies(response, TOKEN_NAME);
//    deleteCookies(response, REFRESH_TOKEN_NAME);
//  }
//
//  private void setAuthentication(String token) {
//    var userName = jwtUtils.getUsernameFromToken(token);
//    var authentication = new UsernamePasswordAuthenticationToken(userName, null, null);
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//  }
//
//  private void deleteCookies(HttpServletResponse response, String... cookieNames) {
//    for (String cookieName : cookieNames) {
//      var cookie = new Cookie(cookieName, null);
//      cookie.setPath("/");
//      cookie.setMaxAge(0);
//      response.addCookie(cookie);
//    }
//  }
//}
