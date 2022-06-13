package com.intuit.pavedroad.intcollabsnotification.core.model;

import com.intuit.pavedroad.intcollabsnotification.core.util.OinpJobRequestHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

/**
 * @author ngupta16
 * @createdAt 23/05/22
 **/
@Getter
@NoArgsConstructor
@ToString
public class OinpJobRequest implements JobRequest {

    private String scheduleId;
    private String triggerAt;
    private String graceTime;
    private String triggerData;

    @Override
    public Class<? extends JobRequestHandler<OinpJobRequest>> getJobRequestHandler() {
        return OinpJobRequestHandler.class;
    }


}