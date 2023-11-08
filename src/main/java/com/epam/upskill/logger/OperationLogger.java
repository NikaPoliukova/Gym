package com.epam.upskill.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OperationLogger {
  private static final Logger logger = LoggerFactory.getLogger("com.epam.upskill.logger");

  public void logOperation(String transactionId, String operationName, String message) {
    logger.trace("Transaction ID: {} | Operation: {} | {}", transactionId, operationName, message);
  }
}