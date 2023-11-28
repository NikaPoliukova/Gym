//package com.epam.upskill.controller;
//
//import com.epam.upskill.exception.AuthenticationException;
//import com.epam.upskill.service.UserService;
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.Timer;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
//
//@Api(tags = "Login")
//@RequiredArgsConstructor
//@RestController
//@Validated
//
//@RequestMapping("/api/v1")
//public class LoginController {
//  @Autowired
//  private Counter customRequestsCounter;
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//  @Autowired
//  private Timer customRequestLatencyTimer;
//
//  private final UserService userService;
//
//
//  @ResponseStatus(HttpStatus.OK)
//  @GetMapping("/login")
//  @ApiOperation("Authenticate a user")
//  public void login(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
//                    @RequestParam("password") @NotBlank @Size(min = 10, max = 10) String password) {
//    Timer.Sample sample = Timer.start();
//    customRequestsCounter.increment();
//    var user = userService.findByUsername(username);
//    sample.stop(customRequestLatencyTimer);
//    if (!passwordEncoder.matches(password, user.getPassword())) {
//      throw new AuthenticationException(", reason: wrong password");
//    }
//  }
//
////  @GetMapping("/logout")
////  @ResponseStatus(HttpStatus.OK)
////  @ApiOperation("Logout")
////  public String logout(HttpServletRequest request, HttpServletResponse response) {
////    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////    if (auth != null) {
////      new SecurityContextLogoutHandler().logout(request, response, auth);
////    }
////    return "redirect:/login?logout";
////  }
//}
