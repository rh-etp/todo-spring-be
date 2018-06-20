package au.com.redhat.labs.demos.todoapi.api.mongo;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data
@Document(collection = "ToDoTask")
@ToString
@Builder
@NoArgsConstructor
public class ToDoTask implements Serializable {
    @Id
    String id;
    String taskName;
    Boolean important;

    public ToDoTask(final String id, final String taskName, final Boolean important) {
        this.id = id;
        this.taskName = taskName;
        this.important = important;
    }
}
