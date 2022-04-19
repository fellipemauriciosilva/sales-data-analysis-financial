package br.com.agibank.sale.service;

import br.com.agibank.sale.domain.Report;

public interface ProcessSaleDataService {

   public Report processAndValidate(String readFile);
	
}
