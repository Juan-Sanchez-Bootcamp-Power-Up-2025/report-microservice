package co.com.crediya.report.sqs.listener;

import co.com.crediya.report.model.report.message.LoanMessage;
import co.com.crediya.report.usecase.report.ReportUseCase;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProcessor {

    private final ReportUseCase reportUseCase;

    @SqsListener("${entrypoint.sqs.queueName}")
    public void onMessage(LoanMessage event) {
        log.debug("SQS ApprovedLoanEvent received: {}", event);
        reportUseCase.incrementApprovedLoansCount(event)
                .doOnError(ex -> log.error("Error processing event {}, message will be retried", event, ex))
                .subscribe(); // starts the flow in the usecase
    }

}
