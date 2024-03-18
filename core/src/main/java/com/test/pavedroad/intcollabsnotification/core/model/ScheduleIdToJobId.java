package com.test.pavedroad.intcollabsnotification.core.model;

import lombok.Builder;
import lombok.Data;
import org.jobrunr.jobs.JobId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("jobScheduleMapping")
public class ScheduleIdToJobId {
    @Id
    private String scheduleId;
    private JobId jobId;
}
