package com.test.pavedroad.intcollabsnotification.core.producer;

import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.model.Trigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "dispatcher.eventbus.enabled", havingValue = "true")
public class EventbusDispatcher<T extends Trigger, U> implements
        TriggerDispatcher<T, U> {


    @Override
    public void publishTrigger(T payload, U config) {
        // send data to kafka or to rest client
        log.info(
                "{} | EventbusClient | EventbusTriggerPublish | success=true | scheduleId={}, publishedAt={}, " +
                        "scheduleId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, payload.getScheduleId(), Instant.now().toEpochMilli(),
                payload.getScheduleId());
    }
}
