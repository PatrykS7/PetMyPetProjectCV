package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.AuthResponse;
import web.petHotel.JSON.UserWithDetails;
import web.petHotel.JSON.UserWithDetailsWithCompany;
import web.petHotel.JSON.UsernameAndPass;
import web.petHotel.entities.AccountDetails;
import web.petHotel.entities.CompanyInfo;
import web.petHotel.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Mono<String> login(@RequestBody UsernameAndPass usernameAndPass, ServerWebExchange exchange){

        return userService.login(usernameAndPass, exchange);
    }

    @PostMapping("/mobileLogin")
    public Mono<AuthResponse> mobileLogin(@RequestBody UsernameAndPass usernameAndPass){

        return userService.mobileLogin(usernameAndPass);
    }

    @PostMapping("/registerUser")
    public Mono<AccountDetails> registerUser(@RequestBody Mono<UserWithDetails> userWithDetailsMono){

        return userService.registerUser(userWithDetailsMono);
    }

    @PostMapping("/registerOwner")
    public Mono<CompanyInfo> registerOwner(@RequestBody Mono<UserWithDetailsWithCompany> ownerWithInfo){

        return userService.registerOwner(ownerWithInfo);
    }

    @DeleteMapping("/deleteUser/{username}")
    @PreAuthorize("hasAnyRole('OWNER','USER')")
    public Mono<Void> deleteUserByUsername(@PathVariable String username){

        return userService.deleteUserByUsername(username);
    }

}