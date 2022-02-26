package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.AuthResponse;
import web.petHotel.JSON.UserWithDetails;
import web.petHotel.JSON.UserWithDetailsWithCompany;
import web.petHotel.JSON.UsernameAndPass;
import web.petHotel.entities.AccountDetails;
import web.petHotel.entities.CompanyInfo;
import web.petHotel.entities.User;
import web.petHotel.repo.AccountDetailsRepo;
import web.petHotel.repo.CompanyInfoRepo;
import web.petHotel.repo.UserRepo;
import web.petHotel.security.JWTUtil;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AccountDetailsRepo accountDetailsRepo;
    private final CompanyInfoRepo companyInfoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    public UserService(UserRepo userRepo, AccountDetailsRepo accountDetailsRepo, CompanyInfoRepo companyInfoRepo) {
        this.userRepo = userRepo;
        this.accountDetailsRepo = accountDetailsRepo;
        this.companyInfoRepo = companyInfoRepo;
    }

    public Flux<User> findAllUsers(){

        return userRepo.findAll();
    }

    public Mono<User> findByUsername(String pathUsername){

        return userRepo.findById(pathUsername)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")));
    }

    public Mono<User> saveUser(Mono<User> postUser) {

        return userRepo.saveAll(postUser).next()
                .onErrorResume(DataIntegrityViolationException.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken")));
    }

    public Mono<AccountDetails> registerUser(Mono<UserWithDetails> userWithDetailsMono) { //ugh

        return userWithDetailsMono.map( up -> {
            up.setPassword(passwordEncoder.encode(up.getPassword()));
            return up;
        })
                .flatMap( ud -> {
                    return userRepo.saveAll(ud.convertToUser()).next()
                            .then( webClient.build()
                                    .post()
                                    .uri("http://EMAIL-SERVICE/email/generateToken/" + ud.getUsername() + "/activate")
                                    .retrieve()
                                    .bodyToMono(Object.class))
                            .then(accountDetailsRepo.saveAll(ud.convertToAccountDetails()).next());
                })
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken")));
    }

    public Mono<CompanyInfo> registerOwner(Mono<UserWithDetailsWithCompany> ownerWithInfo) { //ugh

        return ownerWithInfo.map( up -> {
                    up.setPassword(passwordEncoder.encode(up.getPassword()));
                    return up;
                })
                .flatMap( ud -> {
                    return userRepo.saveAll(ud.convertToUser()).next()
                            .then( webClient.build()
                                    .post()
                                    .uri("http://EMAIL-SERVICE/email/generateToken/" + ud.getUsername())
                                    .retrieve()
                                    .bodyToMono(Object.class))
                            .then(accountDetailsRepo.saveAll(ud.convertToAccountDetails()).next())
                            .then(companyInfoRepo.saveAll(ud.convertToCompanyInfo()).next());
                })
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken")));

    }

    private final JWTUtil jwtUtil = new JWTUtil();

    public Mono<String> login(UsernameAndPass usernameAndPass, ServerWebExchange exchange) {

        return userRepo.findById(usernameAndPass.getUsername())
                .flatMap( usr -> {

                    if( passwordEncoder.matches( usernameAndPass.getPassword(), usr.getPassword())){

                        exchange.getResponse().addCookie(ResponseCookie.from("PetMyPetJWT", jwtUtil.generateToken(usr))
                                .secure(true)
                                .httpOnly(true)
                                .sameSite("Lax")
                                .build());

                        if(usr.isEnabled()) //check if acc active
                            return Mono.just( usr.getAuthority());
                        else
                            return Mono.just( "");
                    }
                    else
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to login"));
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to login")));
    }

    public Mono<AuthResponse> mobileLogin(UsernameAndPass usernameAndPass) {

        return userRepo.findById(usernameAndPass.getUsername())
                .flatMap( usr -> {

                    if( passwordEncoder.matches( usernameAndPass.getPassword(), usr.getPassword()))

                        if (usr.isEnabled())  //check if acc active
                            return Mono.just( new AuthResponse(jwtUtil.generateToken(usr), usr.getAuthority()));
                        else
                            return Mono.just( new AuthResponse());
                    else
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to login"));
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to login")));
    }
}