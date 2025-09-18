package co.com.crediya.report.dynamodb;

import co.com.crediya.report.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.gateways.ReportRepository;
import co.com.crediya.report.model.report.message.LoanMessage;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;

@Slf4j
@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<
        Report,
        String,
        ReportEntity>
        implements ReportRepository {

    private final String metricKey;

    public DynamoDBTemplateAdapter(
            DynamoDbEnhancedAsyncClient connectionFactory,
            ObjectMapper mapper,
            @Value("${adapters.aws.dynamodb.tableName}") String tableName,
            @Value("${adapters.aws.dynamodb.metricKey}") String metricKey
    ) {
        super(connectionFactory, mapper, entity -> mapper.map(entity, Report.class), tableName);
        this.metricKey = metricKey;
    }

    @Override
    public Mono<Report> getReport() {
        log.debug("Querying report");
        return this.getById(metricKey);
    }

    @Override
    public Mono<Void> incrementApprovedLoansCount(LoanMessage loanMessage) {
        log.debug("Updating count of approved loans");
        return getReport()
                .flatMap(report -> {
                    report.setApprovedLoansCount(report.getApprovedLoansCount() + 1);
                    report.setApprovedLoansAmount(report.getApprovedLoansAmount().add(loanMessage.amount()));
                    log.info("[DDB] New approvedLoansCount = {}", report.getApprovedLoansCount());
                    log.info("[DDB] New approvedLoansAmount = {}", report.getApprovedLoansAmount());
                    return this.save(report);
                }).then();
    }

}
