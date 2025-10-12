package co.com.crediya.report.scheduling;

import co.com.crediya.report.usecase.dailyreport.DailyReportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyEmailScheduler {

    private final DailyReportUseCase dailyReportUseCase;

    @Scheduled(cron = "${entrypoint.ses.daily.cron}", zone = "${entrypoint.ses.daily.timezone}")
    public void sendDailyEmail() {
        dailyReportUseCase.sendDailyReport()
                .doOnSubscribe(subscription -> log.debug(">> Sending SES daily report - start"))
                .doOnSuccess(success -> log.info("Daily report sent to admin users"))
                .doOnError(error -> log.error("Error sending daily report", error))
                .subscribe();
    }

}
