package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services;

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
