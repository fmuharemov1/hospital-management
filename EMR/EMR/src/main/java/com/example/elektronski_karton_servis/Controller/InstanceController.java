package com.example.elektronski_karton_servis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {

    @Autowired
    private Environment environment;

    @GetMapping("/instance")
    public String getPort() {
        return "Handled by port: " + environment.getProperty("local.server.port");
    }
}
