package by.shakhau.running.service.mapper;

import by.shakhau.running.persistence.entity.StatsEntity;
import by.shakhau.running.persistence.entity.UserEntity;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.util.AssertHelper;
import by.shakhau.running.service.util.DtoFactory;
import by.shakhau.running.service.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class StatsMapperTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private StatsMapper statsMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void toEntity() {
        UserEntity userEntity = EntityFactory.getUser();
        Stats stats = DtoFactory.getStats();

        when(userMapper.toEntity(stats.getUser())).thenReturn(userEntity);

        StatsEntity statsEntityCrated = statsMapper.toEntity(stats);

        AssertHelper.assertStats(statsEntityCrated, stats);
    }

    @Test
    public void toDto() {
        User user = DtoFactory.getUser();
        StatsEntity statsEntity = EntityFactory.getStats();

        when(userMapper.toDto(statsEntity.getUser())).thenReturn(user);

        Stats statsCrated = statsMapper.toDto(statsEntity);

        AssertHelper.assertStats(statsEntity, statsCrated);
    }
}