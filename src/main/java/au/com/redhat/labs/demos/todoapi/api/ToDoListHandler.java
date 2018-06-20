package au.com.redhat.labs.demos.todoapi.api;


import au.com.redhat.labs.demos.todoapi.api.mongo.ToDoListMongoRepository;
import au.com.redhat.labs.demos.todoapi.api.mongo.ToDoTask;
import au.com.redhat.labs.demos.todoapi.common.routers.AbstractApiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ToDoListHandler extends AbstractApiHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToDoListHandler.class);


    @Autowired
    ToDoListMongoRepository toDoListMongoRepository;

    public Mono<ServerResponse> fetchAlToDoTasks(final ServerRequest request) {

        return ok().body(toDoListMongoRepository.findAll(), ToDoTask.class);

    }

    public Mono<ServerResponse> saveToDoTask(final ServerRequest request) {
        return request.bodyToMono(ToDoTask.class)
                .flatMap(personMongo -> toDoListMongoRepository.save(personMongo))
                .flatMap(this::serverResponse);

//        return ok().body(personMongoRepository.findAll(), ToDoTask.class);

    }


    Mono<ServerResponse> serverResponse(ToDoTask toDoTask) {
        return ok().body(Mono.just(toDoTask), ToDoTask.class)
                .onErrorResume(errorHandler::throwableError);
    }


}
