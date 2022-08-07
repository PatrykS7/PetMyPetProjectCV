package PetMyPet.email.controllers;

import PetMyPet.email.JSON.NotificationEmailData;
import PetMyPet.email.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class EmailNotificationsController {

    @Autowired
    EmailNotificationService emailNotificationService;

    @PostMapping("/notification/delete")
    public Mono<Void> deleteNotification (@RequestBody NotificationEmailData notificationEmailData) throws MessagingException {

        return emailNotificationService.deleteNotification(notificationEmailData);
    }

    @PostMapping("/notification/add")
    public Mono<Void> addNotification (@RequestBody NotificationEmailData notificationEmailData) throws MessagingException {

        return emailNotificationService.addNotification(notificationEmailData);
    }

    @PostMapping("/notification/patch")
    public Mono<Void> patchNotification (@RequestBody NotificationEmailData notificationEmailData) throws MessagingException {

        return emailNotificationService.patchNotification(notificationEmailData);
    }

}
