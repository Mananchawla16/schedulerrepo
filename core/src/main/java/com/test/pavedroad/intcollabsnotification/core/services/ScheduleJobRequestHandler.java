package com.test.pavedroad.intcollabsnotification.core.services;

import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.model.ScheduleTriggerRequest;
import com.test.pavedroad.intcollabsnotification.core.util.JobHandlerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleJobRequestHandler implements JobRequestHandler<ScheduleTriggerRequest> {
    private final JobHandlerUtils jobHandlerUtils;

    @Override
    public void run(ScheduleTriggerRequest triggerRequest) {
        String timeNow = ApplicationConstants.DATE_TIME_FORMATTER.format(ZonedDateTime.now());
        ZonedDateTime zonedTimeNow = ZonedDateTime.parse(timeNow, ApplicationConstants.DATE_TIME_FORMATTER);

        boolean trigger = jobHandlerUtils.checkValidityAsPerGracePeriod(triggerRequest.getScheduleId(),
                triggerRequest.getTriggerAt(), triggerRequest.getGracePeriod(), zonedTimeNow);
        if (trigger) {
            log.info("{} | scheduleTrigger | Processing triggerRequest scheduleId={}, triggerAt={}, triggeredAt={}",
                    ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, triggerRequest.getScheduleId(),
                    triggerRequest.getTriggerAt(), timeNow);
            jobHandlerUtils.pickBeanAndPushDataToDispatcher(triggerRequest);
            log.info("{} | scheduleTrigger | triggerSuccess=true | scheduleId={}",
                    ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, triggerRequest.getScheduleId());
        }
    }
}
