package meelogic.filip.taskManager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskDTO {
    private Integer id;
    private String name;
    private State currentState;
    private Double progressPercentage;
    private Long taskBeginTime;
}
