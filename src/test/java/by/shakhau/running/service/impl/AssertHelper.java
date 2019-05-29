package by.shakhau.running.service.impl;

import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertHelper {

    private AssertHelper() {}

    public static void assertStats(List<Stats> stats, List<Stats> statsFound) {
        assertThat(stats.size()).isEqualTo(statsFound.size());
        for (int i = 0; i < stats.size(); i++) {
            Stats st = stats.get(i);
            Stats stFound = statsFound.get(i);

            assertStats(st, stFound);
        }
    }

    public static void assertStats(Stats stats, Stats statsFound) {
        assertThat(stats.getId()).isEqualTo(statsFound.getId());
        assertThat(stats.getDate()).isEqualTo(statsFound.getDate());
        assertThat(stats.getDistance()).isEqualTo(statsFound.getDistance());
        assertThat(stats.getTime()).isEqualTo(statsFound.getTime());
        assertUsers(stats.getUser(), statsFound.getUser());
    }

    public static void assertUsers(User user, User userFound) {
        assertThat(user.getId()).isEqualTo(userFound.getId());
        assertThat(user.getName()).isEqualTo(userFound.getName());

        List<Role> roles = user.getRoles();
        List<Role> rolesFound = userFound.getRoles();
        assertThat(roles.size()).isEqualTo(rolesFound.size());
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            Role roleFound = rolesFound.get(i);
            assertRoles(role, roleFound);
        }
    }

    public static void assertRoles(Role role, Role roleFound) {
        assertThat(role.getId()).isEqualTo(roleFound.getId());
        assertThat(role.getName()).isEqualTo(roleFound.getName());
    }
}
