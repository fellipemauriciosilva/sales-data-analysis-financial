package br.com.agibank.sale.messaging.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import br.com.agibank.sale.messaging.processor.SaleDataProcessor;

@Component
public class SaleDataConsumer extends AbstractTopicConsumer<String> {

	@Autowired
	public SaleDataConsumer(SaleDataProcessor processor, @Value("${spring.kafka.consumer.group-id}") String topicGroup) {
		super(processor, topicGroup);
	}
	
	@Override
	@KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.sales.data.in}", containerFactory = "kafkaListenerContainerFactory")
	public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        consume(data, acknowledgment);		
	}

}
