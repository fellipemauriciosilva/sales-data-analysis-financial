package br.com.agibank.batch.config;

import static br.com.agibank.batch.util.Constants.JOB;
import static br.com.agibank.batch.util.Constants.JOBLAUNCHER;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import br.com.agibank.batch.domain.Report;
import br.com.agibank.batch.job.listener.JobSalesDataAnalysisListener;
import br.com.agibank.batch.job.listener.JobSalesDataAnalysisStepListener;
import br.com.agibank.batch.job.reader.SalesDataAnalysisReader;
import br.com.agibank.batch.job.writer.SalesDataAnalysisWriter;
import br.com.agibank.batch.job.writer.SalesDataProcessedWriter;

@Configuration
public class JobSalesDataAnalysisConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobSalesDataAnalysisListener jobSalesDataAnalysisListener;

    @Autowired
    private JobSalesDataAnalysisStepListener jobSalesDataAnalysisStepListener;

    @Value("${file-watcher.chunk.size}")
    private int chunkSize;
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS_CONFIG;

    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID_CONFIG;    
    
    @Value("${spring.kafka.topic.sales.data.out}")
    private String TOPIC_SALES_DATA_OUT;
    
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String MAX_POLL_RECORDS_CONFIG;  

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private String SESSION_TIMEOUT_MS_CONFIG;

    @Value("${spring.kafka.consumer.max.poll.interval.ms}")
    private String MAX_POOL_INTERVAL;
    
    @Bean
    public Properties consumerConfigs() {
        Properties props = new Properties();
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);        
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");        
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS_CONFIG);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIMEOUT_MS_CONFIG);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POOL_INTERVAL);
        
        return props;
    }  
    
    @Bean
    @Qualifier(JOBLAUNCHER)
    public JobLauncher jobLauncherSalesDataAnalysis(final JobRepository jobRepository) throws Exception {
        final SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return simpleJobLauncher;
    }

    @Bean
    @StepScope
    public SalesDataAnalysisReader analysisFileItemReader() {
        return new SalesDataAnalysisReader();
    }
        
    @Bean
    public SalesDataAnalysisWriter analysisFileItemWriter() {
        return new SalesDataAnalysisWriter();
    }  

    @Bean
    KafkaItemReader<String, Report> processedFileItemReader() {
        return new KafkaItemReaderBuilder<String, Report>()
                .partitions(new Integer[] {0,1,2})
                .consumerProperties(consumerConfigs())
                .name("processedFileItemReader")
                .saveState(true)
                .topic(TOPIC_SALES_DATA_OUT)
                .build();
    }
    
    @Bean
    public SalesDataProcessedWriter processedFileItemWriter() {
        return new SalesDataProcessedWriter();
    }
    
    @Bean
    @Qualifier(JOB)
    public Job jobSalesDataAnalysis() throws Exception {
        return jobBuilderFactory.get("stepJobAnalyzer")
                .incrementer(new RunIdIncrementer())
                .listener(jobSalesDataAnalysisListener)
                .flow(salesDataAnalysisFileStep())
                .next(salesSaveDataAnalysisFileStep())
                .end()
                .build();
    }

    @Bean
    public Step salesDataAnalysisFileStep() {
        return stepBuilderFactory.get("salesDataAnalysisFileStep")
                .<String, String>chunk(chunkSize)
                .reader(analysisFileItemReader())
                .writer(analysisFileItemWriter())
                .listener(jobSalesDataAnalysisStepListener)
                .build();
    }
    
    @Bean
    public Step salesSaveDataAnalysisFileStep() {
        return stepBuilderFactory.get("salesSaveDataAnalysisFileStep")
                .<Report, Report>chunk(chunkSize)
                .reader(processedFileItemReader())
                .writer(processedFileItemWriter())
                .listener(jobSalesDataAnalysisStepListener)
                .build();
    }
    	
}
