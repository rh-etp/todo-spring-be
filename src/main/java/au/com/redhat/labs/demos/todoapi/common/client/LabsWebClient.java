package au.com.redhat.labs.demos.todoapi.common.client;

import au.com.redhat.labs.demos.todoapi.common.filters.BoundaryEvents;
import au.com.redhat.labs.demos.todoapi.common.filters.LabsReactiveContext;
import au.com.redhat.labs.demos.todoapi.common.logger.LabsLogger;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * An extension of {@link WebClient} that adds server {@link reactor.util.context.Context} to the downstream call and further
 * configure timeouts for the call.
 *
 * @author faisalmasood fmasood@redhat.com
 */

@Configuration
public class LabsWebClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabsWebClient.class);

    @Bean
    WebClient createLabsWebClient() {
        return WebClient.builder()
                .filter(logRequest())
                .clientConnector(connector)
                .build();
    }


    public ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> Mono.subscriberContext()
                .flatMap(context -> {
                    Optional<LabsReactiveContext> labsReactiveContextOptional = context.getOrEmpty(LabsReactiveContext.class);
                    LabsReactiveContext labsReactiveContext = labsReactiveContextOptional.orElse(new LabsReactiveContext(clientRequest.headers()));
                    LabsLogger.log(labsReactiveContext, BoundaryEvents.CLIENT_SENT, () -> LOGGER.info("Calling Downstream system {}", clientRequest.url().toString()));
                    //can add some timing data here, if needed.
                    return next.exchange(clientRequest)
                            .doAfterSuccessOrError((clientResponse, throwable) -> {
                                if (clientResponse != null) {

                                    LabsLogger.log(labsReactiveContext, BoundaryEvents.CLIENT_RECEIVED, () -> LOGGER.info("Got Response from Downstream {}, {}", clientRequest.method(), clientRequest.url().toString()));
                                } else {
                                    LabsLogger.log(labsReactiveContext, BoundaryEvents.CLIENT_RECEIVED, () -> LOGGER.info("Got Error from Downstream {}, {}", clientRequest.method(), clientRequest.url().toString(), throwable));
                                }
                            });
                });


    }


    // Default settings
    ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
            options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                    .compression(true)
                    .afterNettyContextInit(ctx -> ctx.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    ));
}
