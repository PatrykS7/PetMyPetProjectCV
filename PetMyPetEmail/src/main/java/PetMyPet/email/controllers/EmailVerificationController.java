package PetMyPet.email.controllers;

import PetMyPet.email.entities.ActivationToken;
import PetMyPet.email.service.ActivationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/email")
public class EmailVerificationController {

    private final ActivationTokenService activationTokenService;

    @Autowired
    public EmailVerificationController(ActivationTokenService activationTokenService) {
        this.activationTokenService = activationTokenService;
    }

    @PostMapping("/generateToken/{username}/{type}")
    public Mono<ActivationToken> generateToken(@PathVariable String username,@PathVariable String type){

        return activationTokenService.generateToken(username,type);
    }

    @PostMapping("/activateAccount/{token}")
    public Mono<ActivationToken> activateAccount(@PathVariable String token){

        return activationTokenService.activateAccount(token);
    }

    @GetMapping("/resetPassword/{token}/{password}")
    public Mono<ActivationToken> resetPassword(@PathVariable String token, @PathVariable String password){

        return activationTokenService.resetPassword(token,password);
    }
}
