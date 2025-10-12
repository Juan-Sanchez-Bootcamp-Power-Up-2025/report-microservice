package co.com.crediya.report.model.email.gateways;

import co.com.crediya.report.model.email.Email;
import reactor.core.publisher.Mono;

public interface AdminDirectoryGateway {

    Mono<Email> getAdminEmails();

}
