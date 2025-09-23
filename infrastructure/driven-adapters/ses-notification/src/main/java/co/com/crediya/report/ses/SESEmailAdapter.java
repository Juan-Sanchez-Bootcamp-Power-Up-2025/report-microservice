package co.com.crediya.report.ses;

import co.com.crediya.report.model.email.gateways.SESGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESEmailAdapter implements SESGateway {

    private final SesAsyncClient sesAsyncClient;

    @Value("")
    private String from;

    @Override
    public Mono<Void> sendEmail(List<String> to, String subject, String body) {
        Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

        Content subjectContent = Content.builder()
                .data(subject)
                .charset("UTF-8")
                .build();

        Body bodyEmail = Body.builder()
                .text(Content.builder().data(body).charset("UTF-8").build())
                // .html(Content.builder().data(htmlBody).charset("UTF-8").build()) // if html is needed
                .build();

        Message message = Message.builder()
                .subject(subjectContent)
                .body(bodyEmail)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(from)
                .destination(destination)
                .message(message)
                .build();

        return Mono.fromFuture(sesAsyncClient.sendEmail(request))
                .doOnSubscribe(s -> log.debug("Sending daily email through SES to {}", to))
                .doOnSuccess(resp -> log.info("SES sent. messageId={}", resp.messageId()))
                .doOnError(ex -> log.error("Sending saily email through SES failed", ex))
                .then();
    }

}
