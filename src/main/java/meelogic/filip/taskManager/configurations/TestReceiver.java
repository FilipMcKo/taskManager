package meelogic.filip.taskManager.configurations;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class TestReceiver {

    public void receiveMessage(String message) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("Received <" + message + ">");
    }

}
