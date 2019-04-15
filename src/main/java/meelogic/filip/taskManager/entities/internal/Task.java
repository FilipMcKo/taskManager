package meelogic.filip.taskManager.entities.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    private Integer id;
    private String name;
    private String description;
    private State currentState;
    private Double progressPercentage;
    private Long taskBeginTime;
}
