package meelogic.filip.taskManager.entities.internal;

public enum TaskDuration {
    SHORT(5000L), REGULAR(10000L), LONG(300000L);

    private final long duration;

    TaskDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }
}
