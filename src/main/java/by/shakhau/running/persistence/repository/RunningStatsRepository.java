package by.shakhau.running.persistence.repository;

import by.shakhau.running.persistence.entity.StatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningStatsRepository extends JpaRepository<StatsEntity, Long> {

    List<StatsEntity> findByUserId(Long userId);
}