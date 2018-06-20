package au.com.redhat.labs.demos.todoapi.mongo;

import au.com.redhat.labs.demos.todoapi.api.ToDoListRouter;
import au.com.redhat.labs.demos.todoapi.api.mongo.ToDoListMongoRepository;
import au.com.redhat.labs.demos.todoapi.api.mongo.ToDoTask;
import au.com.redhat.labs.demos.todoapi.common.application.LabsReactiveApplication;
import au.com.redhat.labs.demos.todoapi.framework.AbstractIntegrationTest;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LabsReactiveApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DisplayName("Mongo ToDo Item Test Test")
@AutoConfigureDataMongo

public class ToDoListMongoRepositoryIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    ToDoListMongoRepository toDoListMongoRepository;

    @Autowired
    MongoOperations mongoOperations;

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

    @BeforeEach
    public void setUp() {

        mongoOperations.dropCollection(ToDoTask.class);
        mongoOperations.insert(new ToDoTask("1", "Test", false));
    }


    @Test
    public void testFetchData() {
        final String todoList = get(
                builder -> builder.host("localhost").port(localServerPort).path(ToDoListRouter.BASE_PATH + ToDoListRouter.TODO_LIST_API_ENDPOINT).build(),
                HttpStatus.OK,
                httpHeaders -> {
                    httpHeaders.add("test", "testvalue");


                },
                String.class);
        assertEquals(todoList.contains("Test"), true);

    }
}
