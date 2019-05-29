package by.shakhau.running.rest;

import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{userName}/available")
    public Boolean available(@PathVariable String userName) {
        return userService.findByName(userName) == null;
    }

    @PostMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@PathVariable String userName, @PathVariable String password) {
        return userService.createUser(userName, password);
    }
}
