/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */

package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import java.util.Date;

/**
 *
 * Returns current date and time represented in UTC milliseconds encapsulated in
 *  * a {@link Date} object.
 *
 * <h3>Note</h3>
 * <p>We do not encourage use of the legacy {@code Date} class. This is only a simple example.</p>
 * <p>For real code, you are strongly encouraged to use the classes in the {@code java.time} package.</p>
 * @see <a href="https://www.oracle.com/technical-resources/articles/java/jf14-date-time.html">Java SE 8 Date and Time</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html">java.time package summary</a>
 *
 *  @author koppenheim
 */
public interface DateTimeService {

    /**
     * Gets current date and time represented in UTC milliseconds encapsulated
     * in a {@link Date} object.
     *
     * @return current date and time in UTC milliseconds
     */
    public Date getDateTime();
}
