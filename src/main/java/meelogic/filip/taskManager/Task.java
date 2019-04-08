package meelogic.filip.taskManager;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private State currentState;

    @Column
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

    @Override
    public String toString() {
        return ("id: " + id + "\nname: " + name + "\ncurrent state: " + currentState + "\nprogress: " + progress + "\n\n");
    }


}
