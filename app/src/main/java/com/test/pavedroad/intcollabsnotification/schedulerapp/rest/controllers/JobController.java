package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;


import java.time.ZonedDateTime;
import java.util.function.Consumer;

import com.test.pavedroad.intcollabsnotification.core.config.TriggerMapper;
import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.dao.ScheduleIdToJobIdDao;
import com.test.pavedroad.intcollabsnotification.core.model.*;
import com.test.pavedroad.intcollabsnotification.core.services.MyServiceInterface;


import io.swagger.v3.oas.annotations.Operation;

import lombok.extern.slf4j.Slf4j;

import org.jobrunr.jobs.JobId;
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


    @GetMapping(value = "/schedule-trigger", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> scheduleTrigger(@Valid @RequestBody ScheduleTriggerDto scheduleTriggerDto) {
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

}
