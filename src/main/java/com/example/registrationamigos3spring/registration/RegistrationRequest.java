package com.example.registrationamigos3spring.registration;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest
{
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

}
