package com.andymur;

import com.andymur.configuration.SqsTestConfig;
import com.andymur.messaging.Sender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@DirtiesContext
@TestPropertySource(properties = {
        "cloud.aws.sqs.listener.auto-startup = true"
})
@Import(SqsTestConfig.class)
public class SqsIT {
    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");
    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(SQS);
    /**
     * Just configures Localstack's SQS server endpoint in the application
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.sqs.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(SQS).toString());
    }

    @Autowired
    SqsAsyncClient amazonSQS;

    @Autowired
    private Sender sender;

    @Value("${qname}")
    private String qname;

    @Test
    public void test() {
        amazonSQS.createQueue(CreateQueueRequest.builder().queueName(qname).build());
        sender.send(qname);
    }
}
