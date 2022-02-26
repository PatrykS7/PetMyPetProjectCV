# PetMyPetProjectCV

This is a snippet of a backend app that I am currently working on. It's not yet finished, thus it can contain some errors and some parts of it might get changed in the future.

The petHotel_backend folder holds reactive REST API (with WebFlux) used to service an application for animal hotels. Some features of this application are: making and managing users accounts(both for hotel owners and normal users), making reservations, browsing through offers, adding and managing hotel, giving reviews. The authorization and authentication are managed through Spring Security and JWT tokens sent by http only cookies. The application is connected to Postgres database using R2DBC.

The PetMyPetEmail folder contains the backend application that conducts the email verification and password reset systems. For a given request, application composes an email message with a unique link that leads to a website where the user can fulfill his call.

Both apps are treated like microservices and are communicating to each other using  Eureka Server which (very) basic implementation is in PetMyPetEurekaServer folder.

In the future, I am planning to add reactive service for images management and dig deeper into Spring Cloud.
