package com.intuit.pavedroad.intcollabsnotification.schedulerapp;

import com.intuit.pavedroad.intcollabsnotification.core.JobRunrStorageConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(JobRunrStorageConfiguration.class)
public class SchedulerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerWebApplication.class, args);
    }
}
