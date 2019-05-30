package by.shakhau.running.rest;

import by.shakhau.running.service.RunningStatsService;
import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Stats;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.AssertHelper;
import by.shakhau.running.util.DtoFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatsResourceTest extends ResourceTest {

    @MockBean
    private RunningStatsService runningStatsService;

    @Test
    @WithUserDetails("testUser")
    public void testFindAll() throws Exception {
        Stats st = createStats();
        List<Stats> stats = Arrays.asList(st);

        when(runningStatsService.findAll()).thenReturn(stats);

        List<Stats> statsFound = getMapper().readValue(getMockMvc()
                .perform(get("/running/stats/all"))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<Stats>>() {});

        AssertHelper.assertStats(stats, statsFound);
    }

    @Test
    @WithUserDetails("testUser")
    public void testFindById() throws Exception {
        Stats stats = createStats();

        when(runningStatsService.findById(stats.getId())).thenReturn(stats);

        Stats statsFound = getMapper().readValue(getMockMvc()
                .perform(get("/running/stats/" + stats.getUser().getId()))
                .andReturn().getResponse().getContentAsString(), Stats.class);

        AssertHelper.assertStats(stats, statsFound);
    }

    @Test
    @WithUserDetails("testUser")
    public void testAverage() throws Exception {
        Stats stats = createStats();
        List<AverageStats> averageStats = Collections.singletonList(DtoFactory.getAverageStats());

        when(runningStatsService.averageStats(stats.getUser().getId())).thenReturn(averageStats);

        List<AverageStats> averageStatsCreated = getMapper().readValue(getMockMvc()
                .perform(get("/running/stats/average/user/" + stats.getUser().getId()))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<AverageStats>>() {});

        AverageStats averageStatsToCmp = averageStats.stream().findFirst().get();
        for (AverageStats averageSt : averageStatsCreated) {
            assertThat(averageSt.getTitle()).isEqualTo(averageStatsToCmp.getTitle());
            assertThat(averageSt.getAverageSpeed()).isEqualTo(averageStatsToCmp.getAverageSpeed());
            assertThat(averageSt.getTotalDistance()).isEqualTo(averageStatsToCmp.getTotalDistance());
            assertThat(averageSt.getAverageTime()).isEqualTo(averageStatsToCmp.getAverageTime());
        }
    }

    @Test
    @WithUserDetails("testUser")
    public void testAdd() throws Exception {
        Stats stats = createStats();
        User user = stats.getUser();

        when(runningStatsService.add(any(), any())).thenReturn(stats);

        Stats statsAdded = getMapper().readValue(getMockMvc()
                .perform(post("/running/stats/add/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(stats)))
                .andReturn().getResponse().getContentAsString(), Stats.class);

        AssertHelper.assertStats(stats, statsAdded);
    }

    @Test
    @WithUserDetails("testUser")
    public void testUpdate() throws Exception {
        Stats stats = createStats();

        when(runningStatsService.update(any())).thenReturn(stats);

        Stats statsUpdated = getMapper().readValue(getMockMvc()
                .perform(put("/running/stats/update")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(stats)))
                .andReturn().getResponse().getContentAsString(), Stats.class);

        AssertHelper.assertStats(stats, statsUpdated);
    }

    @Test
    @WithUserDetails("testUser")
    public void testDelete() throws Exception {
        Stats stats = createStats();

        getMockMvc()
                .perform(delete("/running/stats/" + stats.getId()))
                .andExpect(status().isOk());

        verify(runningStatsService, times(1)).delete(stats.getId());
    }

    private Stats createStats() {
        User user = DtoFactory.getUser();
        user.setPassword(null);
        Stats stats = DtoFactory.getStats();
        stats.setUser(user);
        return stats;
    }
}