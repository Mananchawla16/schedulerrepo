/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */

package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Implementation of {@code DateTimeService}.
 *
 * @author koppenheim
 */
@Service
public class DateTimeServiceImpl implements DateTimeService {

    public DateTimeServiceImpl() {
    }

    @Override
    public Date getDateTime() {
        return new Date();
    }
}
