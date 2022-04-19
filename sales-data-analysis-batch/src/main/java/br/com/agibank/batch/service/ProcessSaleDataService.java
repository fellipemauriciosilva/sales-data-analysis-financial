package br.com.agibank.batch.service;

import java.io.IOException;
import java.util.List;

import br.com.agibank.batch.domain.Report;

public interface ProcessSaleDataService {

    public String readAndValidationFile(String filePath) throws Exception;
	
    public void writeAndValidationDataFile(List<? extends Report> reports,String pathWriter,String fileName) throws IOException;

}
