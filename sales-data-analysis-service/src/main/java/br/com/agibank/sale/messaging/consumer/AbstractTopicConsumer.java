package br.com.agibank.sale.messaging.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import br.com.agibank.sale.messaging.processor.TopicProcessor;

public abstract class AbstractTopicConsumer<T> implements AcknowledgingMessageListener<String, T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TopicProcessor<T> processor;
    private final String topicGroup;

    public AbstractTopicConsumer(TopicProcessor<T> processor, String topicGroup) {
        this.topicGroup = topicGroup;
        this.processor = processor;
    }

    public void consume(ConsumerRecord<String, T> data, Acknowledgment acknowledgment) {
        T event = null;
        try {
            event = data.value();
            logger.info("Kafka consumer >>> TopicGroup: {} - Topic: {} - Partition: {} - Offset: {} - Key: {} - Payload: {}", topicGroup, data.topic(), data.partition(), data.offset(), data.key(), event);
            processor.process(event);
        } catch (Exception e) {
            logger.error("Kafka CONSUMER_ERROR >>> {}", event);
            logger.error("Kafka CONSUMER_ERROR >>> Error :", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }

}