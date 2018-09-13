package com.chinasoft.goldidea;

import com.chinasoft.goldidea.common.HttpsClientRequestFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class GoldenIdeaSerivceApplication {
	@Bean
	public RestTemplate httpsRestTemplate() {
		RestTemplate restClient =  new RestTemplate (new HttpsClientRequestFactory ());
		return restClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(GoldenIdeaSerivceApplication.class, args);
	}
}
