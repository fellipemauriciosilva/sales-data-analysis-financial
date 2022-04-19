package br.com.agibank.sale.parser;

import java.util.List;

import br.com.agibank.sale.domain.Sale;
import br.com.agibank.sale.domain.SaleItem;
import br.com.agibank.sale.exceptions.InvalidFileDataSizeException;
import static br.com.agibank.sale.utils.Constants.TYPE_SALES;
import static br.com.agibank.sale.utils.Constants.DELIMITER;

public class SaleParser {
	
	private static final int DATA_MAX_LENGTH = 4;
	
    public static Sale parse(String line) throws Exception {

        if(!line.startsWith(TYPE_SALES))
            throw new Exception("The line must start with " + TYPE_SALES);

        String[] data = line.split(DELIMITER);

        if(data.length != DATA_MAX_LENGTH){
            throw new InvalidFileDataSizeException("Sale data size must be 4");
        }

        List<SaleItem> itens = SalesItemListParser.parse(data[2]);

        Sale sale = Sale.builder()
                .id(Integer.valueOf(data[1]))
                .items(itens)
                .sellerName(data[3])
                .build();

        sale.updateTotal();
        return sale;
    }

}
