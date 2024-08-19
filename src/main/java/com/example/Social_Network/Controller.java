package com.example.Social_Network;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Controller {
    @GetMapping("/home")
    public String home(){
        return "hihi";
    }

}
