package com.energymeter.notificationservice.controller;

import com.energymeter.notificationservice.dto.NotificationDTO;
import com.energymeter.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(path = "/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribeNotification(@PathVariable long userId, @RequestParam(required = false) Integer timeout) {
        return notificationService.getNotificationStream(userId, timeout);
    }

    @PostMapping
    public void sendNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotification(notificationDTO).subscribe();
    }

}
