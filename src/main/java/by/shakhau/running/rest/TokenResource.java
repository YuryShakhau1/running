package by.shakhau.running.rest;

import by.shakhau.running.security.JwtGenerator;
import by.shakhau.running.security.model.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenResource {

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping
    public String generate(@RequestBody JwtUser jwtUser) {
        return jwtGenerator.generate(jwtUser);
    }
}
