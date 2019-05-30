package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.util.AssertHelper;
import by.shakhau.running.util.DtoFactory;
import by.shakhau.running.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToEntity() {
        Role role = DtoFactory.getRole();

        RoleEntity roleEntityCrated = roleMapper.toEntity(role);

        AssertHelper.assertRoles(roleEntityCrated, role);
    }

    @Test
    public void testToDto() {
        RoleEntity roleEntity = EntityFactory.getRole();

        Role roleCreated = roleMapper.toDto(roleEntity);

        AssertHelper.assertRoles(roleEntity, roleCreated);
    }
}