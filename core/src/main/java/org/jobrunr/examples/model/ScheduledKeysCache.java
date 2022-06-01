package org.jobrunr.examples.model;

import org.jobrunr.jobs.JobId;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ngupta16
 * @createdAt 25/05/22
 **/
public class ScheduledKeysCache {

    /*Map : scheduleId - jobId */
    //should map have list<jobId> ?
    private static final Map<String, JobId> SCHEDULED_KEYS = new HashMap<>();

    public static boolean addRecord(String scheduleId, JobId jobId) {
        SCHEDULED_KEYS.put(scheduleId, jobId);
        return true;
    }

    public static JobId getJobId(String scheduleId) {
        return SCHEDULED_KEYS.get(scheduleId);
    }
}
