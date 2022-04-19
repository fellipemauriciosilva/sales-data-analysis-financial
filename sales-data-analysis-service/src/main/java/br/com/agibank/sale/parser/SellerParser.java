package br.com.agibank.sale.parser;

import java.math.BigDecimal;

import br.com.agibank.sale.domain.Seller;
import br.com.agibank.sale.exceptions.InvalidFileDataSizeException;
import br.com.agibank.sale.utils.Constants;

public class SellerParser {

    public static Seller parse(String line) throws Exception {

        if(!line.startsWith(Constants.TYPE_SELLER))
            throw new Exception("The line must start with " + Constants.TYPE_SELLER);

        String[] data = line.split(Constants.DELIMITER);

        if(data.length != 4){
            throw new InvalidFileDataSizeException("Seller data size 4");
        }
        
        Seller seller = Seller.builder()
                .cpf(data[1])
                .name(data[2])
                .salary( new BigDecimal(data[3]) )
                .build();

        return seller;
    }

	
}
