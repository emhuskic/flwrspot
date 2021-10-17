package com.flower.external.quotes;

import com.flower.external.quotes.response.ApiResponse;
import com.flower.external.quotes.response.QuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class QuoteManagerImpl extends QuoteManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteManagerImpl.class);
    private final WebClient apiClient;

    public QuoteManagerImpl(final WebClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public String quoteOfTheDay() {
        try {
            LOGGER.debug("Obtaining quote of the day...");

            final ApiResponse apiResponse = this.apiClient.get().uri("/qod")
                    .retrieve().bodyToMono(ApiResponse.class).block();

            if (apiResponse != null)  {
                return apiResponse
                        .getContents()
                        .getQuotes()
                        .stream()
                        .findAny()
                        .map(QuoteResponse::getQuote).orElse(null);
            }
        } catch (Exception ex) {
            LOGGER.error("Could not obtain Quote of the day. Reason: {}", ex.getMessage());
        }
        return null;
    }
}
