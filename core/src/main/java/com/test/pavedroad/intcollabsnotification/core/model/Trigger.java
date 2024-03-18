package com.test.pavedroad.intcollabsnotification.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trigger {
    /**
     * scheduleId of trigger
     */
    @EqualsAndHashCode.Include
    private String scheduleId;

    /**
     * time when trigger has to be enqueued
     */
    private String triggerAt;

    /**
     * grace period after which trigger shouldn't be executed
     */
    private String gracePeriod;

    /**
     * payload associated with trigger
     */
    private String triggerData;

    /**
     * dispatcher config name
     */
    private String dispatcherConfig;
}
