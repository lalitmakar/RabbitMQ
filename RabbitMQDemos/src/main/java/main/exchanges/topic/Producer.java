package main.exchanges.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import main.utils.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


//Messages sent to a topic exchange can't have an arbitrary routing_key - it must be a list of words, delimited by dots.
public class Producer {

    private static Scanner scanner = new Scanner(System.in);

    private static String EXCHANGE_NAME = Utils.TOPIC_EXCHANGE;

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            System.out.println("Exchange Declared !");

            while(true){
                System.out.println("Please type the routing key of the message that you want to send : ");
                String routingKey = scanner.nextLine();

                System.out.println("Please type the message that you want to send : ");
                String message = scanner.nextLine();


                System.out.println("Trying to put message : "+message+" in Queue...");
                channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Message Sent !");
            }


        } finally {
            System.out.println("DONE !");
        }

    }

}
