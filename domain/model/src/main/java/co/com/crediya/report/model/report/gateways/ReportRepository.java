package co.com.crediya.report.model.report.gateways;

import co.com.crediya.report.model.report.Report;
import reactor.core.publisher.Mono;

public interface ReportRepository {

    Mono<Report> getReport();

    Mono<Report> incrementApprovedLoansCount();

}
