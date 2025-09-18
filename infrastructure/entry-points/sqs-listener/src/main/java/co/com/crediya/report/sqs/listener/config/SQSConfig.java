package co.com.crediya.report.sqs.listener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SQSConfig {

    @Bean
    public SqsAsyncClient configSqsApprovedLoans(SQSProperties properties, MetricPublisher publisher) {
        return SqsAsyncClient.builder()
                .region(Region.of(properties.region()))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(properties.accessKeyId(), properties.secretAccessKey())))
                .build();
    }

}
