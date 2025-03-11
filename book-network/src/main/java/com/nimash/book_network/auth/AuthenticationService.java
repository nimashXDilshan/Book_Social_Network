package com.nimash.book_network.auth;

import com.nimash.book_network.email.EmailService;
import com.nimash.book_network.email.EmailTemplateName;
import com.nimash.book_network.role.RoleRepository;
import com.nimash.book_network.user.Token;
import com.nimash.book_network.user.TokenRepository;
import com.nimash.book_network.user.User;
import com.nimash.book_network.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {

        //
        System.out.println(request);

        var userRole=roleRepository.findByName("USER")
                //todo better exception handling
                .orElseThrow(()->new IllegalStateException("ROLE USER was not initialized"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole)) //This ensures that the user is assigned the "USER" role upon registration.
                .build();   /// In this case use builder design pattern // because want to more inputs for the first time

        // actual position in the database insertion
        userRepository.save(user);

        sendValidationEmail(user);



    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken=generateAndSaveActivationToken(user);

        //send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );

    }

    private String generateAndSaveActivationToken(User user){

        //generate a token
        String generatedToken=generateActivationCode(6);
        // In the token class has builder

        // create the token object(with created & updated times)
        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        // save the created token in the database;
        tokenRepository.save(token);

        //return the generated token
        return  generatedToken;
    }


    // actual token generation part
    private String generateActivationCode(int length){

        String character ="0123456789";
        StringBuilder codeBuilder=new StringBuilder();
        SecureRandom secureRandom=new SecureRandom();

        for(int  i=0; i<10 ;i++){
            int randomIndex=secureRandom.nextInt(character.length()); //0..9
            codeBuilder.append(character.charAt(randomIndex));
        }

        return codeBuilder.toString();

    }
}
