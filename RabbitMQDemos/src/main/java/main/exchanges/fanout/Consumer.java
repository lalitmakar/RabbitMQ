package main.exchanges.fanout;

import com.rabbitmq.client.*;
import main.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//run multiple instances of consumer, it will craete a random Q and bind with fanout exchange.
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Utils.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        System.out.println("Exchange declared !");

        String randomQueueName = channel.queueDeclare().getQueue();

        channel.queueBind(randomQueueName,Utils.FANOUT_EXCHANGE,"");
        System.out.println("binding between exchange and queue is done !");


        DeliverCallback callback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody());
            System.out.println("MESSAGE RECEIVED : "+message);
            System.out.println("--------------------------------------------------------------------------");
        };

        channel.basicConsume(randomQueueName,true,callback,consumerTag -> {});

    }

}
