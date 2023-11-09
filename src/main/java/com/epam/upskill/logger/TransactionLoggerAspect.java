package com.epam.upskill.logger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggerAspect {
  private static final Logger logger = LoggerFactory.getLogger("com.epam.upskill.logger");

  @Autowired
  private OperationLoggerAspect operationLoggerAspect;

  @Before("execution(* com.epam.upskill.service.impl.*.*(..)) ")
  public void logTransactionStart() {
    String transactionId = operationLoggerAspect.getTransactionId();
    logger.info("Transaction ID: {} | Transaction started", transactionId);
  }
}