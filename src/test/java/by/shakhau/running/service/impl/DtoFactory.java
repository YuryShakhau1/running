package by.shakhau.running.service.impl;

import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;

import java.util.Collections;
import java.util.Date;

public final class DtoFactory {

    private DtoFactory() {}

    private static final Long ID = EntityFactory.getId();
    private static final Date DATE = EntityFactory.getDate();

    public static Long getId() {
        return ID;
    }

    public static Date getDate() {
        return DATE;
    }

    public static Role getRole() {
        Role role = new Role();
        role.setId(getId());
        role.setName("RoleName");
        return role;
    }

    public static User getUser() {
        User user = new User();
        user.setId(getId());
        user.setName("UserName");
        user.setPassword("Password");
        user.setRoles(Collections.singletonList(getRole()));
        return user;
    }

    public static Stats getStats() {
        Stats stats = new Stats();
        stats.setId(getId());
        stats.setDate(getDate());
        stats.setDistance(1.0D);
        stats.setTime(10.0D);
        stats.setUser(getUser());
        return stats;
    }

    public static AverageStats getAverageStats() {
        Stats stats = getStats();
        AverageStats averageStats = new AverageStats();
        averageStats.setTitle("AverageStatsTitle");
        averageStats.setTotalDistance(stats.getDistance() + stats.getDistance());
        averageStats.setAverageTime(stats.getTime());
        averageStats.setAverageSpeed(stats.getDistance() / stats.getTime());
        return averageStats;
    }
}