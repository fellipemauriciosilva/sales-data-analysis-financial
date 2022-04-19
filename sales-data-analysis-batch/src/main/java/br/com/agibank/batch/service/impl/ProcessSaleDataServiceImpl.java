package br.com.agibank.batch.service.impl;

import static br.com.agibank.batch.util.Constants.EXTENSION;
import static br.com.agibank.batch.util.Constants.EXTENSION_DONE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.agibank.batch.domain.Report;
import br.com.agibank.batch.service.ProcessSaleDataService;;

@Service
public class ProcessSaleDataServiceImpl implements ProcessSaleDataService {

    @Override
    public String readAndValidationFile(String filePath) throws Exception {
        validateExtension(filePath);

        final Path pathCreated = Paths.get(filePath);
        return Files.readString(pathCreated);
    }
    
    @Override
    public void writeAndValidationDataFile(List<? extends Report> reports,String pathWriter,String fileName) throws IOException {

        String dataSave =
        		reports.stream()
                        .map(c-> {
                            return String.format("Número de clientes no arquivo de entrada : %s ",c.getQtdCustomers())
                                    .concat("\n")
                                    .concat(String.format("Número de Vendedores no arquivo de entrada : %s ", c.getQtdSeller()))
                                    .concat("\n")
                                    .concat(String.format("Id da venda mais cara : %s", c.getIdSales()))
                                    .concat("\n")
                                    .concat(String.format("Pior Vendedor : %s ", c.getSeller()));
                        })
                        .findFirst()
                        .get();


        Path path = Paths.get(pathWriter.concat(fileName).replace(EXTENSION, EXTENSION_DONE));
        Files.write(path, dataSave.getBytes());
    }
    
    
    private void validateExtension(String fileName) throws Exception {

        if(!(fileName.toLowerCase().endsWith(EXTENSION)))
            throw new Exception("Invalid format, only allowed " + EXTENSION + " file extension");

    }
	
}
