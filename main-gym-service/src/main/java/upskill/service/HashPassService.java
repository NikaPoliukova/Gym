package upskill.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class HashPassService {

  private final BCrypt.Hasher hasher;

  public HashPassService(@Value("${secret}") String secret) {
    hasher = BCrypt.with(new SecureRandom(secret.getBytes()));
  }

  public boolean verify(String password, String hashPass) {
    final BCrypt.Result verify = BCrypt.verifyer().verify(password.getBytes(), hashPass.getBytes());
    return verify.verified;
  }

  public String hashPass(String password) {
    return hasher.hashToString(10, password.toCharArray());
  }
//
//  private final String secret;
//
//  public HashPassService(@Value("${secret}") String secret) {
//    this.secret = secret;
//  }
//
//  public boolean verify(String password, String hashPass) {
//    final BCrypt.Result verify = BCrypt.verifyer().verify(password.getBytes(), hashPass.getBytes());
//    return verify.verified;
//  }
//
//  public String hashPass(String password) {
//    BCrypt.Hasher hasher = BCrypt.with(new SecureRandom(secret.getBytes()));
//    return hasher.hashToString(10, password.toCharArray());
//  }
}