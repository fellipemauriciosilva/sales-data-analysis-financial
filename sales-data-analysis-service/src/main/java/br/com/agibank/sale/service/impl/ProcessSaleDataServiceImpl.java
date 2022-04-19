package br.com.agibank.sale.service.impl;

import static br.com.agibank.sale.utils.Constants.DELIMITER;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.agibank.sale.domain.GroupSale;
import br.com.agibank.sale.domain.Report;
import br.com.agibank.sale.domain.TypeData;
import br.com.agibank.sale.parser.CustomerParser;
import br.com.agibank.sale.parser.SaleParser;
import br.com.agibank.sale.parser.SellerParser;
import br.com.agibank.sale.service.ProcessSaleDataService;

@Service
public class ProcessSaleDataServiceImpl implements ProcessSaleDataService {

	@Override
	public Report processAndValidate(String readFile) {
		Stream<String> lines = readFile.lines();

		lines.forEach(line -> {
			String[] parts = line.split(DELIMITER);
			try {
				final String code = parts[0];
				validateCodeFile(code);
				importText(code, line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		final GroupSale instance = GroupSale.getInstance();
		return Report.builder()
				.qtdCustomers(instance.getCustomers().size())
				.qtdSeller(instance.getSellers().size())
				.idSales(instance.getGreaterSale().getId())
				.seller(instance.getWorstSeller() != null ? instance.getWorstSeller().getName() : "N/A")
				.build();
	}

	private void validateCodeFile(String codeFile) throws Exception {
		if (!isCodExists(codeFile)) {
			throw new Exception("Data type of this line is not valid");
		}
	}

	private boolean isCodExists(final String code) {
		return Arrays.asList(TypeData.values())
				.stream()
				.filter(cod -> cod.getCodeSplit().equals(code))
				.findFirst()
				.isPresent();
	}

	private void importText(String code, String line) throws Exception {

		if (TypeData.SELLER.getCodeSplit().equals(code))
			GroupSale.getInstance().addSeller(SellerParser.parse(line));

		if (TypeData.CUSTOMER.getCodeSplit().equals(code))
			GroupSale.getInstance().addCustomer(CustomerParser.parse(line));

		if (TypeData.SALES.getCodeSplit().equals(code))
			GroupSale.getInstance().addSale(SaleParser.parse(line));
	}
}
