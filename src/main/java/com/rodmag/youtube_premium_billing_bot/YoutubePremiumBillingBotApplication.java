package com.rodmag.youtube_premium_billing_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@SpringBootApplication
public class YoutubePremiumBillingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoutubePremiumBillingBotApplication.class, args);
	}

}
