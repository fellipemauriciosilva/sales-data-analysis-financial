package br.com.agibank.sale.parser;

import java.math.BigDecimal;

import br.com.agibank.sale.domain.SaleItem;
import br.com.agibank.sale.exceptions.InvalidFileDataSizeException;

public class SaleItemParser {

    private static final String SEPARATOR = "-";
    private static final int MAX_DATA_LENGTH = 3;

    public static SaleItem parse(String line) throws InvalidFileDataSizeException {
        String[] data = line.split(SEPARATOR);

        if(data.length != MAX_DATA_LENGTH){
            throw new InvalidFileDataSizeException("Sale item data size must be 4");
        }

        SaleItem item = SaleItem.builder()
                .id(Integer.valueOf(data[0]))
                .quantity(Integer.parseInt(data[1]))
                .price(new BigDecimal(data[2]))
                .build();

        return item;
    }
	
}
