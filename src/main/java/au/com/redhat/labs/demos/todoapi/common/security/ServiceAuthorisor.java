package au.com.redhat.labs.demos.todoapi.common.security;

import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * This is an interface which is used by {@link SecurityConfiguration#authoriseRequest(Mono, AuthorizationContext)} to authorise calls.
 * Users can provide custom implementation, if needed.
 *
 * @author fmasood fmasood@redhat.com
 */
public interface ServiceAuthorisor {

    /**
     * Returns an authorization decision
     * @param authorizationContext
     * @return
     */
     Mono<Boolean> authoriseRequest(AuthorizationContext authorizationContext);

}
