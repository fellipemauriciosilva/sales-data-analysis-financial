package br.com.agibank.sale.domain;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import br.com.agibank.sale.exceptions.FileSaleDataException;

public class MapperGroupSaleTests {

    private GroupSale groupSale;

    @Before
    public void setup() throws FileSaleDataException {
        groupSale = new GroupSale();

        groupSale.addSeller(
                Seller.builder()
                        .cpf("2345")
                        .name("SalesMan 1")
                        .salary(BigDecimal.valueOf(1234.56))
                        .build()
        );

        groupSale.addCustomer(
                Customer.builder()
                        .businessArea("IT")
                        .cnpj("12345678909876")
                        .name("Customer")
                        .build()
        );

        groupSale.addSale(
                Sale.builder()
                        .id(1)
                        .seller(Seller.builder()
                                .cpf("2345")
                                .name("Vendedor 1")
                                .salary(BigDecimal.valueOf(1234.56))
                                .build()
                        )
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
    }

    @Test
    public void getSalesmen() {

        assertTrue(groupSale.getSellers().size() == 1);
        Seller salesman = groupSale.getSellers().get(0);
        assertTrue(salesman.getCpf().equals("2345"));
    }

    @Test
    public void addSalesman() {
        assertTrue(groupSale.getSellers().size() == 1);

        groupSale.addSeller(Seller.builder().name("Outro").build());
        assertTrue(groupSale.getSellers().size() == 2);
        Seller salesman = groupSale.getSellers().get(1);
        assertTrue(Objects.isNull(salesman.getCpf()));
    }

    @Test
    public void getCustomers() {

        assertTrue(groupSale.getCustomers().size() == 1);
        Customer customer = groupSale.getCustomers().get(0);
        assertTrue(customer.getCnpj().equals("12345678909876"));

    }

    @Test
    public void addCustomer() {

        assertTrue(groupSale.getCustomers().size() == 1);

        groupSale.addCustomer(Customer.builder().name("Mais um").build());
        assertTrue(groupSale.getCustomers().size() == 2);
        Customer customer = groupSale.getCustomers().get(1);
        assertTrue(Objects.isNull(customer.getCnpj()));
    }

    @Test
    public void getSales() {

        assertTrue(groupSale.getSales().size() == 1);
        Sale sale = groupSale.getSales().get(0);
        assertTrue(sale.getItems().size() == 1);

    }

    @Test
    public void addSale() throws FileSaleDataException {
        assertTrue(groupSale.getSales().size() == 1);

        groupSale.addSale(
                Sale.builder()
                        .id(2)
                        .seller(Seller.builder()
                                .cpf("2345")
                                .name("Vendedor 1")
                                .salary(BigDecimal.valueOf(1234.56))
                                .build()
                        )
                        .items(
                                Arrays.asList(
                                        SaleItem.builder()
                                                .id(1)
                                                .price(BigDecimal.valueOf(5))
                                                .quantity(7)
                                                .build()
                                )
                        )
                        .build()
        );

        assertTrue(groupSale.getSales().size() == 2);
        Sale sale = groupSale.getSales().get(1);
        assertTrue(sale.getItems().size() == 1);
        assertTrue(sale.getId() == 2);
    }

}
