package br.com.agibank.sale.fixture;

import java.util.Arrays;

import br.com.agibank.sale.domain.Sale;

public class SaleFixture {
	
    public static Sale getOneSaleWithOneItem(){
        return Sale.builder()
                .id(1)
                .sellerName("SalesMan")
                .items(
                        Arrays.asList(
                                SaleItemFixture.getOneSaleItem()
                        )
                )
                .build();
    }

    public static Sale getONeSaleWithoutSalesMan(){
        return Sale.builder()
                .id(1)
                .items(
                        Arrays.asList(
                                SaleItemFixture.getOneSaleItem()
                        )
                )
                .build();
    }

    public static Sale getOneSaleWithObjectSalesMan(){
        return Sale.builder()
                .id(1)
                .seller(SellerFixture.getOneSalesManWithNameSalesMan())
                .items(
                        Arrays.asList(
                                SaleItemFixture.getOneSaleItem()
                        )
                )
                .build();
    }

    public static Sale getOneSaleWithItensTotalSix(){
        return Sale.builder()
                .id(1)
                .sellerName("SalesMan 2")
                .items(
                        Arrays.asList(
                                SaleItemFixture.getOneSaleItemTotalSix()
                        )
                )
                .build();
    }

}
