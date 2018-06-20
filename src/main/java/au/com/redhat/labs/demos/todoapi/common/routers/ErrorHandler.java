package au.com.redhat.labs.demos.todoapi.common.routers;


import au.com.redhat.labs.demos.todoapi.common.exceptions.InvalidPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * @author faisalmasood fmasood@redhat.com
 */

public class ErrorHandler {

    private static final String ERROR_OCCURRED = "Error occured";
    private static final String NOT_FOUND = "No API Available at this location";
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    public Mono<ServerResponse> invalidPath(final ServerRequest request) {
        return Mono.just(new InvalidPathException(NOT_FOUND)).transform(this::getResponse);
    }

    public Mono<ServerResponse> throwableError(final Throwable error) {
        LOGGER.error(ERROR_OCCURRED, error);
        return Mono.just(error).transform(this::getResponse);
    }

    private <T extends Throwable> Mono<ServerResponse> getResponse(final Mono<T> monoError) {
        return monoError.transform(ThrowableConverter::translate)
                .flatMap(translation -> ServerResponse
                        .status(translation.getHttpStatus())
                        .body(Mono.just(new ErrorResponse(translation.getMessage() + "*** Some Custom Stuff ****")), ErrorResponse.class));
    }
}