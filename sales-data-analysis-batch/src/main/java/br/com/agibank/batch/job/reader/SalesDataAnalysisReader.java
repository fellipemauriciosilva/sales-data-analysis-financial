package br.com.agibank.batch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.agibank.batch.service.ProcessSaleDataService;

@Component
@StepScope
public class SalesDataAnalysisReader implements ItemReader<String> {

    private static final Logger LOG = LoggerFactory.getLogger(SalesDataAnalysisReader.class);

    @Autowired
    private StepParameters stepParameters;

    @Autowired
    private ProcessSaleDataService service;

    private boolean processed;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!processed) {

            LOG.info("ProcessAnalyzerReader - Getting Started ItemReader - processed = {}", processed);

            String read = service.readAndValidationFile(stepParameters.getFilePath());

            LOG.info("ProcessAnalyzerReader - File reading in FileWatcher ==> {}", stepParameters.getFilePath());
            processed = true;

            return read;
        }
        return null;
    }
}
