/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.test.pavedroad.intcollabsnotification.schedulerapp.models;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Documents implements Serializable {
    private static final long serialVersionUID = 962497687514167703L;

    private List<Document> documents;

}
