package au.com.redhat.labs.demos.todoapi.common.security.impl;

import au.com.redhat.labs.demos.todoapi.common.security.ServiceAuthorisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author faisalmasood fmasood@redhat.com
 */

@Component
public class DefaultServiceAuthorisor implements ServiceAuthorisor {


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServiceAuthorisor.class);


    public Mono<Boolean> authoriseRequest(AuthorizationContext authorizationContext) {

        return Mono.just(Boolean.TRUE);

    }


}
