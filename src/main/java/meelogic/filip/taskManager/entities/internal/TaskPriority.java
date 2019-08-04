package meelogic.filip.taskmanager.entities.internal;

import java.util.Map;
import java.util.TreeMap;

public enum TaskPriority {
    LOW(3), MEDIUM(5), HIGH(7);

    private final Integer priority;
    private static Map<Integer, TaskPriority> taskMap = new TreeMap<>();

    static {
        taskMap.put(LOW.getPriorityAsInteger(), LOW);
        taskMap.put(MEDIUM.getPriorityAsInteger(), MEDIUM);
        taskMap.put(HIGH.getPriorityAsInteger(), HIGH);
    }

    TaskPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriorityAsInteger() {
        return this.priority;
    }

    public static String getPriorityAsString(Integer intPriority){
        return taskMap.get(intPriority).toString();
    }
}
