package au.com.redhat.labs.demos.todoapi.common.logger;

import au.com.redhat.labs.demos.todoapi.common.filters.LabsReactiveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.function.Consumer;

/**
 * This class is responsible for logging with MDC data from signal
 *
 * @author faisalmasood fmasood@redhat.com
 */

//TODO - add events like server sent etc
public final class LabsLogger {
    private LabsLogger() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LabsLogger.class);

    /**
     * Will not record {@link Signal#isOnComplete()} or {@link Signal#isOnError()}
     *
     * @param signal
     * @param logStatemet
     * @param <T>
     */
    public static <T> void logOnEach(Signal<T> signal, Consumer<T> logStatemet) {
        if (!signal.isOnNext()) return;

        //TODO - replace it with getOrEmpty
        LabsReactiveContext labsReactiveContext = signal.getContext().get(LabsReactiveContext.class);
        try {
            if (labsReactiveContext != null) labsReactiveContext.getLabsMDCMap().forEach(MDC::put);
            logStatemet.accept(signal.get());
        } finally {
            MDC.clear();
        }
    }

    public static void log(LabsReactiveContext labsReactiveContext, Runnable logStatement) {
        log(labsReactiveContext, null, logStatement);
    }

    // Logging client and server events
    public static void log(LabsReactiveContext labsReactiveContext, String logEventName, Runnable logStatement) {
        if (logEventName != null) MDC.put(LOG_EVENT, logEventName);

        try {
            if (labsReactiveContext != null) labsReactiveContext.getLabsMDCMap().forEach(MDC::put);
            logStatement.run();
        } finally {
            MDC.clear();
        }

    }

    public static void log(Context context, Runnable logConsumer) {
        log((context.isEmpty() ? null : context.get(LabsReactiveContext.class)), logConsumer);
    }

    private static final String LOG_EVENT = "LOG_EVENT";


}