package com.example.registrationamigos3spring.AppUser;

import com.example.registrationamigos3spring.registration.token.ConfirmationToken;
import com.example.registrationamigos3spring.registration.token.ConfirmationTokenSevice;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService
{
    private final static String USER_NOT_FOUND_MSG=
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenSevice confirmationTokenSevice;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(AppUser appUser)
    {
      boolean userExists=  appUserRepository.findByEmail(appUser.getEmail()).isPresent();
      if(userExists)
      {

          //TODO check of attributes are same
          //TODO if email not confirmed and send confirmatio email

          throw new IllegalStateException("email already taken");
      }
      String encodedPassword= bCryptPasswordEncoder.encode(appUser.getPassword());

      appUser.setPassword(encodedPassword);

      appUserRepository.save(appUser);

        String token=UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenSevice.saveConfirmationToken(confirmationToken);

        //TODO:send email

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

}
