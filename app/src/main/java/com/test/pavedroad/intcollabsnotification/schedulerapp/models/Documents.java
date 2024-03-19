
package com.test.pavedroad.intcollabsnotification.schedulerapp.models;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Documents implements Serializable {
    private static final long serialVersionUID = 962497687514167703L;

    private List<Document> documents;

}
