package com.energymeter.notificationservice.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfiguration {

    @Bean
    public Mono<Connection> connectionMono() {
        var connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        ExchangeBuilder.directExchange("notify-user").durable(true).build();
        return Mono.fromCallable(() -> connectionFactory.newConnection("notification")).cache();
    }

    @Bean
    public Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions()
                .connectionMono(connectionMono));
    }

    @Bean
    public ReceiverOptions receiverOptions(Mono<Connection> connectionMono) {
        return new ReceiverOptions()
                .connectionMono(connectionMono);
    }

    @Bean
    Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }

    @PreDestroy
    public void close() throws Exception {
        connectionMono().block().close();
    }

}
