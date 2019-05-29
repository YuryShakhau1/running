package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.repository.RoleRepository;
import by.shakhau.running.service.RoleService;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findByName(String name) {
        return roleMapper.toDto(roleRepository.findByName(name));
    }
}
