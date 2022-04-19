package br.com.agibank.sale.domain;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SaleItem {

    private Integer id;
    private Integer quantity;
    private BigDecimal price;
	
    public BigDecimal valueSales() {
        return Optional.of(price)
                .map(c -> c.multiply(BigDecimal.valueOf(quantity)))
                .orElse(BigDecimal.ZERO);
    }
}
