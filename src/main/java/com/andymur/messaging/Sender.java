package com.andymur.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private QueueMessagingTemplate sqsTemplate;

    @SqsListener(value = "a", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void processMessage(String message) {

    }

    public void send(String queueName) {
        sqsTemplate.convertAndSend(queueName, "");
    }

}
