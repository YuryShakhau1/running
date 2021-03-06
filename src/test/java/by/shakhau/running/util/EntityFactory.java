package by.shakhau.running.util;

import by.shakhau.running.persistence.entity.RoleEntity;
import by.shakhau.running.persistence.entity.StatsEntity;
import by.shakhau.running.persistence.entity.UserEntity;

import java.util.Collections;
import java.util.Date;

public final class EntityFactory {

    private EntityFactory() {}

    private static final Long ID = 1L;
    private static final Date DATE = new Date();

    public static Long getId() {
        return ID;
    }

    public static Date getDate() {
        return DATE;
    }

    public static RoleEntity getRole() {
        RoleEntity role = new RoleEntity();
        role.setId(getId());
        role.setName("ROLE_RoleName");
        return role;
    }

    public static UserEntity getUser() {
        UserEntity user = new UserEntity();
        user.setId(getId());
        user.setName("UserName");
        user.setPassword("Password");
        user.setRoles(Collections.singletonList(getRole()));
        return user;
    }

    public static StatsEntity getStats() {
        StatsEntity stats = new StatsEntity();
        stats.setId(getId());
        stats.setDate(getDate());
        stats.setDistance(1.0D);
        stats.setTime(10.0D);
        stats.setUser(getUser());
        return stats;
    }
}