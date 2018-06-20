package au.com.redhat.labs.demos.todoapi.common.filters;

/**
 * This holds the event name that would be used in {@link LabsServerFilter} and {@link au.com.redhat.labs.demos.todoapi.common.client.LabsWebClient}
 * to record http events.
 */
public final class BoundaryEvents {

    private BoundaryEvents(){

    }

    public static final String SERVER_SENT = "ServerSent";
    public static final String SERVER_RECEIVED = "ServerReceived";
    public static final String CLIENT_SENT = "ClientSent";
    public static final String CLIENT_RECEIVED = "ClientReceived";


}
