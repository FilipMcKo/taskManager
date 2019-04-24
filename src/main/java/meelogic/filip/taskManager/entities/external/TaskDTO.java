package meelogic.filip.taskManager.entities.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import meelogic.filip.taskManager.entities.internal.State;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDTO {
    private Integer id;
    private String name;
    private String description;
    private State currentState;
    private Double progressPercentage;
}
