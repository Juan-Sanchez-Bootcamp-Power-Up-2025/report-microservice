package co.com.crediya.report.model.report.gateways;

import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.message.LoanMessage;
import reactor.core.publisher.Mono;

public interface ReportRepository {

    Mono<Report> getReport();

    Mono<Void> incrementApprovedLoansCount(LoanMessage loanMessage);

}
