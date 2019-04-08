package meelogic.filip.taskManager;

import lombok.Data;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import javax.persistence.*;

@Data
public class Task {

    private Integer id;

    private String name;

    private State currentState;

    private Double progress;

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ("id: " + id + "\nname: " + name + "\ncurrent state: " + currentState + "\nprogress: " + progress + "\n\n");
    }


}
