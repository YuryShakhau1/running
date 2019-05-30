package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.persistence.entity.UserEntity;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.util.AssertHelper;
import by.shakhau.running.service.util.DtoFactory;
import by.shakhau.running.service.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapper userMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void toEntity() {
        List<RoleEntity> roleEntities = Collections.singletonList(EntityFactory.getRole());
        User user = DtoFactory.getUser();

        when(roleMapper.toEntityList(user.getRoles())).thenReturn(roleEntities);

        UserEntity userEntityCrated = userMapper.toEntity(user);

        AssertHelper.assertUsers(userEntityCrated, user);
    }

    @Test
    public void toDto() {
        List<Role> roles = Collections.singletonList(DtoFactory.getRole());
        UserEntity userEntity = EntityFactory.getUser();

        when(roleMapper.toDtoList(userEntity.getRoles())).thenReturn(roles);

        User userCrated = userMapper.toDto(userEntity);

        AssertHelper.assertUsers(userEntity, userCrated);
    }
}