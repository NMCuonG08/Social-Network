package com.example.Social_Network.User.Registration;

import com.example.Social_Network.User.IUserService;
import com.example.Social_Network.User.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private final IUserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping()
    public String createUser(@RequestBody RegistrationRequest request,
                             final HttpServletRequest httpServletRequest){
        User user = userService.register(request);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));
        return "Success! Please check your email to complete your registration.";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam String token){
        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if(theToken.getUser().isEnable()){
            return "The user has already exist!";
        }
        String verificationResult =userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("Valid")){
            return "Email verification successful! Now you can log in to your account!";
        }
        return "Invalid verification token.";
    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName()
                + ":" + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
}
