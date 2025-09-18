package co.com.crediya.report.dynamodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.aws.dynamodb")
public record DynamoDBConnectionProperties(
        String entrypoint,
        String endpoint,
        String region,
        String accessKeyId,
        String secretAccessKey
) {}
