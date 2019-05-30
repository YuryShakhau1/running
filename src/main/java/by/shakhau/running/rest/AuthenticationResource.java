package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.security.jwt.JwtTokenProvider;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserAuthenticationDto user) {
        try {
            String userName = user.getUserName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, user.getPassword()));

            User userFound = userService.findByName(userName);

            if (userFound == null) {
                throw new BadCredentialsException("User " + userName + " not found");
            }

            return ResponseEntity.ok(jwtTokenProvider.createToken(userName, userFound.getRoles()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid user name or password", e);
        }
    }
}
