package com.andymur.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;

@Configuration
@EnableSqs
public class AwsSQSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.sqs.endpoint}")
    private String endpoint;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    @Primary
    AmazonSQSAsync amazonSQSAsync() {
        AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder =
                AmazonSQSAsyncClientBuilder.standard();

        BasicAWSCredentials basicAWSCredentials =
                new BasicAWSCredentials(accessKey, secretKey);

        amazonSQSAsyncClientBuilder.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region));
        amazonSQSAsyncClientBuilder.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));

        return amazonSQSAsyncClientBuilder.build();
    }
}