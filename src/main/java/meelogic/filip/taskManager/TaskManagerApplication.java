package meelogic.filip.taskManager;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TaskManagerApplication {

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://10.6.4.172:3306/taskManager","root","supersecret");
        flyway.migrate();
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}
