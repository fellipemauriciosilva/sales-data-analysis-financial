package br.com.agibank.sale.messaging.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.agibank.sale.domain.GroupSale;
import br.com.agibank.sale.domain.Report;
import br.com.agibank.sale.messaging.producer.TopicProducer;
import br.com.agibank.sale.service.ProcessSaleDataService;

@Component
public class SaleDataProcessor implements TopicProcessor<String> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ProcessSaleDataService service;
	private final TopicProducer<String> salaDataReportProducer;

	@Autowired
	public SaleDataProcessor(ProcessSaleDataService service, TopicProducer<String> salaDataReportProducer) {
		this.service = service;
		this.salaDataReportProducer = salaDataReportProducer;
	}

	@Override
	public void process(String data) {
		
		Report report = null;
		
		try {
			logger.info("Kafka Consumer >>> Processing! {}", data);
			report = service.processAndValidate(data);
			salaDataReportProducer.produce(report.toString());
		} catch (Exception e) {
			logger.error("Kafka PROCESS_ERROR >>> Error :", e);
		} finally {
			GroupSale.closeInstance();
			logger.info("Kafka consumer >>> Processed! {}", report.toString());
		}
	}
}
