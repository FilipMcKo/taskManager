package meelogic.filip.taskManager.configurations;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class RabbitMqConfig {
    private static final String QUEUE_NAME = "myQueue";
    private static final String FANOUT_EXCHANGE = "myExchange";

    @Bean
    public List<Declarable> fanoutBindings() {
        Map<String, Object> args = new HashMap();
        args.put("x-max-priority", 10);
        Queue fanoutQueue = new Queue(QUEUE_NAME, true, false, false, args);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE);

        return Arrays.asList(
                fanoutQueue,
                fanoutExchange,
                bind(fanoutQueue).to(fanoutExchange));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean
    Queue queue() {
        Map<String, Object> args = new HashMap();
        args.put("x-max-priority", 10);
        return new Queue(QUEUE_NAME, true, false, false, args);
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange fanoutExchange) {

        return bind(queue).to(fanoutExchange);
    }


/*
    to są narzędzia do zbierania wiadomości z kolejki
    muszę wrzucać wiadomość z kolejki do wątku jeżeli jest pusty,
    wątek który zgarnie z kolejki message ma co sekundę przeliczać progres taska
    i po osiągnięciu stu procent ma być stanu wolnego i ma pobrać kolejny message
    z kolejki, jeżeli cokolwiek jest w kolejce

    na koniec pamiętać o testach

    podsumowujac to wychodzi chyba na to, że muszę użyć konfiguracji javy (bez springa)
    czyli zadekralowac channelFactory i utworzyć channel, a potem na nim mogę wykonywać operacje
    basicGet i basicAck, które manualnie ściagją wiadomosci z kolejki i je zatwierdzają
    w konfiguracji springowej nie mam dostępu do obiektu channel a zarazem do metody basicGet
    czyli albo używam konfiguracji javy i obiektu channel, albo szukam analogicznego rozwiązania
    na konfiguracji springa
*/
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(QUEUE_NAME);
//        container.setMessageListener(listenerAdapter);
//        container.setConcurrentConsumers(1);
//        container.setConcurrency("1");
//
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(TestReceiver receiver) throws InterruptedException {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }

}
