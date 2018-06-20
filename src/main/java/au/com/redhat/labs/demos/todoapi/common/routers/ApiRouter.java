package au.com.redhat.labs.demos.todoapi.common.routers;

import org.springframework.web.reactive.function.server.RouterFunction;

/**
 *
 * @author faisalmasood fmasood@redhat.com
 */

public interface ApiRouter {

    RouterFunction<?> doRoute(final ErrorHandler errorHandler);
}
