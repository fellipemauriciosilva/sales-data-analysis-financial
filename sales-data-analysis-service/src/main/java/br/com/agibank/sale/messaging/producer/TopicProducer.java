package br.com.agibank.sale.messaging.producer;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessagingException;


public class TopicProducer<T> {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, T> producer;
    private final String topicOut;

    public TopicProducer(KafkaTemplate<String, T> producer, String topicOut) {
        this.producer = producer;
        this.topicOut = topicOut;
    }

    public Optional<SendResult<String, T>> produce(T body) {
        String key = (String) body;
        logger.info("Kafka producer >>> Topic: {} - Key: {} - Payload: {}", topicOut, key, body);
        SendResult<String, T> result = send(key, body);
        logger.info("Kafka producer >>> Sent {}", result);
        return Optional.ofNullable(result);
    }

    private SendResult<String, T> send(String key, T body) {
        SendResult<String, T> result;
        try {
            result = producer.send(topicOut, key, body).get();
            logger.info("Message event produced");
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error producing message", e);
            throw new MessagingException("Error producing message to kafka", e);
        }
        return result;
    }

}
