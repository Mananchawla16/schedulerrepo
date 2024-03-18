package com.test.pavedroad.intcollabsnotification.core.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CancelResponse extends Response {
    private String status;
}
