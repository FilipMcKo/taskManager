package meelogic.filip.taskManager.entities;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Task {

    public final static AtomicInteger counter = new AtomicInteger();
    private Integer id;
    private String name;
    private State currentState;
    private Double progressPercentage;
    private Long taskBeginTime;

    public Task(String name) {
        this.id = counter.incrementAndGet();
        this.name = name;
        this.currentState = State.NONE;
        this.progressPercentage = 0.0;
    }

    public Task() {
        this.id = counter.incrementAndGet();
        this.currentState = State.NONE;
        this.progressPercentage = 0.0;
    }
}
