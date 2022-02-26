package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.OwnerAccountDetailWithCompanyInfo;
import web.petHotel.entities.AccountDetails;
import web.petHotel.repo.AccountDetailsRepo;
import web.petHotel.repo.CompanyInfoRepo;

@Service
public class AccountDetailsService {

    private final AccountDetailsRepo accountDetailsRepo;
    private final CompanyInfoRepo companyInfoRepo;

    @Autowired
    public AccountDetailsService(AccountDetailsRepo accountDetailsRepo, CompanyInfoRepo companyInfoRepo) {
        this.accountDetailsRepo = accountDetailsRepo;
        this.companyInfoRepo = companyInfoRepo;
    }

    public Mono<AccountDetails> getAccountDetailsByUsername(String username) {

        return accountDetailsRepo.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")));
    }

    public Mono<AccountDetails> updateAcountDetails(Mono<AccountDetails> accountDetails) {

        return accountDetails.map( acc -> {
            acc.setNew(false);
            return acc;
        })
                .flatMap( j -> accountDetailsRepo.save(j));
    }

    public Mono<AccountDetails> saveAccountDetails(Mono<AccountDetails> accountDetailsMono) {

        return accountDetailsRepo.saveAll(accountDetailsMono).next()
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to create account details")));
    }

    public Mono<OwnerAccountDetailWithCompanyInfo> getOwnerDetails(String username) {

        return accountDetailsRepo.findByUsername(username)
                .flatMap( ad -> {
                    return Mono.just(
                            new OwnerAccountDetailWithCompanyInfo(
                                    username,
                                    ad.getFirstName(),
                                    ad.getLastName(),
                                    ad.getStreet(),
                                    ad.getZipcode(),
                                    ad.getCity(),
                                    ad.getPhoneNumber(),
                                    null,
                                    null,
                                    null
                            )

                    );
                })
                .zipWith(companyInfoRepo.findById(username))
                .map( zip -> {
                    zip.getT1().setName(zip.getT2().getName());
                    zip.getT1().setNip(zip.getT2().getNip());
                    zip.getT1().setRegon(zip.getT2().getRegon());
                    return zip.getT1();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find owner")));
    }

    public Mono<AccountDetails> patchOwnerDetails(Mono<OwnerAccountDetailWithCompanyInfo> ownerDet) {

        return ownerDet.flatMap( od -> {
            return accountDetailsRepo.saveAll(od.convertToAccountDetails()).next()
                    .zipWith(companyInfoRepo.saveAll(od.convertToCompanyInfo()).next())
                    .map( zip -> zip.getT1());
        })
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to edit data")));
    }
}
