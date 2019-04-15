package meelogic.filip.taskManager.entities.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import meelogic.filip.taskManager.entities.internal.State;

@AllArgsConstructor
@Data
public class TaskDTO {
    private Integer id;
    private String name;
    private String description;
    private State currentState;
    private Double progressPercentage;
}
