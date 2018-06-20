package au.com.redhat.labs.demos.todoapi.api.mongo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ToDoListMongoRepository extends ReactiveCrudRepository<ToDoTask, String> {

}
