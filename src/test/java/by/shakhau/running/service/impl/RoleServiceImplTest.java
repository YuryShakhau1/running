package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.persistence.repository.RoleRepository;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.mapper.RoleMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByName() {
        String name = "Name";

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(name);

        Role role = new Role();
        role.setId(1L);
        role.setName(name);

        when(roleRepository.findByName(name)).thenReturn(roleEntity);
        when(roleMapper.toDto(roleEntity)).thenReturn(role);

        Role roleFound = roleService.findByName(name);

        assertThat(role.getId()).isEqualTo(roleFound.getId());
        assertThat(role.getName()).isEqualTo(roleFound.getName());
    }
}