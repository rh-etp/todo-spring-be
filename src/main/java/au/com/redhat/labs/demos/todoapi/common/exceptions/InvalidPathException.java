package au.com.redhat.labs.demos.todoapi.common.exceptions;


/**
 *
 * @author faisalmasood fmasood@redhat.com
 */

public class InvalidPathException extends Exception {

    public InvalidPathException(final String message) {
        super(message);
    }
}
