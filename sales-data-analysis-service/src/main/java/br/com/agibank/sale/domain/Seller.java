package br.com.agibank.sale.domain;

import java.math.BigDecimal;

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
public class Seller {

	private String cpf;
	private String name;
	private BigDecimal salary;
	@Builder.Default
	private BigDecimal salesAmount = new BigDecimal(0);

	public void addSalePrice(BigDecimal value) {
		if (salesAmount == null)
			salesAmount = new BigDecimal(0);
		salesAmount = salesAmount.add(value);
	}
}
