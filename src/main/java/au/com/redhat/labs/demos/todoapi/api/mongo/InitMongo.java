package au.com.redhat.labs.demos.todoapi.api.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;


@Component
public class InitMongo {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitMongo.class);

    @Bean
    CommandLineRunner init(MongoOperations mongoOperations) {
        return args -> {
            mongoOperations.dropCollection(ToDoTask.class);
            mongoOperations.insert(new ToDoTask("1", "Buy Grocerioes", true));
            mongoOperations.insert(new ToDoTask("2", "Pay Taxes", false));
            mongoOperations.findAll(ToDoTask.class).forEach(toDoTask -> LOGGER.info(toDoTask.toString())
            );

        };
    }


}
