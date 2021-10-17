package com.flower.external.quotes.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponse {
    private final String author;
    private final String quote;
    private final Object tags;
    private final String id;
    private final String image;
    private final int length;

    @JsonCreator
    public QuoteResponse(@JsonProperty("author") final String author,
                         @JsonProperty("quote") final String quote,
                         @JsonProperty("tags") final Object tags,
                         @JsonProperty("id") final String id,
                         @JsonProperty("image") final String image,
                         @JsonProperty("length") final int length) {
        this.author = author;
        this.quote = quote;
        this.tags = tags;
        this.id = id;
        this.image = image;
        this.length = length;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }

    public Object getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getLength() {
        return length;
    }
}