package meelogic.filip.taskManager;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;


@Data
public class Task {

    final static AtomicInteger counter = new AtomicInteger();

    private Integer id;

    private String name;

    private State currentState;

    private Double progressPercentage;

    Task(String name) {
        this.id=counter.incrementAndGet();
        this.name = name;
        this.currentState = State.NONE;
        this.progressPercentage = 0.0;
    }

}
