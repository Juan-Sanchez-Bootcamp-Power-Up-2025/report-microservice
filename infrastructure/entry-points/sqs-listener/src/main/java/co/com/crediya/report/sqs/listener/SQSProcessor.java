package co.com.crediya.report.sqs.listener;

import co.com.crediya.report.sqs.listener.message.LoanMessage;
import co.com.crediya.report.usecase.report.ReportUseCase;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProcessor {

    private final ReportUseCase reportUseCase;

    @SqsListener("${entrypoint.sqs.queueName}")
    public void onMessage(LoanMessage event) {
        log.debug("SQS ApprovedLoanEvent received: {}", event);
        reportUseCase.incrementApprovedLoansCount()
                .doOnError(ex -> log.error("Error processing event {}, message will be retried", event, ex))
                .subscribe(); // starts the flow in the usecase
    }

}
