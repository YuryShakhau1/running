package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.StatsEntity;
import by.shakhau.running.service.dto.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsMapper implements Mapper<StatsEntity, Stats> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public StatsEntity toEntity(Stats dto) {
        if (dto == null) {
            return null;
        }

        StatsEntity entity = new StatsEntity();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setDistance(dto.getDistance());
        entity.setTime(dto.getTime());
        entity.setUser(userMapper.toEntity(dto.getUser()));
        return entity;
    }

    @Override
    public Stats toDto(StatsEntity entity) {
        if (entity == null) {
            return null;
        }

        Stats dto = new Stats();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDistance(entity.getDistance());
        dto.setTime(entity.getTime());
        dto.setUser(userMapper.toDto(entity.getUser()));
        return dto;
    }
}
