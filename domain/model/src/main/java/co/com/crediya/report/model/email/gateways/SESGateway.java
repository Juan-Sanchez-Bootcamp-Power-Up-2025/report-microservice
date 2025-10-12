package co.com.crediya.report.model.email.gateways;

import reactor.core.publisher.Mono;

import java.util.List;

public interface SESGateway {

    Mono<Void> sendEmail(List<String> to, String subject, String body);

}
