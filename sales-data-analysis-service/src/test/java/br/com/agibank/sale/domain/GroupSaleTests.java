package br.com.agibank.sale.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import br.com.agibank.sale.exceptions.FileSaleDataException;
import br.com.agibank.sale.fixture.CustomerFixture;
import br.com.agibank.sale.fixture.SaleFixture;
import br.com.agibank.sale.fixture.SaleItemFixture;
import br.com.agibank.sale.fixture.SellerFixture;

public class GroupSaleTests {

    @Test
    public void notAddSaleWithoutSalesman(){
        GroupSale groupSale = new GroupSale();
        Sale sale = SaleFixture.getOneSaleWithOneItem();

        Sale saleNoMan = SaleFixture.getONeSaleWithoutSalesMan();

        try {
            groupSale.addSale(sale);
            assertTrue(groupSale.getSales().size() == 1);
            groupSale.addSale(saleNoMan);
        } catch (FileSaleDataException e) {
            assertTrue(e.getClass().equals(FileSaleDataException.class));
        }
    }

    @Test
    public void notAddEqualSalesMan() {
        GroupSale groupSale = new GroupSale();

        Seller salesman2 = SellerFixture.getOneSalesManWithNameSalesMan2();

        groupSale.addSeller(salesman2);

        assertTrue(groupSale.getSellers().size() == 1);

        Seller salesman2New = SellerFixture.getOneSalesManWithNameSalesMan2();

        groupSale.addSeller(salesman2New);

        assertTrue(groupSale.getSellers().size() == 1);

        Seller salesman3 = SellerFixture.getOneSalesManWithNameSalesMan3();

        groupSale.addSeller(salesman3);

        assertTrue(groupSale.getSellers().size() == 2);
    }

    @Test
    public void setSalesmanToSaleAddingSaleman() throws FileSaleDataException {
        GroupSale groupSale = new GroupSale();
        groupSale.addSale(
                SaleFixture.getOneSaleWithObjectSalesMan()
        );

        groupSale.addSale(
                SaleFixture.getOneSaleWithItensTotalSix()
        );

        Seller salesman2 = SellerFixture.getOneSalesManWithNameSalesMan2();

        groupSale.addSeller(salesman2);

        Sale sale = groupSale.getSales().get(0);
        assertTrue(sale.getSeller().getName().equals("SalesMan 1"));
        assertTrue(sale.getSeller().getSalesAmount().doubleValue() == 7.65);

        sale = groupSale.getSales().get(1);
        assertTrue(sale.getSeller().getName().equals("SalesMan 2"));
        assertTrue(sale.getSeller().getSalesAmount().doubleValue() == 6);
    }

