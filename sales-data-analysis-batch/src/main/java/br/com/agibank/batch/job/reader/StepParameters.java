package br.com.agibank.batch.job.reader;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class StepParameters {

    @Value("#{jobParameters['filePath']}")
    private String filePath;

    @Value("#{jobParameters['event']}")
    private String event;

    @Value("#{jobParameters['fileUuid']}")
    private String fileUuid;

    @Value("#{jobParameters['fileLength']}")
    private Long fileLength;

    @Value("#{jobParameters['jobExecutionDate']}")
    private Date jobExecutionDate;

    @Value("#{jobParameters['pathFiles']}")
    private String path;

    @Value("#{jobParameters['pathFilesWriter']}")
    private String pathWriter;

    @Value("#{jobParameters['fileName']}")
    private String fileName;

    public String getFilePath() {
        return filePath;
    }

    String getEvent() {
        return event;
    }

    String getFileUuid() {
        return fileUuid;
    }

    Long getFileLength() {
        return fileLength;
    }

    Date getJobExecutionDate() {
        return jobExecutionDate;
    }

    public String getPath() {
        return path;
    }

    public String getPathWriter() {
        return pathWriter;
    }

    public String getFileName() {
        return fileName;
    }
}
