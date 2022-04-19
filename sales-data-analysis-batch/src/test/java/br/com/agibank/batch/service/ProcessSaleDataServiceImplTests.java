package br.com.agibank.batch.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.agibank.batch.fixture.DataFileFixture;
import br.com.agibank.batch.service.impl.ProcessSaleDataServiceImpl;

@RunWith(SpringRunner.class)
public class ProcessSaleDataServiceImplTests {

	   @MockBean
	   ProcessSaleDataService service;

	    @Rule
	    public TemporaryFolder tempFolder = new TemporaryFolder();

	    @Before
	    public void init(){
	        MockitoAnnotations.initMocks(this);
	    }

	    @Test
	    public void readingFileAndValidationWithSucess() {
	        String message = "Invalid format, only allowed .dat file extension";
	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();
	        try {
	            fileReaderService.readAndValidationFile("arquivo.json");
	        } catch (Exception e) {
	            assertTrue(" Message should be " + message, e.getMessage().equals(message));
	        }
	    }

	    @Test(expected = Exception.class)
	    public void loadDataFileinReadWithException() throws Exception {

	        final File tempFile = tempFolder.newFile("fileTest.txt");
	        Path path = Paths.get(tempFile.toURI());
	        Files.write(path, DataFileFixture.loadDataToFileTests().getBytes());

	        final String s = Files.readString(path);

	        Assert.assertEquals(DataFileFixture.loadDataToFileTests(), s);

	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();

	        String getReturn = fileReaderService.readAndValidationFile("fileTest.txt");

	        Assert.assertTrue(Objects.nonNull(getReturn));
	        assertTrue(getReturn.equals(DataFileFixture.loadDataToFileTests()));
	    }

	    @Test
	    public void loadDataFileinReadWithSucess() throws Exception {

	        final File tempFile = tempFolder.newFile("loadDataFileinReadWithSucess.dat");
	        Path path = Paths.get(tempFile.toURI());
	        Files.write(path, DataFileFixture.loadDataToFileTests().getBytes());
	        final String s = Files.readString(path);

	        Assert.assertEquals(DataFileFixture.loadDataToFileTests(), s);

	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();

	        String getReturn = fileReaderService.readAndValidationFile(path.toAbsolutePath().toString());

	        Assert.assertTrue(Objects.nonNull(getReturn));
	        assertTrue(getReturn.equals(DataFileFixture.loadDataToFileTests()));
	    }

	}
