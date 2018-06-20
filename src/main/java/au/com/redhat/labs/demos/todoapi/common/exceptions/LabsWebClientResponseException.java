package au.com.redhat.labs.demos.todoapi.common.exceptions;

import org.springframework.web.reactive.function.client.WebClientResponseException;


/**
 * Simple class where extension of custom response would be managed.
 *
 * @author faisalmasood fmasood@redhat.com
 */
public class LabsWebClientResponseException extends WebClientResponseException {
    /**
     * Construct a new instance of with the given response data.
     *
     * @param message
     * @param statusCode the raw status code value
     * @param statusText the status text
     */
    public LabsWebClientResponseException(String message, int statusCode, String statusText) {
        super(message, statusCode, statusText, null, null, null);
    }
}
