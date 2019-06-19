package meelogic.filip.taskManager.entities.external;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class TaskCreationRequest {
    @NotNull
    @Size(min = 2, max = 30)
    String name;
    String description;
    Long customDuration;
    String taskPriority;
}
