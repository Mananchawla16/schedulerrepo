package com.test.pavedroad.intcollabsnotification.core.model;

import com.test.pavedroad.intcollabsnotification.core.services.ScheduleJobRequestHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ScheduleTriggerRequest extends Trigger implements JobRequest {
    @Override
    public Class<? extends JobRequestHandler<ScheduleTriggerRequest>> getJobRequestHandler() {
        return ScheduleJobRequestHandler.class;
    }
}
