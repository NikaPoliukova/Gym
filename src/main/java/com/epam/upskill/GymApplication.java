package com.epam.upskill;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class GymApplication {

  public static void main(String[] args) {
  SpringApplication.run(GymApplication.class, args);
// ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
//
//    String[] beanNames = context.getBeanDefinitionNames();
//    for (String beanName : beanNames) {
//      System.out.println("Bean Name: " + beanName);
//    }
  }
}
