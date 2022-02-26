package PetMyPet.email.emailUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
@Async
public class EmailService implements EmailSender{

    @Autowired
    @Qualifier("gmail")
    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String email, String subject){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("petmypetmail@gmail.com");

            mailSender.send(mimeMessage);
        }
        catch (MessagingException e){

            throw new IllegalStateException("Could not send email");
        }
    }
}
