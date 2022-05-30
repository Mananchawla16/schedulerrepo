/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.intuit.pavedroad.intcollabsnotification.schedulerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SchedulerApplication {

    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(SchedulerApplication.class, args);
    }

    public static void main(String[] args) {
        run(args);  //NOSONAR
    }
}
