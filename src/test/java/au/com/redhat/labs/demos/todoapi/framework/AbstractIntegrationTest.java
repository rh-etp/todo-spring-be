package au.com.redhat.labs.demos.todoapi.framework;


import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class AbstractIntegrationTest {
    @Autowired
    private WebTestClient client;



    private static MongodProcess process;

    @BeforeAll
    public static void init() throws IOException {
        Command command = Command.MongoD;

        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
                .defaults(command)
                .artifactStore(new ExtractedArtifactStoreBuilder()
                        .defaults(command)
                        .download(new DownloadConfigBuilder()
                                .defaultsForCommand(command)
                        ))

                .build();

        MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
        MongodExecutable executable = starter.prepare(new MongodConfigBuilder()

                .version(Version.Main.V3_2)

                .net(new Net(27017, Network.localhostIsIPv6()))
                .build());
        process = executable.start();
    }


    protected <T> T get(final Function<UriBuilder, URI> uriBuilder, final HttpStatus httpStatus,
                        final Consumer<HttpHeaders> httpHeaders, final Class<T> returnType) {
        return client.get()
                .uri(uriBuilder)
                .headers(httpHeaders)
                .accept(APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(APPLICATION_JSON_UTF8)
                .expectBody(returnType)
                .returnResult().getResponseBody();
    }


    protected <T, K> T post(final Function<UriBuilder, URI> uriBuilder, final HttpStatus httpStatus, final K requestBody,
                            final Consumer<HttpHeaders> httpHeaders,
                            final Class<T> returnType) {
        return client.post()
                .uri(uriBuilder)
                .headers(httpHeaders)
                .body(BodyInserters.fromObject(requestBody))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(returnType)
                .returnResult().getResponseBody();
    }


    protected WebTestClient getClient() {
        return client;
    }


}
