package co.com.crediya.report.consumer;

import co.com.crediya.report.model.email.Email;
import co.com.crediya.report.model.email.gateways.AdminDirectoryGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements AdminDirectoryGateway {

    private final WebClient client;

    @Override
    public Mono<Email> getAdminEmails() {
        return client.get()
                .uri(uri -> uri.path("/api/v1/users/admin-emails")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.debug("Authentication service 4xx");
                    return Mono.error(new RuntimeException("Admin emails not found"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.debug("Authentication service 5xx");
                    return Mono.error(new RuntimeException("Authentication service not available"));
                })
                .bodyToMono(Email.class)
                .doOnSuccess(ok -> log.debug("Admin emails retrieved from auth microservice"));
    }
}
