package com.liga.semin.tgclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication(exclude = DataSource.class)
public class TgClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgClientApplication.class, args);
	}

}
