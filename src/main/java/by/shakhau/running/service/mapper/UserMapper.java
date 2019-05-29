package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.UserEntity;
import by.shakhau.running.service.dto.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, User> {

    @Override
    public UserEntity toEntity(User dto) {
        if (dto == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        return entity;
    }

    @Override
    public User toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User dto = new User();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}
