package main.exchanges.topic;

import com.rabbitmq.client.*;
import main.utils.Utils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Consumer {


    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, TimeoutException {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Utils.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        System.out.println("Exchange declared !");

        String randomQueueName = channel.queueDeclare().getQueue();

        System.out.println("Please type the binding key for this Queue");
        String bindingKey = scanner.nextLine();

        channel.queueBind(randomQueueName,Utils.DIRECT_EXCHANGE,bindingKey);
        System.out.println("binding between exchange and queue is done !");


        DeliverCallback callback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody());
            System.out.println("MESSAGE RECEIVED : "+message);
            System.out.println("--------------------------------------------------------------------------");
        };

        channel.basicConsume(randomQueueName,true,callback,consumerTag -> {});

    }

}
