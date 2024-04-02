package com.liga.semin.tgclient.bot_config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
@Setter
@ComponentScan
public class BotConfig {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.webhoot}")
    private String webhook;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
