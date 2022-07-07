package com.intuit.pavedroad.intcollabsnotification.core.util;

import java.util.concurrent.TimeUnit;

import com.intuit.pavedroad.intcollabsnotification.core.model.OinpJobRequest;

import lombok.extern.slf4j.Slf4j;

import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
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
    }
}
