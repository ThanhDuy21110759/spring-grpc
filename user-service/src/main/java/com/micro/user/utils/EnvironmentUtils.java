package com.micro.user.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.yml")
public class EnvironmentUtils implements EnvironmentAware {
  private static Environment env;

  static {
    System.setProperty("PORT", dotenv().get("PORT"));
    System.setProperty("CONTEXT_PATH", dotenv().get("CONTEXT_PATH"));
    System.setProperty("GRPC_PORT", dotenv().get("GRPC_PORT"));
  }

  public static String getEnvironmentValue(String propertyKey) {
    return env.getProperty(propertyKey);
  }

  public static Dotenv dotenv() {
    return Dotenv.configure()
        .directory("./")
        .filename(".env")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();
  }

  @Override
  public void setEnvironment(Environment environment) {
    env = environment;
  }
}
