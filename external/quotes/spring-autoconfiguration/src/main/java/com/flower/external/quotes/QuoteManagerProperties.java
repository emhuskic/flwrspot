package com.flower.external.quotes;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("quotes")
public class QuoteManagerProperties {
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(final String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
