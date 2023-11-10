package com.epam.upskill.logger;

import com.epam.upskill.exception.OperationFailedException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class OperationLoggerAspect {
  private static final Logger logger = LoggerFactory.getLogger("com.epam.upskill.logger");
  private final ThreadLocal<String> transactionIdThreadLocal = new ThreadLocal<>();
  Object myResult;

  @SneakyThrows
  @Around("execution(* com.epam.upskill.service.impl.*.*(..))")
  public Object logOperationStart(ProceedingJoinPoint aspect) {
    String name = aspect.getSignature().getName();
    log.debug("generate transactionId  for operation: " + name);
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);
    transactionIdThreadLocal.set(transactionId);
    try {
      myResult = aspect.proceed();
      return myResult;
    } finally {
      logger.info("Transaction ID: " + transactionId + " | Operation completed with result = " + myResult);
      transactionIdThreadLocal.remove();
    }
  }

  public String getTransactionId() {
    return transactionIdThreadLocal.get();
  }
}

