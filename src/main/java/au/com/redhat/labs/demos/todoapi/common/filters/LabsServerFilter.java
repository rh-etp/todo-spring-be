package au.com.redhat.labs.demos.todoapi.common.filters;


import au.com.redhat.labs.demos.todoapi.common.logger.LabsLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.net.URI;

/**
 * A simple {@link WebFilter} that tracks the in/out of call.
 *
 * @author faisalmasood fmasood@redhat.com
 */

@Order(Ordered.LOWEST_PRECEDENCE - 200)
@Component
public class LabsServerFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabsServerFilter.class);


    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        org.springframework.http.server.reactive.ServerHttpRequest request = serverWebExchange.getRequest();
        String path = getRequestPath(request.getURI());


        LabsReactiveContext labsReactiveContext = new LabsReactiveContext(request.getHeaders());
        labsReactiveContext.populateResponseHeaders(serverWebExchange.getResponse().getHeaders());
        LabsLogger.log(labsReactiveContext, BoundaryEvents.SERVER_RECEIVED, () -> LOGGER.info("Received request for {}", path));
        serverWebExchange.getAttributes().put(LabsReactiveContext.class.getName(), labsReactiveContext);
        return webFilterChain.filter(serverWebExchange)
                .subscriberContext(Context.of(LabsReactiveContext.class, labsReactiveContext))
                .doAfterSuccessOrError((r, t) -> LabsLogger.log(labsReactiveContext, BoundaryEvents.SERVER_SENT, () -> LOGGER.info("Sent response for url{} and responsecode {}",
                        path, getHttpStatusCode(serverWebExchange))));
    }

    private String getRequestPath(URI uri) {
        return uri.getQuery() == null ? uri.getPath() : uri.getPath() + "?" + uri.getQuery();
    }

    private int getHttpStatusCode(ServerWebExchange exchange) {
        HttpStatus status = exchange.getResponse().getStatusCode();
        return status == null ? 0 : status.value();
    }

}