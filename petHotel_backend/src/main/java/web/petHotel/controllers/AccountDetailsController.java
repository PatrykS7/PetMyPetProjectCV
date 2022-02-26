package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.OwnerAccountDetailWithCompanyInfo;
import web.petHotel.entities.AccountDetails;
import web.petHotel.service.AccountDetailsService;

@RestController
@RequestMapping("/api")
public class AccountDetailsController {

    @Autowired
    AccountDetailsService accountDetailsService;

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/accountDetails/{username}")
    public Mono<AccountDetails> getAccountDetailsByUsername(@PathVariable String username){

        return  accountDetailsService.getAccountDetailsByUsername(username);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/patchAccountDetails")
    public Mono<AccountDetails> updateAccountDetails(@RequestBody Mono<AccountDetails> accountDetails){

        return accountDetailsService.updateAcountDetails(accountDetails);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/saveAccountDetails")
    public Mono<AccountDetails> saveAccountDetails(@RequestBody Mono<AccountDetails> accountDetailsMono){

        return accountDetailsService.saveAccountDetails(accountDetailsMono);
    }

    @PreAuthorize("hasAnyRole('USER','OWNER')")
    @GetMapping("/ownerAccountDetails/{username}")
    public Mono<OwnerAccountDetailWithCompanyInfo> getOwnerDetails(@PathVariable String username){

        return accountDetailsService.getOwnerDetails(username);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/patchOwnerWithDetails")
    public Mono<AccountDetails> patchOwnerDetails(@RequestBody Mono<OwnerAccountDetailWithCompanyInfo> ownerDet){

        return accountDetailsService.patchOwnerDetails(ownerDet);
    }
}
