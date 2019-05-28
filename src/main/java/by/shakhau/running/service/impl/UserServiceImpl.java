package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.repository.UserRepository;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public User findById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }
}
