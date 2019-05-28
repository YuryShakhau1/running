package by.shakhau.running.service;

import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Stats;

import java.util.List;

public interface RunningStatsService {

    List<Stats> findAll();
    Stats findById(Long id);
    Stats add(Stats stats, Long userId);
    Stats update(Stats stats);
    void delete(Long id);
    List<AverageStats> averageStats(Long userId);
}
