package br.com.agibank.batch.helper;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import br.com.agibank.batch.domain.Event;

public class JobParameterBuilderHelper {

    public static final String FILE_PATH = "filePath";

    public static final String EVENT = "event";

    public static final String FILE_UUID = "fileUuid";

    public static final String FILE_LENGTH = "fileLength";

    public static final String JOB_EXECUTION_DATE = "jobExecutionDate";

    public static final String PATH = "pathFiles";

    public static final String PATH_WRITER = "pathFilesWriter";

    public static final String FILE_NAME = "fileName";

    public static JobParameters create(final Event event, final UUID uuid, final String pathWriter) {
        final File file = event.getPath().toFile();

        return new JobParametersBuilder()
                .addString(JobParameterBuilderHelper.FILE_PATH, file.getAbsolutePath())
                .addString(JobParameterBuilderHelper.PATH, event.getPath().toString())
                .addString(JobParameterBuilderHelper.FILE_NAME, file.getName())
                .addString(JobParameterBuilderHelper.PATH_WRITER, pathWriter)
                .addString(JobParameterBuilderHelper.FILE_UUID, uuid.toString())
                .addString(JobParameterBuilderHelper.EVENT, event.getKind().name())
                .addLong(JobParameterBuilderHelper.FILE_LENGTH, file.length())
                .addDate(JobParameterBuilderHelper.JOB_EXECUTION_DATE, new Date())
                .toJobParameters();
    }
	
}
