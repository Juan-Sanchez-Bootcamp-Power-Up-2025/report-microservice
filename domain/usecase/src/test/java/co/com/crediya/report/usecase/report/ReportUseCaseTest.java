package co.com.crediya.report.usecase.report;

import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.gateways.ReportRepository;
import co.com.crediya.report.model.report.message.LoanMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ReportUseCaseTest {

    @Mock
    private ReportRepository reportRepository;

    private ReportUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReportUseCase(reportRepository);
    }

    @Test
    void getReport_returnsReport_onSuccess() {
        Report expected = Report.builder()
                .metricKey("total_approved_loans")
                .approvedLoansCount(5L)
                .approvedLoansAmount(new BigDecimal("1000000"))
                .build();

        when(reportRepository.getReport()).thenReturn(Mono.just(expected));

        StepVerifier.create(useCase.getReport())
                .expectNext(expected)
                .verifyComplete();

        verify(reportRepository, times(1)).getReport();
        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void getReport_propagatesError() {
        RuntimeException boom = new RuntimeException("ddb down");
        when(reportRepository.getReport()).thenReturn(Mono.error(boom));

        StepVerifier.create(useCase.getReport())
                .expectErrorMatches(e -> e == boom)
                .verify();

        verify(reportRepository).getReport();
        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void incrementApprovedLoansCountAndAmount_callsRepositoryWithMessage_andCompletes() {
        LoanMessage message = LoanMessage.builder()
                .amount(new BigDecimal("1500.00"))
                .status("APPROVED")
                .build();

        when(reportRepository.incrementApprovedLoansCountAndAmount(any(LoanMessage.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.incrementApprovedLoansCountAndAmount(message))
                .verifyComplete();

        ArgumentCaptor<LoanMessage> captor = ArgumentCaptor.forClass(LoanMessage.class);
        verify(reportRepository).incrementApprovedLoansCountAndAmount(captor.capture());
        assertThat(captor.getValue()).isEqualTo(message);

        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void incrementApprovedLoansCountAndAmount_propagatesError() {
        LoanMessage message = LoanMessage.builder()
                .amount(new BigDecimal("1500.00"))
                .status("APPROVED")
                .build();

        IllegalStateException boom = new IllegalStateException("update failed");
        when(reportRepository.incrementApprovedLoansCountAndAmount(message))
                .thenReturn(Mono.error(boom));

        StepVerifier.create(useCase.incrementApprovedLoansCountAndAmount(message))
                .expectErrorMatches(e -> e == boom)
                .verify();

        verify(reportRepository).incrementApprovedLoansCountAndAmount(message);
        verifyNoMoreInteractions(reportRepository);
    }

}
