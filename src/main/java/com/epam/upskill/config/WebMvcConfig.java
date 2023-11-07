package com.epam.upskill.config;

import com.epam.upskill.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

  private final AuthInterceptor myInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(myInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/createTrainee", "/createTrainer", "/swagger-ui.html",
            "/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/webjars/**", "/swagger-ui/index.html#/");
  }
}

