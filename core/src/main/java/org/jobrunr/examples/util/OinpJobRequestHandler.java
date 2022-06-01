package org.jobrunr.examples.util;

import org.jobrunr.examples.model.OinpJobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Component;

/**
 * @author ngupta16
 * @createdAt 23/05/22
 **/
@Component
public class OinpJobRequestHandler implements JobRequestHandler<OinpJobRequest> {

    @Override
    public void run(OinpJobRequest jobRequest) throws Exception {
        //gracetime can also be handled here
     //use OinpJobRequest here to actually process that request - let's say send data to kafka or something
        //Test
        System.out.printf("Sending " + jobRequest.getScheduleId() + " to Kafka%n");
    }
}
