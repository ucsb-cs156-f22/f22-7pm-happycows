package edu.ucsb.cs156.happiercows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.context.annotation.Bean;
import java.time.ZonedDateTime;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "utcDateTimeProvider")
@EnableAsync
public class HappierCowsApplication {

  public static void main(String[] args) {
    SpringApplication.run(HappierCowsApplication.class, args);
  }

  @Bean
  public DateTimeProvider utcDateTimeProvider() {
      return () -> {
        ZonedDateTime now = ZonedDateTime.now();
        return Optional.of(now);
      };
  }

}
