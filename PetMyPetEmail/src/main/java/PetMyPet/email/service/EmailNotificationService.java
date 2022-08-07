package PetMyPet.email.service;

import PetMyPet.email.JSON.NotificationEmailData;
import PetMyPet.email.emailUtils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;

@Service
public class EmailNotificationService {

    private final EmailSender emailService;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public EmailNotificationService(EmailSender emailService) {
        this.emailService = emailService;
    }

    public Mono<Void> deleteNotification(NotificationEmailData notificationEmailData) throws MessagingException {

        return Mono.empty()
                .doOnSuccess( (a) -> {

                    try {

                        String htmlBody = thymeleafTemplateEngine.process("deleted_reservation.html", setContext(notificationEmailData));
                        emailService.send(notificationEmailData.getRecipient(), htmlBody, "Rezerwacja anulowana przez klienta.");

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                })
                .then();
    }

    public Mono<Void> addNotification(NotificationEmailData notificationEmailData) {

        return Mono.empty()
                .doOnSuccess( (a) -> {

                    try {

                        String htmlBody = thymeleafTemplateEngine.process("added_reservation.html", setContext(notificationEmailData));
                        emailService.send(notificationEmailData.getRecipient(), htmlBody, "Nowa rezerwacja do twojego hotelu");

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                })
                .then();
    }

    public Mono<Void> patchNotification(NotificationEmailData notificationEmailData) {

        return Mono.empty()
                .doOnSuccess( (a) -> {

                    try {

                        String htmlBody = thymeleafTemplateEngine.process("patch_reservation.html", setContext(notificationEmailData));
                        emailService.send(notificationEmailData.getRecipient(), htmlBody, "Zmiana statusu twojej rezerwacji.");

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                })
                .then();
    }

    //support functions

    private Context setContext(NotificationEmailData notificationEmailData){

        Context thContext = new Context(); //add variables to template
        thContext.setVariable("hotelName", notificationEmailData.getHotelName());
        thContext.setVariable("animalType", notificationEmailData.getAnimalTypeStr());
        thContext.setVariable("animalName", notificationEmailData.getAnimalName());
        thContext.setVariable("startingDate", notificationEmailData.getCheckIn());
        thContext.setVariable("endingDate", notificationEmailData.getCheckOut());


        if( notificationEmailData.getStatus() != null && notificationEmailData.getStatus().equals("A"))
            thContext.setVariable("status", "zaakceptowane");
        else
            thContext.setVariable("status", "odrzucone");

        return thContext;
    }
}
