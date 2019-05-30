package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.persistence.entity.UserEntity;
import by.shakhau.running.persistence.repository.UserRepository;
import by.shakhau.running.service.RoleService;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.mapper.UserMapper;
import by.shakhau.running.util.AssertHelper;
import by.shakhau.running.util.DtoFactory;
import by.shakhau.running.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        User user1 = DtoFactory.getUser();
        User user2 = DtoFactory.getUser();
        user2.setId(2L);
        user2.setName("UserName2");
        List<User> users = Arrays.asList(user1, user2);

        UserEntity userEntity1 = EntityFactory.getUser();
        UserEntity userEntity2 = EntityFactory.getUser();
        userEntity2.setId(2L);
        userEntity2.setName("UserName2");
        List<UserEntity> userEntities = Arrays.asList(userEntity1, userEntity2);

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.toDtoList(userEntities)).thenReturn(users);

        List<User> usersFound = userService.findAll();

        verify(userRepository, times(1)).findAll();
        AssertHelper.assertUsers(users, usersFound);
    }

    @Test
    public void testFindById() {
        Optional<UserEntity> userEntity = Optional.of(EntityFactory.getUser());
        User user = DtoFactory.getUser();

        when(userRepository.findById(user.getId())).thenReturn(userEntity);
        when(userMapper.toDto(userEntity.get())).thenReturn(user);

        User userFound = userService.findById(user.getId());

        verify(userRepository, times(1)).findById(user.getId());
        AssertHelper.assertUsers(user, userFound);
    }

    @Test
    public void testFindByName() {
        UserEntity userEntity = EntityFactory.getUser();
        User user = DtoFactory.getUser();

        when(userRepository.findByName(user.getName())).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(user);

        User userFound = userService.findByName(user.getName());

        verify(userRepository, times(1)).findByName(user.getName());
        AssertHelper.assertUsers(user, userFound);
    }

    @Test
    public void testFindByNameAndPassword() {
        UserEntity userEntity = EntityFactory.getUser();
        User user = DtoFactory.getUser();

        when(userRepository.findByNameAndPassword(user.getName(), user.getPassword())).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(user);

        User userFound = userService.findByNameAndPassword(user.getName(), user.getPassword());

        verify(userRepository, times(1)).findByNameAndPassword(user.getName(), user.getPassword());
        AssertHelper.assertUsers(user, userFound);
    }

    @Test
    public void testCreateUser() {
        String encodedPassword = "EncodedPassword";
        Role role = DtoFactory.getRole();
        User user = DtoFactory.getUser();
        user.setId(null);
        UserEntity userEntityWithEncodedPassword = EntityFactory.getUser();
        userEntityWithEncodedPassword.setId(null);
        userEntityWithEncodedPassword.setPassword(encodedPassword);
        User userWithEncodedPassword = DtoFactory.getUser();
        userWithEncodedPassword.setId(null);
        userWithEncodedPassword.setPassword(encodedPassword);

        when(roleService.findByName(RoleEntity.USER_ROLE)).thenReturn(role);
        when(userRepository.save(userEntityWithEncodedPassword)).thenReturn(userEntityWithEncodedPassword);
        when(userMapper.toEntity(any())).thenReturn(userEntityWithEncodedPassword);
        when(userMapper.toDto(userEntityWithEncodedPassword)).thenReturn(userWithEncodedPassword);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);

        User userCreated = userService.createUser(user.getName(), user.getPassword());

        verify(userRepository, times(1)).save(userEntityWithEncodedPassword);

        assertThat(user.getId()).isEqualTo(userCreated.getId());
        assertThat(user.getName()).isEqualTo(userCreated.getName());
        assertThat(userCreated.getPassword()).isEqualTo(encodedPassword);

        AssertHelper.assertRoles(user.getRoles(), userCreated.getRoles());
    }
}