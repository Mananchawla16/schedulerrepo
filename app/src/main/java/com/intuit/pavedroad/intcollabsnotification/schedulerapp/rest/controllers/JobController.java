package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;

import java.time.Duration;
import java.time.OffsetDateTime;

import com.intuit.pavedroad.intcollabsnotification.core.model.OinpJobRequest;
import com.intuit.pavedroad.intcollabsnotification.core.model.ScheduledKeysCache;
import com.intuit.pavedroad.intcollabsnotification.core.services.MyService;
import com.intuit.pavedroad.intcollabsnotification.core.services.MyServiceInterface;
import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.time.Instant.now;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobScheduler jobScheduler;
    private final MyServiceInterface myService;

    public JobController(JobScheduler jobScheduler, MyServiceInterface myService) {
        this.jobScheduler = jobScheduler;
        this.myService = myService;
    }

    @GetMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public String index() {
        return "Hello World from JobController!<br />" +
                "You can:<br />" +
                "- <a href=\"/jobs/simple-job\">Enqueue a simple job</a><br />" +
                "- <a href=\"/jobs/simple-job-instance\">Enqueue a simple job using a service instance</a><br />" +
                "- <a href=\"/jobs/schedule-simple-job\">Schedule a simple job 3 hours from now using a service instance</a><br />" +
                "- <a href=\"/jobs/long-running-job\">Enqueue a long-running job</a><br />" +
                "- <a href=\"/jobs/long-running-job-with-job-context\">Enqueue a long-running job using a JobContext to log progress</a><br />" +
                "- Learn more on <a href=\"https://www.jobrunr.io/\">www.jobrunr.io</a><br />"
                ;
    }

    @GetMapping(value = "/simple-job", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String simpleJob(@RequestParam(value = "name", defaultValue = "World") String name) {
        final JobId enqueuedJobId = jobScheduler.<MyService>enqueue(myService -> myService.doSimpleJob("Hello " + name));
        return "Job Enqueued: " + enqueuedJobId;
    }

    @GetMapping(value = "/simple-job-instance", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String simpleJobUsingInstance(@RequestParam(value = "name", defaultValue = "World") String name) {
        final JobId enqueuedJobId = jobScheduler.enqueue(() -> myService.doSimpleJob("Hello " + name));
        return "Job Enqueued: " + enqueuedJobId;
    }

    @GetMapping(value = "/schedule-simple-job", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String scheduleSimpleJob(
            @RequestParam(value = "value", defaultValue = "Hello world") String value,
            @RequestParam(value = "when", defaultValue = "PT3H") String when) {
        final JobId scheduledJobId = jobScheduler.schedule(now().plus(Duration.parse(when)), () -> myService.doSimpleJob(value));
        return "Job Scheduled: " + scheduledJobId;
    }

    @GetMapping(value = "/long-running-job", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String longRunningJob(@RequestParam(value = "name", defaultValue = "World") String name) {
        final JobId enqueuedJobId = jobScheduler.<MyService>enqueue(myService -> myService.doLongRunningJob("Hello " + name));
        return "Job Enqueued: " + enqueuedJobId;
    }

    @GetMapping(value = "/long-running-job-with-job-context", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String longRunningJobWithJobContext(@RequestParam(value = "name", defaultValue = "World") String name) {
        final JobId enqueuedJobId = jobScheduler.<MyService>enqueue(myService -> myService.doLongRunningJobWithJobContext("Hello " + name, JobContext.Null));
        return "Job Enqueued: " + enqueuedJobId;
    }

    @PostMapping(value = "/schedule-oinp-job", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String scheduleOINPJob(@RequestBody OinpJobRequest oinpJobRequest) {
        //OffsetDateTime offsetDateTime = OffsetDateTime.parse(oinpJobRequest.getTriggerAt()); //this needs to be passed properly
        //ZonedDateTime can also be used - check if request should also provide zone ?
        final JobId scheduledJobId = BackgroundJobRequest.schedule(OffsetDateTime.now().plusSeconds(120), oinpJobRequest);
        ScheduledKeysCache.addRecord(oinpJobRequest.getScheduleId(), scheduledJobId);
        return "Job Scheduled: " + scheduledJobId;
    }

    @RequestMapping(value = "/kill-oinp-job/{scheduleId}", method = RequestMethod.DELETE)
    public void killScheduledOINPJob(@PathVariable("scheduleId") String scheduleId) {
        JobId jobId = ScheduledKeysCache.getJobId(scheduleId);
        BackgroundJobRequest.delete(jobId); //in actual cache we can plan to keep only job's uuid rather than full object
        System.out.println("Job: " + jobId + " deleted successfully");
    }


    public void scheduleRecurringOINPJob() {
        //use getRepeatInterval in oinp core for this?
        //or use cron
        //dateAdd('Minutes', 1, $$now); days = dateDiff($$now, d); return days;
        //d = dateAdd('Minutes', 5, $$now); min = dateDiff($$now, d); return min;
        //d = dateAdd('Days', 2, $$now); min = dateDiff($$now, d); return min;
        //d = dateAdd('Days', 5, $$now); min = dateDiff($$now, d); return min;
        //dateAdd('Minutes', 2 - minute($$now) % 2, $$now) ; repeatCount= 3
    }
}
