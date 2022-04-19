package br.com.agibank.batch.job.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.agibank.batch.messaging.producer.TopicProducer;

@StepScope
@Component
public class SalesDataAnalysisWriter implements ItemWriter<String> {

    private static final Logger LOG = LoggerFactory.getLogger(SalesDataAnalysisWriter.class);

    @Autowired
	private TopicProducer<String> salaDataReportProducer;

    @Override
    public void write(List<? extends String> rows) throws Exception {

        LOG.info("SalesDataAnalysisWriter - Getting Started ItemWriter");
        
        rows.stream().parallel().forEach((String row) -> {
        	salaDataReportProducer.produce(row);
        });
        
        LOG.info("SalesDataAnalysisWriter - Finishing ItemWriter");

    }

}