package PetMyPet.email.emailUtils;

import javax.mail.MessagingException;

public interface EmailSender {

    void send(String to, String email, String subject) throws MessagingException;
}
