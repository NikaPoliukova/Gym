package com.epam.upskill.logger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
      logger.info(String.format("Transaction ID: %s | Operation completed with result = %s", transactionId, myResult));
      transactionIdThreadLocal.remove();
    }
  }

  public String getTransactionId() {
    return transactionIdThreadLocal.get();
  }
}

