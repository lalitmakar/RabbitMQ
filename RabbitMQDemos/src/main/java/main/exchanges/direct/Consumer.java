package main.exchanges.direct;

import com.rabbitmq.client.*;
import main.utils.Utils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//The binding key must also be in the same form. The logic behind the topic exchange is similar to a direct one -
// a message sent with a particular routing key will be delivered to all the queues that are bound with a matching binding key.
// However there are two important special cases for binding keys:
//
//* (star) can substitute for exactly one word.
//# (hash) can substitute for zero or more words.
public class Consumer {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, TimeoutException {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Utils.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        System.out.println("Exchange declared !");

        String randomQueueName = channel.queueDeclare().getQueue();

        System.out.println("Please type the binding key for this Queue");
        String bindingKey = scanner.nextLine();

        channel.queueBind(randomQueueName,Utils.TOPIC_EXCHANGE,bindingKey);
        System.out.println("binding between exchange and queue is done !");


        DeliverCallback callback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody());
            System.out.println("MESSAGE RECEIVED : "+message);
            System.out.println("--------------------------------------------------------------------------");
        };

        channel.basicConsume(randomQueueName,true,callback,consumerTag -> {});

    }
}
