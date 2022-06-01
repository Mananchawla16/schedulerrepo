package org.jobrunr.examples.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jobrunr.examples.util.OinpJobRequestHandler;
import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

/**
 * @author ngupta16
 * @createdAt 23/05/22
 **/
@Getter
@NoArgsConstructor
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
