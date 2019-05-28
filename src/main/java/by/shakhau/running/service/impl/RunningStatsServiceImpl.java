package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.entity.StatsEntity;
import by.shakhau.running.persistence.repository.RunningStatsRepository;
import by.shakhau.running.service.RunningStatsService;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.service.mapper.StatsMapper;
import by.shakhau.running.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class RunningStatsServiceImpl implements RunningStatsService {

    @Value("${date.format:yyyy-MM-dd}")
    private String dateFromat;

    @Autowired
    private RunningStatsRepository runningStatsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public List<Stats> findAll() {
        return statsMapper.toDtoList(runningStatsRepository.findAll());
    }

    @Override
    public Stats findById(Long id) {
        return statsMapper.toDto(runningStatsRepository.findById(id).orElse(null));
    }

    @Override
    public Stats add(Stats stats, Long userId) {
        User user = userService.findById(userId);
        stats.setUser(user);
        stats.setId(null);
        return statsMapper.toDto(runningStatsRepository.save(statsMapper.toEntity(stats)));
    }

    @Override
    public Stats update(Stats stats) {
        if (stats.getId() == null) {
            throw new IllegalArgumentException("Id of " + stats + " must not be null");
        }

        return statsMapper.toDto(runningStatsRepository.save(statsMapper.toEntity(stats)));
    }

    @Override
    public void delete(Long id) {
        runningStatsRepository.deleteById(id);
    }

    @Override
    public List<AverageStats> averageStats(Long userId) {
        return averageUserStats(statsMapper.toDtoList(runningStatsRepository.findByUserId(userId)));
    }

    private List<AverageStats> averageUserStats(List<Stats> userStats) {
        List<Stats> sortedStats = userStats.stream().sorted(Comparator.comparing(Stats::getDate)).collect(Collectors.toList());

        if (sortedStats.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<Stats>> statsByWeaks = groupByWeakNumbers(sortedStats);
        List<AverageStats> averageStatsList = new ArrayList<>(statsByWeaks.size());
        for (Map.Entry<Long, List<Stats>> statsByWeak : statsByWeaks.entrySet()) {
            Long weakNumber = statsByWeak.getKey();
            List<Stats> stats = statsByWeak.getValue();
            AverageStats averageStats = new AverageStats();
            double totalDistance = totalDistance(stats);
            averageStats.setTitle(title(weakNumber, stats));
            averageStats.setTotalDistance(totalDistance);
            averageStats.setAverageTime(averageTime(stats));
            averageStats.setAverageSpeed(averageSpeed(totalDistance, stats));
            averageStatsList.add(averageStats);
        }
        return averageStatsList;
    }

    private Map<Long, List<Stats>> groupByWeakNumbers(List<Stats> sortedStats) {
        Date firstDate = sortedStats.stream().findFirst().get().getDate();
        Map<Long, List<Stats>> statsByWeaks = new TreeMap<>();
        int dayOfWeak = DateUtil.fromMonday(DateUtil.dayOfWeek(firstDate));
        Date weakBegin = DateUtil.resetTimeDown(DateUtil.addDays(firstDate, -dayOfWeak + 1));
        for (Stats stats : sortedStats) {
            long weakNumber = DateUtil.weakDifference(weakBegin, stats.getDate()) + 1L;
            List<Stats> weakStats = statsByWeaks.get(weakNumber);

            if (weakStats == null) {
                weakStats = new ArrayList<>();
                statsByWeaks.put(weakNumber, weakStats);
            }

            weakStats.add(stats);
        }
        return statsByWeaks;
    }

    private String title(long weakNumber, List<Stats> stats) {
        Date firstDate = stats.stream().findFirst().get().getDate();
        Date lastDate = stats.get(stats.size() - 1).getDate();
        return new StringBuilder()
                .append("Weak ")
                .append(weakNumber)
                .append(": (")
                .append(DateUtil.dateToString(firstDate, dateFromat))
                .append(" / ")
                .append(DateUtil.dateToString(lastDate, dateFromat))
                .append(")").toString();
    }

    private Double averageTime(List<Stats> stats) {
        return stats.stream().mapToDouble(Stats::getTime).average().getAsDouble();
    }

    private Double averageSpeed(double totalDistance, List<Stats> stats) {
        return totalDistance / totalTime(stats);
    }

    private Double totalDistance(List<Stats> stats) {
        return stats.stream().mapToDouble(Stats::getDistance).sum();
    }

    private Double totalTime(List<Stats> stats) {
        return stats.stream().mapToDouble(Stats::getTime).sum();
    }
}
