package by.shakhau.running.service;

import by.shakhau.running.service.dto.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
}
