package br.com.agibank.sale.fixture;

import br.com.agibank.sale.domain.Customer;

public class CustomerFixture {

    public static Customer getOneCustomerByNameOne(){
        return Customer.builder()
                .name("Cliente 1")
                .businessArea("IT")
                .cnpj("123456")
                .build();
    }

    public static Customer getOneCustomerByNameTwo(){
        return Customer.builder()
                .name("Cliente 2")
                .businessArea("IT")
                .cnpj("123456")
                .build();
    }
	
}
