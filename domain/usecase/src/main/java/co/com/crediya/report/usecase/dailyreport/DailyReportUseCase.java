package co.com.crediya.report.usecase.dailyreport;

import co.com.crediya.report.model.email.gateways.AdminDirectoryGateway;
import co.com.crediya.report.model.email.gateways.SESGateway;
import co.com.crediya.report.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DailyReportUseCase {

    private final ReportUseCase reportUseCase;

    private final SESGateway sesGateway;

    private final AdminDirectoryGateway adminDirectoryGateway;

    public Mono<Void> sendDailyReport() {
        return adminDirectoryGateway.getAdminEmails()
                .flatMap(emails -> reportUseCase.getReport()
                        .flatMap(report ->
                                sesGateway.sendEmail(emails.getEmails(),
                                        " [CrediYa] Daily Report",
                                        """
                                            Hello,
                                            
                                            Daily report:
                                            - Loans approved: %d
                                            - Total amount: %s 
                                            
                                            Regards,
                                            CrediYa Platform
                                        """.formatted(report.getApprovedLoansCount(), report.getApprovedLoansAmount()))
                        )
                );
    }

}
