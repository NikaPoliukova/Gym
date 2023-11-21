//package com.epam.upskill.util;
//
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class BruteForceProtectionService {
//
//  private Map<String, Integer> loginAttempts = new HashMap<>();
//
//  public void registerLoginFailure(String username) {
//    loginAttempts.put(username, loginAttempts.getOrDefault(username, 0) + 1);
//  }
//
//  public void resetLoginAttempts(String username) {
//    loginAttempts.remove(username);
//  }
//
//  public int getLoginAttempts(String username) {
//    return loginAttempts.getOrDefault(username, 0);
//  }
//}
