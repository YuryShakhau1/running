package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.service.dto.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements Mapper<RoleEntity, Role> {

    @Override
    public RoleEntity toEntity(Role dto) {
        RoleEntity entity = new RoleEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public Role toDto(RoleEntity entity) {
        Role dto = new Role();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
