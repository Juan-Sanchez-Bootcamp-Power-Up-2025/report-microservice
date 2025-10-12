package co.com.crediya.report.ses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesAsyncClient;

@Configuration
public class SESConfig {

    @Bean
    public SesAsyncClient configSesDailyReport(SESProperties properties, MetricPublisher publisher) {
        return SesAsyncClient.builder()
                .region(Region.of(properties.region()))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(properties.accessKeyId(), properties.secretAccessKey())))
                .build();
    }

}
