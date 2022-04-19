package br.com.agibank.batch.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobSalesDataAnalysisStepListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobSalesDataAnalysisStepListener.class);

    @Override
    public void beforeJob(final JobExecution jobExecution) {
    	JobSalesDataAnalysisStepListener.LOG.info("salesDataAnalysisFileStep - Step started: {}", jobExecution);
        super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
    	JobSalesDataAnalysisStepListener.LOG.info("salesDataAnalysisFileStep - Step ended: {}", jobExecution);
        super.afterJob(jobExecution);
    }

}
