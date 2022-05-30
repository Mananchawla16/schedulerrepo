package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;

public class DateTimeServiceImplTest {
	
    @Test
    public void testDateTimeServiceImpl() {
        DateTimeServiceImpl dateTimeService = new DateTimeServiceImpl();
        Date documentCreationDate = dateTimeService.getDateTime();
        assertFalse(documentCreationDate.after(new Date())); // should be before or equal
    }
}