package com.test.pavedroad.intcollabsnotification.core.model;

import org.jobrunr.jobs.JobId;

import java.util.HashMap;
import java.util.Map;

public class ScheduledKeysCache {

    /*Map : scheduleId - jobId */
    //should map have list<jobId> ? //based on recurring we'll need to decide
    private static final Map<String, JobId> SCHEDULED_KEYS = new HashMap<>();

    public static boolean addRecord(String scheduleId, JobId jobId) {
        SCHEDULED_KEYS.put(scheduleId, jobId);
        return true;
    }

    public static JobId getJobId(String scheduleId) {
        return SCHEDULED_KEYS.get(scheduleId);
    }
}
