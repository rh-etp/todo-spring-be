package au.com.redhat.labs.demos.todoapi.common.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A generic reactor {@link reactor.util.context.Context} that will be used mainly to achieve
 * stuff  {@link org.slf4j.MDC} like behaviour.
 *
 * @see LabsServerFilter
 *
 * @author faisalmasood fmasood@redhat.com
 */


public final class LabsReactiveContext {

    private static final String LABS_TRACE_ID = "trace_id";

    private final Map<String, String> labsMDCMap = new HashMap<>(5);


    public LabsReactiveContext(String traceId) {
        labsMDCMap.put(LABS_TRACE_ID, traceId);
    }

    public Map<String, String> getLabsMDCMap(){
        return Collections.unmodifiableMap(labsMDCMap);
    }

    public void populateResponseHeaders(HttpHeaders headers) {
        headers.add(LABS_TRACE_ID, labsMDCMap.get(LABS_TRACE_ID));
    }


}
