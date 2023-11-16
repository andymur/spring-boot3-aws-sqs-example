package com.andymur.messaging;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private SqsTemplate sqsTemplate;

    @SqsListener(value = "test-queue")
    public void processMessage(String message) {

    }

    public void send(String queueName) {
        sqsTemplate.send(sqsSendOptions -> sqsSendOptions.queue(queueName).payload("test"));
    }

}
