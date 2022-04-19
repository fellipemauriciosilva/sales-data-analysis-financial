package br.com.agibank.sale.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.agibank.sale.SalesDataAnalysisServiceApplication;
import br.com.agibank.sale.domain.GroupSale;
import br.com.agibank.sale.domain.Report;
import br.com.agibank.sale.fixture.DataFileFixture;
import br.com.agibank.sale.service.ProcessSaleDataService;

@SpringBootTest(classes = SalesDataAnalysisServiceApplication.class)
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
	    public void processingDataAndValidationException() throws Exception {

	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();

	        try {
	            String getReturn = DataFileFixture.loadDataToFileWithNumberNotExistsTests();
	            fileReaderService.processAndValidate(getReturn);
	            GroupSale.closeInstance();
	        } catch (Exception e) {
	            assertTrue(e.getMessage().equals("Data type of this line is not valid"));
	        }
	    }

	    @Test
	    public void processingSucess() throws Exception {

	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();

	        String getReturn = DataFileFixture.loadDataToFileTests();

	        Report result = fileReaderService.processAndValidate(getReturn);

	        GroupSale.closeInstance();

	        assertTrue(result.getQtdSeller() == 2);
	        assertTrue(result.getQtdCustomers() == 2);
	        assertTrue(result.getSeller().equals("Paulo"));
	        assertTrue(result.getIdSales() == 10);

	    }

	    @Test
	    public void processingDataWithSucessWithFourSalesMan() throws Exception {

	        ProcessSaleDataServiceImpl fileReaderService = new ProcessSaleDataServiceImpl();

	        String getReturn = DataFileFixture.loadDataToFileWithFourSalesMan();

	        Report result = fileReaderService.processAndValidate(getReturn);
	        GroupSale.closeInstance();

	        assertTrue(result.getQtdSeller() == 4);
	        assertTrue(result.getQtdCustomers() == 2);
	        assertTrue(result.getSeller().equals("David"));
	        assertTrue(result.getIdSales() == 22);

	    }

	}
