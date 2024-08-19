package com.example.Social_Network.WebSocket;

import com.example.Social_Network.Security.JWT.JWTService;
import com.example.Social_Network.Security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketTokenFiller  implements ChannelInterceptor {

    private final JWTService jwtService;
    private final UserDetailService userDetailService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(StompCommand.CONNECT == accessor.getCommand()){
            String jwt = jwtService.parseJwt(accessor);
            if(jwt != null && jwtService.validToken(jwt)){
                String username = jwtService.extractUsernameFromToken(jwt);
                UserDetails userDetails = userDetailService.loadUserByUsername(jwt);
                UsernamePasswordAuthenticationToken authenticationToken  =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null,userDetails.getAuthorities()
                        );
                accessor.setUser(authenticationToken);
            }
        }

        return message;
    }
}