    @Test
    public void notSetSalesmanToSale() throws FileSaleDataException {
        GroupSale groupSale = new GroupSale();
        groupSale.addSale(
        		Sale.builder()
                        .id(1)
                        .seller(SellerFixture.getOneSalesManWithNameSalesMan())
                        .items(Arrays.asList(SaleItemFixture.getOneSaleItem()))
                        .build()
        );

        groupSale.addSale(
        		Sale.builder()
                        .id(1)
                        .sellerName("SalesMan 2")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(2.55))
                                                .quantity(3)
                                                .build()
                                )
                        )
                        .build()
        );

        Seller salesman3 = SellerFixture.getOneSalesManWithNameSalesMan3();

        groupSale.addSeller(salesman3);

        Sale sale = groupSale.getSales().get(0);
        assertTrue(sale.getSeller().getName().equals("SalesMan 1"));

        sale = groupSale.getSales().get(1);
        assertTrue(sale.getSeller() == null);
    }

    @Test
    public void notAddEqualCustomer(){
        GroupSale groupSale = new GroupSale();
        groupSale.addCustomer(CustomerFixture.getOneCustomerByNameOne());

        assertTrue(groupSale.getCustomers().size() == 1);

        groupSale.addCustomer(CustomerFixture.getOneCustomerByNameOne());

        assertTrue(groupSale.getCustomers().size() == 1);

        groupSale.addCustomer(CustomerFixture.getOneCustomerByNameTwo());
        assertTrue(groupSale.getCustomers().size() == 2);
    }

    @Test
    public void setSalesmanToSaleAddingSale() throws FileSaleDataException{
        GroupSale groupSale = new GroupSale();

        Seller salesman1 = SellerFixture.getOneSalesManWithNameSalesMan();
        groupSale.addSeller(salesman1);

        Seller salesman2 = SellerFixture.getOneSalesManWithNameSalesMan2();
        groupSale.addSeller(salesman2);

        assertTrue(groupSale.getSellers().size() == 2);

        Sale noMansSale = Sale.builder()
                .id(1)
                .sellerName("Any Salesman")
                .items(
                        Arrays.asList(
                                SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(3)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(noMansSale);
        Sale selected = groupSale.getSales().get(0);
        assertTrue(groupSale.getSales().size() == 1);
        assertTrue(selected.getSellerName().equals("Any Salesman"));
        assertTrue(selected.getSeller() == null);

        Sale noVendedor1 = Sale.builder()
                .id(1)
                .sellerName("SalesMan 1")
                .items(
                        Arrays.asList(
                               SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(3)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(noVendedor1);
        selected = groupSale.getSales().get(1);
        assertTrue(groupSale.getSales().size() == 2);
        assertTrue(selected.getSellerName().equals("SalesMan 1"));
        assertTrue(selected.getSeller() != null);
        assertTrue(selected.getSeller().getCpf().equals("2345"));
        assertTrue(selected.getSeller().getSalesAmount().doubleValue() == 7.65);

        selected = groupSale.getSales().get(0);
        assertTrue(selected.getSellerName().equals("Any Salesman"));
        assertTrue(selected.getSeller() == null);
    }

    @Test
    public void validateTotalOfSales() throws FileSaleDataException {
        GroupSale groupSale = new GroupSale();
        groupSale.addSale(
        		Sale.builder()
                        .id(2)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(5))
                                                .quantity(8)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(3))
                                                .quantity(4)
                                                .build()
                                )
                        )
                        .build()
        );

        groupSale.addSale(
        		Sale.builder()
                        .id(1)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(10))
                                                .quantity(6)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(2))
                                                .quantity(5)
                                                .build()
                                )
                        )
                        .build()
        );

        groupSale.addSale(
        		Sale.builder()
                        .id(3)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(1))
                                                .quantity(7)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(2))
                                                .quantity(3)
                                                .build()
                                )
                        )
                        .build()
        );

        assertTrue(groupSale.getSales().get(0).getTotal().doubleValue() == 52);
        assertTrue(groupSale.getSales().get(1).getTotal().doubleValue() == 70);
        assertTrue(groupSale.getSales().get(2).getTotal().doubleValue() == 13);
    }

    @Test
    public void validateTheGreaterSale() throws FileSaleDataException {
        GroupSale groupSale = new GroupSale();
        groupSale.addSale(
        		Sale.builder()
                        .id(1)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(5))
                                                .quantity(7)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(3))
                                                .quantity(4)
                                                .build()
                                )
                        )
                        .build()
        );

        groupSale.addSale(
        		Sale.builder()
                        .id(2)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(10))
                                                .quantity(8)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(2))
                                                .quantity(5)
                                                .build()
                                )
                        )
                        .build()
        );

        groupSale.addSale(
                Sale.builder()
                        .id(3)
                        .sellerName("SalesMan")
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(1))
                                                .quantity(6)
                                                .build(),
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(2))
                                                .quantity(4)
                                                .build()
                                )
                        )
                        .build()
        );

        Sale bestSale = groupSale.getGreaterSale();
        assertTrue(bestSale.getId().equals(2));
    }

    @Test
    public void validateTheWorstSalesman() throws FileSaleDataException{
        GroupSale groupSale = new GroupSale();

        Seller salesman1 = Seller.builder()
                .cpf("2345")
                .name("SalesMan 1")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
        groupSale.addSeller(salesman1);

        Seller salesman2 = Seller.builder()
                .cpf("12345")
                .name("SalesMan 2")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
        groupSale.addSeller(salesman2);

        assertTrue(groupSale.getSellers().size() == 2);

        Sale sell1 = Sale.builder()
                .id(1)
                .seller(salesman1)
                .items(
                        Arrays.asList(
                                SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(3)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(sell1);

        Sale sell2 = Sale.builder()
                .id(1)
                .seller(salesman2)
                .items(
                        Arrays.asList(
                                SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(1)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(sell2);

        Seller worst = groupSale.getWorstSeller();

        assertTrue(worst.getName().equals("SalesMan 2"));
        assertTrue(worst.getSalesAmount().doubleValue() == 2.55);
    }

    @Test
    public void validateWorstSalesmanWithZero() throws FileSaleDataException{
        GroupSale groupSale = new GroupSale();

        Seller salesman1 = Seller.builder()
                .cpf("2345")
                .name("SalesMan 1")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
        groupSale.addSeller(salesman1);

        Seller salesman2 = Seller.builder()
                .cpf("12345")
                .name("SalesMan 2")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
        groupSale.addSeller(salesman2);

        Seller salesman3 = Seller.builder()
                .cpf("34567")
                .name("SalesMan 3")
                .salary(BigDecimal.valueOf(1234.56))
                .build();
        groupSale.addSeller(salesman3);

        assertTrue(groupSale.getSellers().size() == 3);

        Sale sell1 = Sale.builder()
                .id(1)
                .seller(salesman1)
                .items(
                        Arrays.asList(
                                SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(3)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(sell1);

        Sale sell2 = Sale.builder()
                .id(1)
                .seller(salesman2)
                .items(
                        Arrays.asList(
                                SaleItem.builder()
                                        .id(1)
                                        .price(BigDecimal.valueOf(2.55))
                                        .quantity(1)
                                        .build()
                        )
                )
                .build();

        groupSale.addSale(sell2);

        Seller worst = groupSale.getWorstSeller();

        assertTrue(worst.getName().equals("SalesMan 3"));
        assertTrue(worst.getSalesAmount().doubleValue() == 0);
    }
	
}
