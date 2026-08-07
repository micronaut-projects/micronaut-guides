package example.micronaut;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        if (LOG.isInfoEnabled()) {
            LOG.info("The time is now {}", DATE_FORMAT.format(new Date()));
        }
    }
}