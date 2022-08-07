package PetMyPet.email.service;

import PetMyPet.email.emailUtils.EmailSender;
import PetMyPet.email.emailUtils.EmailService;
import PetMyPet.email.emailUtils.EmailTemplates;
import PetMyPet.email.entities.ActivationToken;
import PetMyPet.email.repo.ActivationTokensRepo;
import PetMyPet.email.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActivationTokenService {

    private final ActivationTokensRepo activationTokensRepo;
    private final EmailSender emailService;
    private final UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;


    @Autowired
    public ActivationTokenService(ActivationTokensRepo activationTokensRepo, EmailSender emailService, UserRepo userRepo) {
        this.activationTokensRepo = activationTokensRepo;
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    public Mono<ActivationToken> generateToken(String username, String type) {

        String token = UUID.randomUUID().toString();

        return activationTokensRepo.saveOrReplace( username, token, LocalDateTime.now())
                .doOnSuccess( (a)  -> {

                    try {
                        chooseEmail(username,type,token);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                })
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to save token")));
    }

    public Mono<ActivationToken> activateAccount(String token) {

        return activationTokensRepo.findByToken(token)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")))
                .flatMap( activationToken -> {
                    return userRepo.setEnabledTrue(activationToken.getUsername())
                            .then(activationTokensRepo.deleteTokenEntry(activationToken.getToken()));
                });
    }

    private void chooseEmail(String username, String type, String token) throws MessagingException {

        if(type.equals("activate")) {

            Context thContext = new Context(); //add link to email
            String link = "http://localhost:3000/aktywujKonto?token=" + token;
            thContext.setVariable("link", link);

            String htmlBody = thymeleafTemplateEngine.process("activate_account.html", thContext);
            emailService.send(username, htmlBody, "PetMyPet, Potwierdź swój adres email");
        }
        else {

            Context thContext = new Context(); //add link to email
            String link = "http://localhost:3000/zresetujHaslo?token=" + token;
            thContext.setVariable("link", link);

            String htmlBody = thymeleafTemplateEngine.process("reset_password.html", thContext);
            emailService.send(username, htmlBody, "PetMyPet, Zresetuj hasło");
        }
    }

    public Mono<ActivationToken> resetPassword(String token, String password) {

        return activationTokensRepo.findByToken(token)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")))
                .flatMap( activationToken -> {
                   return userRepo.updatePassword(passwordEncoder.encode(password),activationToken.getUsername())
                           .then(activationTokensRepo.deleteTokenEntry(activationToken.getToken()));
                });
    }

}
