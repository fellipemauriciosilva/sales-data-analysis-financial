package br.com.agibank.batch.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;


@Component
public class JobSalesDataAnalysisListener extends JobExecutionListenerSupport {

	private static final Logger LOG = LoggerFactory.getLogger(JobSalesDataAnalysisListener.class);

	@Override
	public void beforeJob(final JobExecution jobExecution) {
		LOG.info("jobSalesDataAnalysis - Batch started: {}", jobExecution);
		super.beforeJob(jobExecution);
	}

	@Override
	public void afterJob(final JobExecution jobExecution) {
		LOG.info("jobSalesDataAnalysis - Batch ended: {}", jobExecution);
		super.afterJob(jobExecution);
	}
}
