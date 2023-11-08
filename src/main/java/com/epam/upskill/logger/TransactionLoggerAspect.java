package com.epam.upskill.logger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggerAspect {
  private static final Logger logger = LoggerFactory.getLogger("com.example.transaction");

  @Before("execution(* com.example.service.*.*(..)) && args(.., transactionId)")
  public void logTransactionStart(String transactionId) {
    logger.info("Transaction ID: {} | Transaction started", transactionId);
  }
}