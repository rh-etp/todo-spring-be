package au.com.redhat.labs.demos.todoapi.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * A global error handler
 *
 * @author faisalmasood fmasood@redhat.com
 */

@Component
@Order(-200)
class GlobalErrorHandler implements WebExceptionHandler {

    private static final Logger LOGGER  = LoggerFactory.getLogger(GlobalErrorHandler.class);


    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        if (throwable != null) {
            byte[] bytes = throwable.getMessage().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);

            if (throwable instanceof LabsWebClientResponseException) {

                serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
            } else {
                LOGGER.error("Throwing Generic Error", throwable);
                serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_GATEWAY);
            }
        }
        return Mono.empty();
    }
}