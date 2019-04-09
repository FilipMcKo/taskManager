package meelogic.filip.taskManager;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class Task {
    private Integer id;

    private String name;

    private State currentState;

    private Double progress;

    public Task(Integer id, String name, State currentState, Double progress) {
        this.id = id;
        this.name = name;
        this.currentState = currentState;
        this.progress = progress;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
