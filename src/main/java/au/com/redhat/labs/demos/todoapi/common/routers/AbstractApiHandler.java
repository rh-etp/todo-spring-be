package au.com.redhat.labs.demos.todoapi.common.routers;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author faisalmasood fmasood@redhat.com
 */

public abstract class AbstractApiHandler {

    @Autowired
    protected ErrorHandler errorHandler;

}
