package com.flower.external.quotes;

import com.flower.external.quotes.response.ApiResponse;
import com.flower.external.quotes.response.ContentResponse;
import com.flower.external.quotes.response.QuoteResponse;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;

import static org.junit.Assert.assertEquals;

public class QuoteManagerImplTest {

    @Mock
    final WebClient.ResponseSpec responseSpec = EasyMock.createMock(WebClient.ResponseSpec.class);

    @Mock
    final WebClient.UriSpec spec = EasyMock.createMock(WebClient.UriSpec.class);

    @Mock
    final WebClient.RequestHeadersUriSpec requestHeadersUriSpec = EasyMock.createMock(WebClient.RequestHeadersUriSpec.class);

    @Mock
    final WebClient apiClient = new WebClient() {
        @Override
        public RequestHeadersUriSpec<?> get() {
            return requestHeadersUriSpec;
        }

        @Override
        public RequestHeadersUriSpec<?> head() {
            return requestHeadersUriSpec;
        }

        @Override
        public RequestBodyUriSpec post() {
            return null;
        }

        @Override
        public RequestBodyUriSpec put() {
            return null;
        }

        @Override
        public RequestBodyUriSpec patch() {
            return null;
        }

        @Override
        public RequestHeadersUriSpec<?> delete() {
            return null;
        }

        @Override
        public RequestHeadersUriSpec<?> options() {
            return null;
        }

        @Override
        public RequestBodyUriSpec method(HttpMethod httpMethod) {
            return null;
        }

        @Override
        public Builder mutate() {
            return null;
        }
    };


    @TestSubject
    QuoteManager quoteManager = new QuoteManagerImpl(apiClient);

    @Test
    void test_quoteOfTheDay() {
        final String quote = "TEST QUOTE";
        final QuoteResponse quoteResponse = new QuoteResponse("author", quote, "test", "test", "test", 1);
        final ApiResponse apiResponse = new ApiResponse(
                "success",
                new ContentResponse(List.of(quoteResponse)),
                "test",
                "test"
        );

        EasyMock.expect(requestHeadersUriSpec.uri(EasyMock.anyString())).andReturn(requestHeadersUriSpec);
        EasyMock.expect(requestHeadersUriSpec.retrieve()).andReturn(responseSpec);
        EasyMock.expect(responseSpec.bodyToMono(ApiResponse.class)).andReturn(Mono.just(apiResponse));

        EasyMock.replay(responseSpec, spec, requestHeadersUriSpec);

        assertEquals(quoteResponse.getQuote(),  quoteManager.quoteOfTheDay());

        EasyMock.verify(responseSpec, spec, requestHeadersUriSpec);
    }
}
