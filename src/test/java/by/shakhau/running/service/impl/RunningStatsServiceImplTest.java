package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.entity.StatsEntity;
import by.shakhau.running.persistence.repository.RunningStatsRepository;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.mapper.StatsMapper;
import by.shakhau.running.service.util.AssertHelper;
import by.shakhau.running.service.util.DtoFactory;
import by.shakhau.running.service.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RunningStatsServiceImplTest {

    @Mock
    private RunningStatsRepository runningStatsRepository;

    @Mock
    private UserService userService;

    @Mock
    private StatsMapper statsMapper;

    @InjectMocks
    private RunningStatsServiceImpl runningStatsService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        StatsEntity statsEntity1 = EntityFactory.getStats();
        StatsEntity statsEntity2 = EntityFactory.getStats();
        statsEntity2.setId(2L);
        List<StatsEntity> statsEntityCollection = Arrays.asList(statsEntity1, statsEntity2);

        Stats stats1 = DtoFactory.getStats();
        Stats stats2 = DtoFactory.getStats();
        stats2.setId(2L);
        List<Stats> statsCollection = Arrays.asList(stats1, stats2);

        when(statsMapper.toDtoList(statsEntityCollection)).thenReturn(statsCollection);
        when(runningStatsRepository.findAll()).thenReturn(statsEntityCollection);

        List<Stats> statsFound = runningStatsService.findAll();

        verify(runningStatsRepository, times(1)).findAll();
        AssertHelper.assertStats(statsCollection, statsFound);
    }

    @Test
    public void teasFindById() {
        Long id = DtoFactory.getId();
        Optional<StatsEntity> statsEntity = Optional.of(EntityFactory.getStats());
        Stats stats = DtoFactory.getStats();

        when(statsMapper.toDto(statsEntity.get())).thenReturn(stats);
        when(runningStatsRepository.findById(id)).thenReturn(statsEntity);

        Stats statsFound = runningStatsService.findById(id);

        verify(runningStatsRepository, times(1)).findById(id);
        AssertHelper.assertStats(stats, statsFound);
    }

    @Test
    public void testAdd() {
        User user = DtoFactory.getUser();
        Stats stats = DtoFactory.getStats();
        StatsEntity statsEntity = EntityFactory.getStats();
        statsEntity.setId(null);

        when(userService.findById(user.getId())).thenReturn(user);
        when(runningStatsRepository.save(statsEntity)).thenReturn(statsEntity);
        when(statsMapper.toEntity(stats)).thenReturn(statsEntity);
        when(statsMapper.toDto(statsEntity)).thenReturn(stats);

        Stats statsCreated = runningStatsService.add(stats, user.getId());

        verify(runningStatsRepository, times(1)).save(statsEntity);
        AssertHelper.assertStats(stats, statsCreated);
    }

    @Test
    public void testUpdate() {
        User user = DtoFactory.getUser();
        Stats stats = DtoFactory.getStats();
        StatsEntity statsEntity = EntityFactory.getStats();
        statsEntity.setId(null);

        when(userService.findById(user.getId())).thenReturn(user);
        when(runningStatsRepository.save(statsEntity)).thenReturn(statsEntity);
        when(statsMapper.toEntity(stats)).thenReturn(statsEntity);
        when(statsMapper.toDto(statsEntity)).thenReturn(stats);

        Stats statsCreated = runningStatsService.update(stats);

        verify(runningStatsRepository, times(1)).save(statsEntity);
        AssertHelper.assertStats(stats, statsCreated);
    }

    @Test
    public void testDelete() {
        Stats stats = DtoFactory.getStats();

        runningStatsService.delete(stats.getId());

        verify(runningStatsRepository, times(1)).deleteById(stats.getId());
    }

    @Test
    public void testAverageStats() {
        User user = DtoFactory.getUser();
        StatsEntity statsEntity1 = EntityFactory.getStats();
        StatsEntity statsEntity2 = EntityFactory.getStats();
        statsEntity2.setId(2L);
        List<StatsEntity> statsEntityCollection = Arrays.asList(statsEntity1, statsEntity2);

        Stats stats1 = DtoFactory.getStats();
        Stats stats2 = DtoFactory.getStats();
        stats2.setId(2L);
        List<Stats> statsCollection = Arrays.asList(stats1, stats2);

        ReflectionTestUtils.setField(runningStatsService, "dateFormat", "yyyy-MM-dd");
        when(runningStatsRepository.findByUserId(user.getId())).thenReturn(statsEntityCollection);
        when(statsMapper.toDtoList(statsEntityCollection)).thenReturn(statsCollection);

        List<AverageStats> averageStatsFound = runningStatsService.averageStats(user.getId());

        for (AverageStats averageSt : averageStatsFound) {
            assertThat(averageSt.getTitle()).startsWith("Weak 1: (");
            assertThat(averageSt.getAverageSpeed()).isEqualTo(
                    (stats1.getDistance() + stats2.getDistance()) / (stats1.getTime() + stats2.getTime()));
            assertThat(averageSt.getTotalDistance()).isEqualTo(stats1.getDistance() + stats2.getDistance());
            assertThat(averageSt.getAverageTime()).isEqualTo((stats1.getTime() + stats2.getTime()) / statsEntityCollection.size());
        }
    }
}