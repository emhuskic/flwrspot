package com.flower.external.quotes.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {
    private final Object success;
    private final ContentResponse contents;
    private final String baseurl;
    private final Object copyright;

    @JsonCreator
    public ApiResponse(@JsonProperty("success") final Object success,
                       @JsonProperty("contents") final ContentResponse contents,
                       @JsonProperty("baseurl") final String baseurl,
                       @JsonProperty("copyright") final Object copyright) {
        this.success = success;
        this.contents = contents;
        this.copyright = copyright;
        this.baseurl = baseurl;
    }

    public Object getSuccess() {
        return success;
    }

    public ContentResponse getContents() {
        return contents;
    }
}

