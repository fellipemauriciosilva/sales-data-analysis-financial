package br.com.agibank.sale.parser;

import br.com.agibank.sale.domain.Customer;
import br.com.agibank.sale.exceptions.InvalidFileDataSizeException;
import static br.com.agibank.sale.utils.Constants.TYPE_CUSTOMER;
import static br.com.agibank.sale.utils.Constants.DELIMITER;

public class CustomerParser {

	private static final int MAX_DATA_LENGTH = 4;

	public static Customer parse(String line) throws Exception {

		if (!line.startsWith(TYPE_CUSTOMER))
			throw new Exception("The line must start with " + TYPE_CUSTOMER);

		String[] data = line.split(DELIMITER);

		if (data.length != MAX_DATA_LENGTH) {
			throw new InvalidFileDataSizeException("Customer data size must be 4: " + line);
		}

		Customer customer = Customer.builder()
				.cnpj(data[1])
				.name(data[2])
				.businessArea(data[3])
				.build();

		return customer;
	}

}
