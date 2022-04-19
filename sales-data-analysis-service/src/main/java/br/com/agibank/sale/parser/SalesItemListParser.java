package br.com.agibank.sale.parser;

import java.util.ArrayList;
import java.util.List;

import br.com.agibank.sale.domain.SaleItem;
import br.com.agibank.sale.exceptions.InvalidFileDataSizeException;

public class SalesItemListParser {
	
	private static final String SEPARATOR = ",";

	public static List<SaleItem> parse(String line) throws InvalidFileDataSizeException {

		line = line.replace("[", "").replace("]", "");

		List<SaleItem> itens = new ArrayList<>();

		String[] data = line.split(SEPARATOR);

		for (String item : data) {
			itens.add(SaleItemParser.parse(item));
		}

		return itens;
	}

}
