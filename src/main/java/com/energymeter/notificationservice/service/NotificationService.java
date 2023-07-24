package com.energymeter.notificationservice.service;

import com.energymeter.notificationservice.dto.NotificationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Flux<String> getNotificationStream(long userId, Integer timeout);

    Mono<Void> sendNotification(NotificationDTO notificationDTO);
}
