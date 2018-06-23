package au.com.redhat.labs.demos.todoapi.framework.logging;

import au.com.redhat.labs.demos.todoapi.common.filters.LabsReactiveContext;
import au.com.redhat.labs.demos.todoapi.common.logger.LabsLogger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static org.mockito.Mockito.*;


/**
 * @author fmasood fmasood@redhat.com
 */
public class LabsLoggerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabsLoggerTest.class);

    @Test
    public void testLabsLogger() {
        //add logger to a mock
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("LABSMOCK");
        root.addAppender(mockAppender);

        HttpHeaders headers = new HttpHeaders();
        String traceHeaderValue = UUID.randomUUID().toString();
        headers.add(TRACE_ID, traceHeaderValue);

        LabsReactiveContext labsReactiveContext = new LabsReactiveContext(traceHeaderValue);

        LabsLogger.log(labsReactiveContext, () -> LOGGER.info("Received request for {}"));

        //verify MDC.clear
        Assert.assertNull(MDC.getCopyOfContextMap());

        verify(mockAppender).doAppend(argThat((ArgumentMatcher) argument -> ((LoggingEvent) argument).getMDCPropertyMap().size() == 1));


    }


    @Test
    public void testLabsLoggerWithServerEvents() {
        //add logger to a mock
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("LABSMOCK");
        root.addAppender(mockAppender);

        HttpHeaders headers = new HttpHeaders();
        String traceHeaderValue = UUID.randomUUID().toString();
        headers.add(TRACE_ID, traceHeaderValue);

        LabsReactiveContext labsReactiveContext = new LabsReactiveContext(traceHeaderValue);

        //verify log events

        LabsLogger.log(labsReactiveContext, SERVER_SENT, () -> LOGGER.info("Message sent to "));

        verify(mockAppender).doAppend(argThat((ArgumentMatcher) argument -> {
            return ((LoggingEvent) argument).getMDCPropertyMap().size() == 2;
        }));


    }


    private static final String TRACE_ID = "trace_id";
    private static final String SERVER_SENT = "ServerSent";

}
