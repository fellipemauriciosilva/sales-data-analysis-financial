package br.com.agibank.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = {"qtdCustomers","qtdSeller","idSales","seller"})
@Builder
public class Report {

    private Integer qtdCustomers;
    private Integer qtdSeller;
    private Integer idSales;
    private String  seller;

}
