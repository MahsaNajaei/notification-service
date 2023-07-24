package com.energymeter.notificationservice.service;

import com.energymeter.notificationservice.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.time.Duration;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationManager implements NotificationService {

    private final Sender sender;
    private final Receiver receiver;
    private final String subject = "notify-user";

    @Override
    public Flux<String> getNotificationStream(long userId, Integer timeout) {

        final var routingKey = subject + "-" + userId;
        final var declareQueue = sender.declareQueue(QueueSpecification.queue(routingKey));

        final var bindQueue = declareQueue.flatMap(
                declareOk ->
                        sender.bindQueue(
                                BindingSpecification.binding()
                                        .queue(declareOk.getQueue())
                                        .exchange(subject)
                                        .routingKey(routingKey)
                        ).map(bindOk -> declareOk.getQueue())
        ).log("Binding Queue!", Level.FINER);

        Flux<String> result = bindQueue
                .flatMapMany(receiver::consumeAutoAck)
                .map(item -> new String(item.getBody()));

        if (!ObjectUtils.isEmpty(timeout))
            result = result.timeout(Duration.ofMillis(timeout));

        return result;
    }

    @Override
    public Mono<Void> sendNotification(NotificationDTO notificationDTO) {
        log.info("sending message!");
        var declareExchange = sender.declareExchange(
                ExchangeSpecification.exchange().name(subject).durable(true).type("direct")
        );
        var routingKey = subject + "-" + notificationDTO.getReceiptID();
        OutboundMessage message = new OutboundMessage(subject, routingKey, notificationDTO.getContent().getBytes());
        return declareExchange.flatMap(item -> sender.send(Mono.fromSupplier(() -> message)));
    }
}
