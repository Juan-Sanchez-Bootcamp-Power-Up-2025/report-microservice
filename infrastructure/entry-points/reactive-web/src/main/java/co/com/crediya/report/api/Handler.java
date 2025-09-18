package co.com.crediya.report.api;

import co.com.crediya.report.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final ReportUseCase reportUseCase;

    public Mono<ServerResponse> listenGETReport(ServerRequest serverRequest) {
        return reportUseCase.getReport()
                .doOnSubscribe(subscription -> log.debug(">> GET /api/v1/reports - start"))
                .flatMap(report ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(report))
                .doOnSuccess(success -> log.info("Report retrieved from dynamodb"))
                .doOnError(error -> log.error("Error retrieving report: {}", error.getMessage()))
                .doFinally(signalType -> log.debug("<< GET /api/v1/reports - end"));
    }

}
