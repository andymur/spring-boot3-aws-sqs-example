package com.andymur.configuration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.cloud.aws.messaging.support.destination.DynamicQueueUrlDestinationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.core.CachingDestinationResolverProxy;
import org.springframework.messaging.core.DestinationResolver;

@TestConfiguration
public class SqsTestConfig {

    /*
     * It is taken from  https://gist.github.com/rponte/8a46133aeca05f07ae49035879a18143
     * It is intended to automatically create a Q on the fly
     * but not really working for some reason
     */

    @Bean
    public BeanPostProcessor simpleMessageListenerContainerPostProcessor(DestinationResolver<String> destinationResolver) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof SimpleMessageListenerContainer container) {
                    container.setDestinationResolver(destinationResolver);
                }
                return bean;
            }
        };
    }

    /**
     * Creates a DynamicQueueUrlDestinationResolver capable of auto-creating
     * a SQS queue in case it does not exist
     */
    @Bean
    public DestinationResolver<String> autoCreateQueueDestinationResolver(
            AmazonSQSAsync sqs,
            @Autowired(required = false) ResourceIdResolver resourceIdResolver) {

        DynamicQueueUrlDestinationResolver autoCreateQueueResolver
                = new DynamicQueueUrlDestinationResolver(sqs, resourceIdResolver);
        autoCreateQueueResolver.setAutoCreate(true);

        return new CachingDestinationResolverProxy<>(autoCreateQueueResolver);
    }

}