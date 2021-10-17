package com.flower.external.quotes.response;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContentResponse {
    private final List<QuoteResponse> quotes;

    @JsonCreator
    public ContentResponse(@JsonProperty("quotes") final List<QuoteResponse> quotes) {
        this.quotes = quotes;
    }

    public List<QuoteResponse> getQuotes() {
        return quotes;
    }
}