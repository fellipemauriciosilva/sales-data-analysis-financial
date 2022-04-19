package br.com.agibank.batch.watcher.consumer;

import static br.com.agibank.batch.util.Constants.HOMEPATH;
import static br.com.agibank.batch.util.Constants.JOB;
import static br.com.agibank.batch.util.Constants.JOBLAUNCHER;

import java.util.UUID;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.agibank.batch.domain.Event;
import br.com.agibank.batch.helper.JobParameterBuilderHelper;

@Component
public class FileConsumer implements BiConsumer<Event, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConsumer.class);

    @Autowired
    @Qualifier(JOB)
    private Job jobSalesDataAnalysis;

    @Autowired
    @Qualifier(JOBLAUNCHER)
    private JobLauncher jobLauncherSalesDataAnalysis;

    @Value("${file-watcher.pathWriter}")
    private String pathWriter;

    @Override
    public void accept(Event event, String s) {

        final UUID uuid = UUID.randomUUID();
        final JobParameters jobParameters = JobParameterBuilderHelper.create(event, uuid,System.getProperty(HOMEPATH).concat(pathWriter));

        try {
            LOGGER.info("Consumer receives the FileWatcher file and calls the JOB");

            jobLauncherSalesDataAnalysis.run(jobSalesDataAnalysis, jobParameters);

        } catch (final Exception e) {
            LOGGER.error("m=accept, watcherEvent={}, uuid={}", event, uuid.toString(), e);
        }

    }
}
