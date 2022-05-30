/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package karate;

import com.intuit.karate.FileUtils;
import com.intuit.karate.junit4.Karate;
import com.intuit.karate.netty.FeatureServer;
import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(Karate.class)
public class ApiMockRunner extends KarateTestBase {

    @BeforeClass
    public static void beforeClass() {
        log.info("Karate mock setup class called.");

        final File apiMock = FileUtils.getFileRelativeTo(ApiMockRunner.class, "../api-mock.feature");
        final FeatureServer server = FeatureServer.start(apiMock, 0, false, null);
        final int port = server.getPort();

        System.setProperty("karate.mock.port", port + "");

        log.info("Karate mock server started on port " + port);
    }
}