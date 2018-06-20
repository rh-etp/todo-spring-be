package au.com.redhat.labs.demos.todoapi.common.logger;

import org.jboss.logging.MDC;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * This thread pool is intended to run the supplied logic in a separate {@link Thread}.
 * It is strictly being used for the masked logging operation and is not intended to use for anything else.
 *
 * Be aware that, we run this code inside a hardware constrainted pod and additional thread handling would prove to be
 * problematic, if used in a non-efficient manner.
 *
 *
 * This class follows Singleton pattern to avoid multiple pool creations.
 *
 * Most of the stuff is hard coded with minimal settings. Using {@link ThreadPoolExecutor.CallerRunsPolicy}.
 * Donot use this class other than the aysnc logging of payload. (which is done already)
 *
 *
 * @author fmasood2 fmasood@redhat.com
 *
 */
class AsyncJobThreadPool {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AsyncJobThreadPool.class);

    public void runJob(Supplier<String> logFunction, org.slf4j.Logger logger){
        fixedThreadPool.submit(new LogAsyncRunner(logFunction, logger, MDC.getMap()));
    }


    private static class LogAsyncRunner implements Runnable{

        private final org.slf4j.Logger logger;
        private final Supplier<String> logFunction;
        private final Map<String, Object> mdcMap;
        public LogAsyncRunner(final Supplier<String> logFunction,
                              final org.slf4j.Logger logger,
                              final Map<String, Object> mdcMap){
            this.logFunction = logFunction;
            this.logger = logger;
            this.mdcMap = mdcMap;
        }

        @Override
        public void run() {
            MDC.clear();
            try {
                mdcMap.entrySet().stream().forEach(stringObjectEntry -> MDC.put(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
                String resultToLog = logFunction.get();
                logger.info(resultToLog);
            }catch(Throwable throwable){
                LOGGER.error("Unable to apply logging function", throwable);
            }
            MDC.clear();
        }
    }


    private static AsyncJobThreadPool ASYNC_JOB_THREAD_POOOL = new AsyncJobThreadPool();

    public static AsyncJobThreadPool getInstance(){
        return ASYNC_JOB_THREAD_POOOL;
    }


    BlockingQueue<Runnable> blockingRequestQueue = new ArrayBlockingQueue<>(MAX_QUEUE_DEPTH);
    ExecutorService fixedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE, THREADS_KEEP_ALIVE_TIME_IN_SECONDS,
            TimeUnit.SECONDS, blockingRequestQueue, new ThreadPoolExecutor.CallerRunsPolicy());
    //no one can instantiate this class
    private AsyncJobThreadPool(){

    }

    @Override
    protected void finalize(){
        fixedThreadPool.shutdown();
    }

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 5;
    private static final long THREADS_KEEP_ALIVE_TIME_IN_SECONDS = 1;
    private static final int MAX_QUEUE_DEPTH = 10;
}
