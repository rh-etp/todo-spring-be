package au.com.redhat.labs.demos.todoapi.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * We are using Spring security
 *
 * @author faisalmasood fmasood@redhat.com
 */

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Component
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.httpBasic().disable();
        serverHttpSecurity.formLogin().disable();
        serverHttpSecurity.csrf().disable();
        serverHttpSecurity.logout().disable();
        serverHttpSecurity.authenticationManager(reactiveAuthenticationManager);
        serverHttpSecurity.securityContextRepository(serverSecurityContextRepository);
        serverHttpSecurity.authorizeExchange().pathMatchers(HEALTH_ENDPOINT).permitAll();
        serverHttpSecurity.authorizeExchange().pathMatchers(PROMETHEUS_ENDPOINT).permitAll();
        serverHttpSecurity.authorizeExchange().anyExchange().access(this::authoriseRequest);
        return serverHttpSecurity.build();
    }





    @Autowired
    ServiceAuthorisor serviceAuthorisor;


    private Mono<AuthorizationDecision> authoriseRequest(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {

        return authentication
                .flatMap(auth -> serviceAuthorisor.authoriseRequest(authorizationContext))
                .map(AuthorizationDecision::new);
    }


    ReactiveAuthenticationManager reactiveAuthenticationManager = authentication -> {
        authentication.setAuthenticated(true);
        return Mono.just(authentication);


    };

    ServerSecurityContextRepository serverSecurityContextRepository = new ServerSecurityContextRepository() {
        @Override
        public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
            return null;
        }

        @Override
        public Mono<SecurityContext> load(ServerWebExchange exchange) {
            Authentication authentication = new AbstractAuthenticationToken(null) {

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }
            };
            return Mono.just(new SecurityContextImpl(authentication));
        }
    };


    private static final String PROMETHEUS_ENDPOINT = "/prometheus";
    private static final String HEALTH_ENDPOINT = "/health";
}