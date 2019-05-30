package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{userName}/available")
    public Boolean available(@PathVariable String userName) {
        return userService.findByName(userName) == null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody UserAuthenticationDto user) {
        return userService.createUser(user.getUserName(), user.getPassword());
    }
}
