package br.com.agibank.batch.job.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.agibank.batch.domain.Report;
import br.com.agibank.batch.job.reader.StepParameters;
import br.com.agibank.batch.service.ProcessSaleDataService;

@StepScope
@Component
public class SalesDataProcessedWriter implements ItemWriter<Report> {

	private static final Logger LOG = LoggerFactory.getLogger(SalesDataProcessedWriter.class);

	@Autowired
	private StepParameters stepParameters;

	@Autowired
	private ProcessSaleDataService service;

	@Override
	public void write(List<? extends Report> reports) throws Exception {

		LOG.info("SalesDataProcessedWriter - Getting Started ItemWriter");

		service.writeAndValidationDataFile(reports, stepParameters.getPathWriter(), stepParameters.getFileName());

		LOG.info("SalesDataProcessedWriter - Finishing ItemWriter");

	}

}