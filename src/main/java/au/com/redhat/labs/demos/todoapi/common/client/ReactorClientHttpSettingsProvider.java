package au.com.redhat.labs.demos.todoapi.common.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.util.concurrent.TimeUnit;

/**
 * This class provides a utility to set the client settings for {@link LabsWebClient} for each service
 *
 * @author fmasood fmasood@redhat.com
 */

public class ReactorClientHttpSettingsProvider {

    private ReactorClientHttpSettingsProvider(){

    }

    public static ClientHttpConnector createNettyClientHttpConnector(final int connectTimeOut, final int readTimeOut){
        return new ReactorClientHttpConnector(
                options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut)
                        .compression(true)
                        .afterNettyContextInit(ctx -> ctx.addHandlerLast(new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS))
                        ));

    }
}
