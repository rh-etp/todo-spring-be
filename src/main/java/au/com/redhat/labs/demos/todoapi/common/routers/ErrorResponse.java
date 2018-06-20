package au.com.redhat.labs.demos.todoapi.common.routers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author faisalmasood fmasood@redhat.com
 */

public class ErrorResponse {

    private final String error;

    @JsonCreator
    public ErrorResponse(@JsonProperty("error") final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}