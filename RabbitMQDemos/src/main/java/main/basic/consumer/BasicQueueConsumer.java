package main.basic.consumer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import main.utils.Utils;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BasicQueueConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Utils.QUEUE_NAME,true,false,true,null);
        System.out.println("QUEUE DECLARED ...");

        DeliverCallback callback = (consumerTag,delivery)->{
            String message = new String(delivery.getBody());
            System.out.println("MESSAGE RECEIVED : "+message);
            System.out.println("--------------------------------------------------------------------------");
        };

        channel.basicConsume(Utils.QUEUE_NAME,true,callback,consumerTag -> {});

    }

}
