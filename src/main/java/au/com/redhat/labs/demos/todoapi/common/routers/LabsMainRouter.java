package au.com.redhat.labs.demos.todoapi.common.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

/**
 * @author faisalmasood fmasood@redhat.com
 */
@Component
public class LabsMainRouter {

    @Autowired
    ApiRouter apiRouter;

    public RouterFunction<?> doRoute(final ErrorHandler errorHandler) {
        //TODO - in future we could add more
        return apiRouter.doRoute(errorHandler);
    }


}
