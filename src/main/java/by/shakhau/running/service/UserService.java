package by.shakhau.running.service;

import by.shakhau.running.service.dto.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User findByName(String name);
    User findByNameAndPassword(String name, String password);
    User createUser(String name, String password);
}
