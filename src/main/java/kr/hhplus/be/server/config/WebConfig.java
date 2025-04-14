package kr.hhplus.be.server.config;

import kr.hhplus.be.server.queues.application.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final TokenInterceptor tokenInterceptor;

  public WebConfig(TokenInterceptor tokenInterceptor) {
    this.tokenInterceptor = tokenInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry interceptorRegistry){
    interceptorRegistry.addInterceptor(tokenInterceptor)
        .addPathPatterns("/api/v1/reservations/**", "/api/v1/seats/**", "/api/v1/points/**");
  }
}
