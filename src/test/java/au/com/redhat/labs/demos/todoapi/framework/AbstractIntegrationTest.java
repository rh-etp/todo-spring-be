package au.com.redhat.labs.demos.todoapi.framework;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class AbstractIntegrationTest {
    @Autowired
    private WebTestClient client;

    protected <T> T get(final Function<UriBuilder, URI> uriBuilder, final HttpStatus httpStatus,
                        final Consumer<HttpHeaders> httpHeaders, final Class<T> returnType) {
        return client.get()
                .uri(uriBuilder)
                .headers(httpHeaders)
                .accept(APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(APPLICATION_JSON_UTF8)
                .expectBody(returnType)
                .returnResult().getResponseBody();
    }


    protected <T, K> T post(final Function<UriBuilder, URI> uriBuilder, final HttpStatus httpStatus, final K requestBody,
                            final Consumer<HttpHeaders> httpHeaders,
                            final Class<T> returnType) {
        return client.post()
                .uri(uriBuilder)
                .headers(httpHeaders)
                .body(BodyInserters.fromObject(requestBody))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(returnType)
                .returnResult().getResponseBody();
    }


    protected WebTestClient getClient() {
        return client;
    }


}
