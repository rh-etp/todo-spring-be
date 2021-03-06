package au.com.redhat.labs.demos.todoapi.service;


import au.com.redhat.labs.demos.todoapi.api.ToDoListRouter;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LabsReactiveApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DisplayName("Integartion test for ToDo")
public class ToDoListServiceTest extends AbstractIntegrationTest {


    @LocalServerPort
    private int localServerPort;


    @Autowired
    MongoOperations mongoOperations;



    @BeforeEach
    public void setUp() {

        mongoOperations.dropCollection(ToDoTask.class);
        mongoOperations.insert(new ToDoTask("1", "Test", false));
    }


    @Test
    void postToDoTest() {


        ToDoTask toDoTask = new ToDoTask("10", "Masood", false);

        final ToDoTask response = post(
                builder -> builder.host("localhost").port(localServerPort).path(ToDoListRouter.BASE_PATH + ToDoListRouter.TODO_LIST_API_ENDPOINT).build(),
                HttpStatus.OK,
                toDoTask,
                httpHeaders -> {


                },
                ToDoTask.class);

        assertEquals(response, toDoTask);


    }


}