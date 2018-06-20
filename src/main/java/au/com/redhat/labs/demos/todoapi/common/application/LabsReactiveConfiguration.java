package au.com.redhat.labs.demos.todoapi.common.application;

import au.com.redhat.labs.demos.todoapi.common.routers.ErrorHandler;
import au.com.redhat.labs.demos.todoapi.common.routers.LabsMainRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

/**
 * @author faisalmasood fmasood@redhat.com
 */

@Configuration
@EnableWebFlux
public class LabsReactiveConfiguration {

    @Autowired
    LabsMainRouter labsMainRouter;

    @Bean
    RouterFunction<?> mainRouterFunction(final ErrorHandler errorHandler) {
        return labsMainRouter.doRoute(errorHandler);
    }


    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandler();
    }


}
