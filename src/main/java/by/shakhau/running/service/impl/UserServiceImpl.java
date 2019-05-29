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

    @Override
    public User findByName(String name) {
        return userMapper.toDto(userRepository.findByName(name));
    }

    @Override
    public User findByNameAndPassword(String name, String password) {
        return userMapper.toDto(userRepository.findByNameAndPassword(name, password));
    }

    @Override
    public User createUser(String name, String password) {
        if (userRepository.findByName(name) != null) {
            return null;
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)));
    }
}
