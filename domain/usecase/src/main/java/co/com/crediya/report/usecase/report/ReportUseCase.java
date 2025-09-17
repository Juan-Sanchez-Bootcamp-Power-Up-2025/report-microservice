package co.com.crediya.report.usecase.report;

import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportUseCase {

    //public final ReportRepository reportRepository;

    public Mono<Report> getReport() {
        //return reportRepository.getReport();
        return Mono.just(Report.builder().build());
    }

    public Mono<Report> incrementApprovedLoansCount() {
        return Mono.just(Report.builder().build());
        //return reportRepository.incrementApprovedLoansCount();
    }

}
