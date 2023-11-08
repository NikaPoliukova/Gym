package com.epam.upskill.logger;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OperationLoggerAspect {
  private static final Logger logger = LoggerFactory.getLogger("com.epam.upskill.logger");

  @Before("execution(* com.epam.upskill.service.impl.*.*(..)) && args(.., transactionId)")
  public void logOperationStart(String transactionId) {
    logger.info("Transaction ID: {} | Operation started", transactionId);
  }

  @AfterReturning(pointcut = "execution(* com.epam.upskill.service.impl.*.*(..)) && args(.., transactionId)", returning = "result")
  public void logOperationSuccess(String transactionId, Object result) {
    logger.info("Transaction ID: {} | Operation succeeded with result: {}", transactionId, result);
  }

  @AfterThrowing(pointcut = "execution(* com.epam.upskill.service.impl.*.*(..)) && args(.., transactionId)", throwing = "exception")
  public void logOperationError(String transactionId, Exception exception) {
    logger.error("Transaction ID: {} | Operation failed with exception: {}", transactionId, exception.getMessage());
  }
}