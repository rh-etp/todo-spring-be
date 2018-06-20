package au.com.redhat.labs.demos.todoapi.api;

import au.com.redhat.labs.demos.todoapi.common.routers.ApiRouter;
import au.com.redhat.labs.demos.todoapi.common.routers.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * this class is supposed to define the main entry point
 *
 * @author fmasood2
 */
@Component
public class ToDoListRouter implements ApiRouter {

    final ToDoListHandler todoListHandler;

    public ToDoListRouter(ToDoListHandler todoListHandler) {
        this.todoListHandler = todoListHandler;
    }

    @Override
    public RouterFunction<?> doRoute(ErrorHandler errorHandler) {
        return
                nest(path(BASE_PATH),
                        nest(
                                contentType(MediaType.APPLICATION_JSON),
                                route(GET(TODO_LIST_API_ENDPOINT), todoListHandler::fetchAlToDoTasks)
                                        .andRoute(POST(TODO_LIST_API_ENDPOINT), todoListHandler::saveToDoTask)
                        )
                                .andOther(route(RequestPredicates.all(), errorHandler::invalidPath))
                );

    }


    public final  static String BASE_PATH = "/api";

    public final static String TODO_LIST_API_ENDPOINT = "/todolist";


}
