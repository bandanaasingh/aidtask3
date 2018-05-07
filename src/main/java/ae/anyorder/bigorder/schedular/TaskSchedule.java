package ae.anyorder.bigorder.schedular;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TaskSchedule {
    private static final Logger log = Logger.getLogger(TaskSchedule.class);

    private final String CRON_WEEKLY = "0 0 0 * * 0"; //every sunday
    private final String CRON_FORTNIGTLY = "0 0 0 14 * *"; //every 14th day
    private final String CRON_MONTHLY = "0 0 0 1 * *"; //1st of every month at 00:00
    private final String CRON_DAILY = "0 0 16 * * *"; //Daily at 16:00
    private final String CORN_PER_MINUTE = "0 0/1 * * * *"; // per minute


    @Scheduled(fixedRate = 2000)  ///2000ms
    public void scheduleTaskWithFixedRate() {
        log.info("Fixed Rate Task :: Execution Time - {}");
    }

    @Scheduled(cron = CORN_PER_MINUTE) //minute hour dayOfMonth month dayOfWeek year
    @Transactional
    public void generateWeeklyInvoice() {
        try {
            log.info("Corn Per minute test:");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = CRON_FORTNIGTLY) //minute hour dayOfMonth month dayOfWeek year
    @Transactional
    public void generateFortnightlyInvoice() {
        try {
            log.info("Generating fortnightly invoice:");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
