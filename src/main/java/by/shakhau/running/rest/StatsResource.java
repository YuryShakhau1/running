package by.shakhau.running.rest;

import by.shakhau.running.service.RunningStatsService;
import by.shakhau.running.service.dto.AverageStats;
import by.shakhau.running.service.dto.Stats;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/running/stats")
public class StatsResource {

    @Autowired
    private RunningStatsService runningStatsService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public List<Stats> findAll() {
        return runningStatsService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public Stats findById(@PathVariable Long id) {
        return runningStatsService.findById(id);
    }

    @GetMapping(value = "/average/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public List<AverageStats> average(@PathVariable Long userId) {
        return runningStatsService.averageStats(userId);
    }

    @PostMapping(value = "/add/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public Stats add(@PathVariable Long userId, @RequestBody Stats stats) {
        return runningStatsService.add(stats, userId);
    }

    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public Stats update(@RequestBody Stats stats) {
        return runningStatsService.update(stats);
    }

    @DeleteMapping(value = "/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    public void delete(@PathVariable Long id) {
        runningStatsService.delete(id);
    }
}
