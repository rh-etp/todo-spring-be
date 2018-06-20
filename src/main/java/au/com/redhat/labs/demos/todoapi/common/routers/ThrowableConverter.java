package au.com.redhat.labs.demos.todoapi.common.routers;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;


/**
 * @author faisalmasood fmasood@redhat.com
 */


class ThrowableConverter {

    private final HttpStatus httpStatus;
    private final String message;

    private ThrowableConverter(final Throwable throwable) {
        this.httpStatus = getStatus(throwable);
        this.message = throwable.getMessage();
    }

    //TODO - add logic for multiple type of errors
    private HttpStatus getStatus(final Throwable error) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    HttpStatus getHttpStatus() {
        return httpStatus;
    }

    String getMessage() {
        return message;
    }

    static <T extends Throwable> Mono<ThrowableConverter> translate(final Mono<T> throwable) {
        return throwable.flatMap(error -> Mono.just(new ThrowableConverter(error)));
    }
}

