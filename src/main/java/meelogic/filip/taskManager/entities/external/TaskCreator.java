package meelogic.filip.taskManager.entities.external;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor

// TODO: nazwa
public class TaskCreator {
    // TODO: javax.validation
    String name;
    String decription;
}
