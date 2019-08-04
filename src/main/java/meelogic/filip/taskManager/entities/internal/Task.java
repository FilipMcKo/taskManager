package meelogic.filip.taskmanager.entities.internal;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private State currentState;
    private Double progressPercentage;
    private Long taskBeginTime;
    private Long customDuration;
    private Integer priority;
}
