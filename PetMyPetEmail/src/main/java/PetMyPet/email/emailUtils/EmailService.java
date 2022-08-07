package PetMyPet.email.emailUtils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Async
public class EmailService implements EmailSender{

    @Qualifier("gmail")
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("classpath:/images/pet1.png")
    private Resource pet1Img;

    @Value("classpath:/images/pet2.png")
    private Resource pet2Img;

    @Override
    public void send(String to, String email, String subject){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("petmypetmail@gmail.com");

            //add images
            helper.addInline("pet1.png", pet1Img);
            helper.addInline("pet2.png", pet2Img);

            mailSender.send(mimeMessage);
        }
        catch (MessagingException e){

            throw new IllegalStateException("Could not send email");
        }
    }
}
