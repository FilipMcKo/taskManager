package meelogic.filip.taskManager.configurations;

import org.springframework.stereotype.Component;

@Component
public class TestReceiver {

    public void receiveMessage(String message) throws InterruptedException {

        System.out.println("Id that came back: " + message);
    }

}
