package meelogic.filip.taskmanager.entities.internal;

public enum TaskPriority {
    LOW(3), HIGH(6);

    private final Integer priority;

    TaskPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriorityAsInteger() {
        return this.priority;
    }

}
