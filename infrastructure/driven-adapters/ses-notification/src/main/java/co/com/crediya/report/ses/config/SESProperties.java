package co.com.crediya.report.ses.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.ses")
public record SESProperties(
        String region,
        String accessKeyId,
        String secretAccessKey
) {}
