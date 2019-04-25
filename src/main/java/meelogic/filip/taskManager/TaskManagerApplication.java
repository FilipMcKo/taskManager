package meelogic.filip.taskManager;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class TaskManagerApplication {

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mysql://10.6.4.172:3306/taskManager", "root", "supersecret")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        SpringApplication.run(TaskManagerApplication.class, args);

    }
}
