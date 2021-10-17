package com.flower.external.quotes;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EntityScan("com.flower.external.quotes")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(QuoteManagerProperties.class)
public class QuoteManagerAutoConfiguration {

    @Bean
    @ConditionalOnClass(QuoteManagerImpl.class)
    public WebClient quotesApiClient(final QuoteManagerProperties properties) {
        return WebClient.create(properties.getApiUrl());
    }

    @Bean
    @ConditionalOnMissingBean(QuoteManager.class)
    @ConditionalOnClass(QuoteManagerImpl.class)
    public QuoteManagerImpl quoteManager(final WebClient webClient) {
        return new QuoteManagerImpl(webClient);
    }
}
