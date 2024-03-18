package com.test.pavedroad.intcollabsnotification.core.dao;

import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.model.ScheduleIdToJobId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jobrunr.jobs.JobId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleIdToJobIdDao {
    private final MongoTemplate mongoTemplate;

    public ScheduleIdToJobId insertScheduleIdToJobIdMap(String scheduleId, JobId jobId) {
        ScheduleIdToJobId scheduleIdToJobId = ScheduleIdToJobId.builder().scheduleId(scheduleId).jobId(jobId).build();
        ScheduleIdToJobId insertedScheduleIdToJobId = mongoTemplate.insert(scheduleIdToJobId);
        log.info("{} | scheduleIdJobIdMapping | insert | success=true | scheduleId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
        return insertedScheduleIdToJobId;
    }

    public ScheduleIdToJobId updateScheduleIdToJobIdMap(String scheduleId, JobId jobId) throws Exception {
        Query query = Query.query(Criteria.where(ApplicationConstants.MONGO_ID).is(scheduleId));
        Update update = Update.update(ApplicationConstants.JOB_ID, jobId);
        ScheduleIdToJobId updatedScheduleIdToJobId = mongoTemplate.findAndModify(query, update,
                ScheduleIdToJobId.class);
        if (ObjectUtils.isEmpty(updatedScheduleIdToJobId)) {
            log.error("{} | scheduleIdJobIdMapping | findAndModify | success=false | recordNotFound | scheduleId={}",
                    ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
            throw new Exception("Failed to update scheduleId-JobId map for scheduleId=" + scheduleId);
        }
        log.info("{} | scheduleIdJobIdMapping | findAndModify | success=true | scheduleId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
        return updatedScheduleIdToJobId;
    }

    public ScheduleIdToJobId findScheduleId(String scheduleId) throws Exception {
        ScheduleIdToJobId scheduleIdToJobId = mongoTemplate.findById(scheduleId, ScheduleIdToJobId.class);
        if (ObjectUtils.isEmpty(scheduleIdToJobId)) {
            log.error("{} | scheduleIdJobIdMapping | findById | success=false | recordNotFound | scheduleId={}",
                    ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
            throw new Exception("Trigger with scheduleId=" + scheduleId + " not found.");
        }
        log.info("{} | scheduleIdJobIdMapping | findById | success=true | scheduleId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
        return scheduleIdToJobId;
    }

    public ScheduleIdToJobId deleteScheduleId(String scheduleId) throws Exception {
        Query query = Query.query(Criteria.where(ApplicationConstants.MONGO_ID).is(scheduleId));
        ScheduleIdToJobId scheduleIdToJobId = mongoTemplate.findAndRemove(query, ScheduleIdToJobId.class);
        if (ObjectUtils.isEmpty(scheduleIdToJobId)) {
            log.error("{} | scheduleIdJobIdMapping | findAndRemove | success=false | recordNotFound | scheduleId={}",
                    ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
            throw new Exception(
                    "Trigger with scheduleId=" + scheduleId + " can't be deleted as it doesn't exist.");
        }
        log.info("{} | scheduleIdJobIdMapping | findAndRemove | success=true | scheduleId={}",
                ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId);
        return scheduleIdToJobId;
    }
}
