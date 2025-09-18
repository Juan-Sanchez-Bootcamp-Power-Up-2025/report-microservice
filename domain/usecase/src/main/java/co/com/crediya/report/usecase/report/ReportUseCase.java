package co.com.crediya.report.usecase.report;

import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.gateways.ReportRepository;
import co.com.crediya.report.model.report.message.LoanMessage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportRepository reportRepository;

    public Mono<Report> getReport() {
        return reportRepository.getReport();
    }

    public Mono<Void> incrementApprovedLoansCountAndAmount(LoanMessage loanMessage) {
        return reportRepository.incrementApprovedLoansCountAndAmount(loanMessage);
    }

}
