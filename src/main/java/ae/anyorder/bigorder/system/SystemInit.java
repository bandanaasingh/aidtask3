package ae.anyorder.bigorder.system;

import ae.anyorder.bigorder.service.SystemPropertyService;
import com.amazonaws.http.IdleConnectionReaper;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * Created by Frank on 4/9/2018.
 */
@Component
public class SystemInit {
    private static final Logger log = Logger.getLogger(SystemInit.class);

    @Autowired
    SystemPropertyService systemPropertyService;

    /* Called after bean construction and all dependency injection. */
    @PostConstruct
    public void initialize(){
        log.info("+++++++++++ Starting the application ++++++++++++++");
        systemPropertyService.preferenceInitializaton();
        log.info("+++++++++++ Prefrences Initialised ++++++++++++++");
    }

    @PreDestroy
    /* Called just before a bean is destroyed */
    public void cleanup(){
        log.info("+++++++++++ Shutting down the application ++++++++++++++++");
        unregisterJDBCDriver();
        closeAWSThreads();
    }

    private void unregisterJDBCDriver() {
        log.info("Unregistering JDBC Driver manually");
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        Driver driver = null;

        // clear drivers
        while(drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);

            } catch (Exception e) {
                // deregistration failed, might want to do something, log at the very least
                log.error("Error occurred while deregistering JDBC Driver AWS Thread", e);
            }
        }

        // MySQL driver leaves around a thread. This static method cleans it up.
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            log.error("Error occurred while cleaning mysql driver abandonded threads", e);
        }
    }

    private void closeAWSThreads() {
        log.info("Closing AWS Threads");
        try {
            IdleConnectionReaper.shutdown();
        } catch (Exception e) {
            log.error("Error occurred while closing AWS Thread", e);
        }
    }
}
