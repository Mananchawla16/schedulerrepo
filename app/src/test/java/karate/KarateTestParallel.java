package karate;

import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateStats;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.SchedulerWebApplication;

import cucumber.api.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import test.ServerStart;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author ktamilvanan
 * @since January 23, 2020 in version 0.7.2
 */
@CucumberOptions(plugin = {"pretty", "html:target/surefire-reports"}, tags = {"~@ignore"})
@EnableAutoConfiguration
public class KarateTestParallel {
    public static final String[] TEST_ARGS = new String[]{
            "--spring.profiles.active=local,default",
            "--server.port=0"
    };
    private static ServerStart server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        String env = System.getProperty("karate.env");
        if (env == null || env.equalsIgnoreCase("local")) {
            server = new ServerStart(SchedulerWebApplication.class);
            server.start(TEST_ARGS, false);
            System.setProperty("karate.server.port", server.getPort() + "");
        }
    }

    @AfterClass
    public static void afterClass() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testParallel() {
        String karateOutputPath = "target/surefire-reports";
        KarateStats stats = CucumberRunner.parallel(getClass(), 5, karateOutputPath);
        generateReport(karateOutputPath);
        assertTrue("there are scenario failures", stats.getFailCount() == 0);
    }

    private static void generateReport(String karateOutputPath) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        for (File file : jsonFiles) {
            jsonPaths.add(file.getAbsolutePath());
        }
        Configuration config = new Configuration(new File("target"), "Metrics");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }
}