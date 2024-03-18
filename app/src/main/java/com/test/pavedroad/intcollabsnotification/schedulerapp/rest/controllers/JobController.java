package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.function.Consumer;

import com.test.pavedroad.intcollabsnotification.core.config.TriggerMapper;
import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.dao.ScheduleIdToJobIdDao;
import com.test.pavedroad.intcollabsnotification.core.model.*;
import com.test.pavedroad.intcollabsnotification.core.services.MyService;
import com.test.pavedroad.intcollabsnotification.core.services.MyServiceInterface;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.test.pavedroad.intcollabsnotification.core.dto.ScheduleTriggerDto;

import static java.time.Instant.now;
import org.mapstruct.factory.Mappers;

import javax.validation.Valid;


@RestController
@RequestMapping( "v1/jobs")
@Slf4j
public class JobController {

    private final JobScheduler jobScheduler;
    private final MyServiceInterface myService;
    TriggerMapper triggerMapper = Mappers.getMapper(TriggerMapper.class);

    private final ScheduleIdToJobIdDao scheduleIdToJobIdDao;


    public JobController(JobScheduler jobScheduler, MyServiceInterface myService, ScheduleIdToJobIdDao scheduleIdToJobIdDao) {
        this.jobScheduler = jobScheduler;
        this.myService = myService;
        this.scheduleIdToJobIdDao = scheduleIdToJobIdDao;
    }

    @GetMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public String index() {
        return "Hello World from JobController!<br />" +
                "You can:<br />" +
                "- <a href=\"/jobs/simple-job\">Enqueue a simple job</a><br />" +
                "- <a href=\"/jobs/simple-job-instance\">Enqueue a simple job using a service instance</a><br />" +
                "- <a href=\"/jobs/schedule-simple-job\">Schedule a simple job 3 hours from now using a service instance</a><br />" +
                "- <a href=\"/jobs/long-running-job\">Enqueue a long-running job</a><br />" +
                "- <a href=\"/jobs/long-running-job-with-job-context\">Enqueue a long-running job using a JobContext to log progress</a><br />" +
                "- Learn more on <a href=\"https://www.jobrunr.io/\">www.jobrunr.io</a><br />"
                ;
    }

    @GetMapping(value = "/schedule-trigger", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> simpleJob(@Valid @RequestBody ScheduleTriggerDto scheduleTriggerDto) {
        ScheduleTriggerRequest scheduleTriggerRequest = triggerMapper.mapScheduleTriggerDtoToEntity(scheduleTriggerDto);

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                scheduleTriggerRequest.getTriggerAt(), ApplicationConstants.DATE_TIME_FORMATTER);
        final JobId jobId = BackgroundJobRequest.schedule(zonedDateTime, scheduleTriggerRequest);

        Consumer<ScheduleIdToJobIdDao> doa = scheduleIdToJobIdDao -> scheduleIdToJobIdDao.insertScheduleIdToJobIdMap(
                scheduleTriggerRequest.getScheduleId(), jobId);
        doa.accept(scheduleIdToJobIdDao);

        log.info("{} | scheduleTrigger | Scheduled job having scheduleId={} with jobId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleTriggerRequest.getScheduleId(), jobId);

        TriggerResponse response = new TriggerResponse();
        response.setScheduleId(scheduleTriggerRequest.getScheduleId());
        return ResponseEntity.ok(response);
    }

//    @GetMapping(value = "/simple-job-instance", produces = {MediaType.TEXT_PLAIN_VALUE})
//    public String simpleJobUsingInstance(@RequestParam(value = "name", defaultValue = "World") String name) {
//        final JobId enqueuedJobId = jobScheduler.enqueue(() -> myService.doSimpleJob("Hello " + name));
//        return "Job Enqueued: " + enqueuedJobId;
//    }
//
//    @GetMapping(value = "/schedule-simple-job", produces = {MediaType.TEXT_PLAIN_VALUE})
//    public String scheduleSimpleJob(
//            @RequestParam(value = "value", defaultValue = "Hello world") String value,
//            @RequestParam(value = "when", defaultValue = "PT3H") String when) {
//        final JobId scheduledJobId = jobScheduler.schedule(now().plus(Duration.parse(when)), () -> myService.doSimpleJob(value));
//        return "Job Scheduled: " + scheduledJobId;
//    }
//
//    @GetMapping(value = "/long-running-job", produces = {MediaType.TEXT_PLAIN_VALUE})
//    public String longRunningJob(@RequestParam(value = "name", defaultValue = "World") String name) {
//        final JobId enqueuedJobId = jobScheduler.<MyService>enqueue(myService -> myService.doLongRunningJob("Hello " + name));
//        return "Job Enqueued: " + enqueuedJobId;
//    }
//
//    @GetMapping(value = "/long-running-job-with-job-context", produces = {MediaType.TEXT_PLAIN_VALUE})
//    public String longRunningJobWithJobContext(@RequestParam(value = "name", defaultValue = "World") String name) {
//        final JobId enqueuedJobId = jobScheduler.<MyService>enqueue(myService -> myService.doLongRunningJobWithJobContext("Hello " + name, JobContext.Null));
//        return "Job Enqueued: " + enqueuedJobId;
//    }
//
//    @RequestMapping(value = "/schedule-oinp-job", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
//    @Operation(description = "Schedule a notification for oinp", summary = "Schedule a notification using job request",
//            security = {@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
//    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Bad request") })
//    public String scheduleOINPJob(@RequestBody OinpJobRequest oinpJobRequest) {
//        log.info("Starting request processing");
//        ZonedDateTime zonedDateTime = OffsetDateTime.parse(oinpJobRequest.getTriggerAt()).atZoneSimilarLocal(ZoneId.of("Asia" +
//                "/Kolkata"));
//        //ZonedDateTime can also be used - check if request should also provide zone ?
//        final JobId scheduledJobId = BackgroundJobRequest.schedule(zonedDateTime, oinpJobRequest);
//        ScheduledKeysCache.addRecord(oinpJobRequest.getScheduleId(), scheduledJobId);
//        return "Job Scheduled: " + scheduledJobId;
//    }

    @RequestMapping(value = "/cancel-trigger/{scheduleId}", method = RequestMethod.DELETE)
    @Operation(description = "Cancel a trigger", summary = "Cancel a scheduled trigger")
    public ResponseEntity<Object> killScheduledOINPJob(@PathVariable("scheduleId") String scheduleId) throws Exception {

        ScheduleIdToJobId scheduleIdToJobId = scheduleIdToJobIdDao.findScheduleId(scheduleId);
        BackgroundJobRequest.delete(scheduleIdToJobId.getJobId());
        scheduleIdToJobIdDao.deleteScheduleId(scheduleId);
        CancelResponse response = new CancelResponse();
        response.setScheduleId(scheduleId);
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }


    public void scheduleRecurringOINPJob() {
        //use getRepeatInterval in oinp core for this?
        //or use cron
        //dateAdd('Minutes', 1, $$now); days = dateDiff($$now, d); return days;
        //d = dateAdd('Minutes', 5, $$now); min = dateDiff($$now, d); return min;
        //d = dateAdd('Days', 2, $$now); min = dateDiff($$now, d); return min;
        //d = dateAdd('Days', 5, $$now); min = dateDiff($$now, d); return min;
        //dateAdd('Minutes', 2 - minute($$now) % 2, $$now) ; repeatCount= 3
    }
}
