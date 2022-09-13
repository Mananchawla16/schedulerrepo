package com.intuit.pavedroad.intcollabsnotification.core.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import com.intuit.pavedroad.intcollabsnotification.core.model.OinpJobRequest;
import com.intuit.pavedroad.intcollabsnotification.core.model.ScheduledKeysCache;

import lombok.extern.slf4j.Slf4j;

import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.springframework.stereotype.Component;

/**
 * @author ngupta16
 * @createdAt 23/05/22
 **/
@Component
@Slf4j
public class OinpJobRequestHandler implements JobRequestHandler<OinpJobRequest> {

    @Override
    public void run(OinpJobRequest jobRequest) throws Exception {
        //gracetime can also be handled here
        //writing to audit table can also be handled here
     //use OinpJobRequest here to actually process that request - let's say send data to kafka or something
        //Test
        log.info("Sending " + jobRequest + " to Kafka%n");
        TimeUnit.SECONDS.sleep(1);
        long timeNow = System.currentTimeMillis();
        log.info("Sent successfully to Kafka at" + timeNow + "%n");
        ZonedDateTime zonedDateTime = OffsetDateTime.parse(jobRequest.getTriggerAt()).atZoneSimilarLocal(ZoneId.of("Asia" +
                "/Kolkata"));
        jobRequest.setTriggerAt("2022-08-15T12:10:00+01:00"); //do some calculations here based on repeatCound and
        // Interval
        final JobId scheduledJobId = BackgroundJobRequest.schedule(zonedDateTime, jobRequest);
        ScheduledKeysCache.addRecord(jobRequest.getScheduleId(), scheduledJobId);
    }
}
