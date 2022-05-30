/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.intuit.pavedroad.intcollabsnotification.schedulerapp.models;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
public class Document implements Serializable {
    private static final long serialVersionUID = -6626485332327240995L;
    private String id;
    private Date createdDate;
    private Date lastModifiedDate;
    private String userName;
    private String content;
}
