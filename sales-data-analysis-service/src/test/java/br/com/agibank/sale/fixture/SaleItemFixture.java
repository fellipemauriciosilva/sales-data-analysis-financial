package br.com.agibank.sale.fixture;

import java.math.BigDecimal;

import br.com.agibank.sale.domain.SaleItem;

public class SaleItemFixture {

    public static SaleItem getOneSaleItem(){
        return SaleItem.builder()
                .id(1)
                .price(BigDecimal.valueOf(2.55))
                .quantity(3)
                .build();
    }

    public static SaleItem getOneSaleItemTotalSix(){
        return SaleItem.builder()
                .id(1)
                .price(BigDecimal.valueOf(2))
                .quantity(3)
                .build();
    }
	
}
