package br.com.agibank.sale.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Sale {

    private Integer id;
    private List<SaleItem> items;
    private String sellerName;
    private Seller seller;
    private BigDecimal total;
	
    public void updateTotal() {
        this.setTotal(new BigDecimal(0));

        BigDecimal sum = new BigDecimal(0);
        for (SaleItem item: items) {
            sum = sum.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        setTotal(sum);
    }    
    
    public BigDecimal getTotalValue() {
        return items
                .stream()
                .map(c-> c.valueSales())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
