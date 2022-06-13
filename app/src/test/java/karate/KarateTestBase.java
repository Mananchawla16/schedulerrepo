package karate;

import com.intuit.karate.junit4.Karate;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.SchedulerWebApplication;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.AfterClass;

/**
 * Created by jsk-initializr.
 */
@Slf4j
@RunWith(Karate.class)
public class KarateTestBase {

    private static KarateParallelRunner runner;

    @BeforeClass
    public static void setUpClass() throws Exception {
        log.info("Karate setUpClass called.");
        runner = new KarateParallelRunner(SchedulerWebApplication.class);
        runner.start(new String[]{"--spring.profiles.active=local", "--server.port=0"}, false);

    }

    @AfterClass
    public static void afterClass() {
        if (runner != null) {
            runner.stop();
        }
    }
}