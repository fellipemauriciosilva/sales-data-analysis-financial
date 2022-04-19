package br.com.agibank.sale.fixture;

import java.math.BigDecimal;

import br.com.agibank.sale.domain.Seller;

public class SellerFixture {

    public static Seller getOneSalesManWithNameSalesMan(){

        return Seller.builder()
                .cpf("2345")
                .name("SalesMan 1")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
    }


    public static Seller getOneSalesManWithNameSalesMan2(){

        return Seller.builder()
                .cpf("12345")
                .name("SalesMan 2")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
    }

    public static Seller getOneSalesManWithNameSalesMan3(){

        return Seller.builder()
                .cpf("12345")
                .name("SalesMan 3")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
    }
}
