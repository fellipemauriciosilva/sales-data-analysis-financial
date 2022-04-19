package br.com.agibank.sale.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.agibank.sale.exceptions.FileSaleDataException;
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
public class GroupSale {

	private List<Seller> sellers;
	private List<Customer> customers;
	private List<Sale> sales;
	private static GroupSale instance = null;

    public GroupSale() {
    	sellers = new ArrayList<>();
        customers = new ArrayList<>();
        sales = new ArrayList<>();
    }
	
	public static GroupSale getInstance() {
		if (instance == null)
			instance = new GroupSale();

		return instance;
	}
	
    public static void closeInstance(){
        instance = null;
    }
    
    
    public void addSeller(Seller seller) {

        for (Seller people : sellers) {
            if (people.equals(seller))
                return;
        }

        sellers.add(seller);

        Sale sale = sales
                .stream()
                .filter((sl) ->
                        sl.getSeller() == null && sl.getSellerName().equals(seller.getName()))
                .findFirst()
                .orElse(null);

        if (sale != null) {
            sale.setSeller(seller);
            sale.updateTotal();
            seller.addSalePrice(sale.getTotal());
        }

    }    
    
    public void addCustomer(Customer customer) {

        for (Customer current : customers) {
            if (current.equals(customer))
                return;
        }

        customers.add(customer);
    }    
    
    public void addSale(Sale sale) throws FileSaleDataException {
        if (sale.getSeller() != null) {
            sale.updateTotal();
            sale.getSeller().addSalePrice(sale.getTotal());
            sale.setSellerName(sale.getSeller().getName());
        } else if (sale.getSellerName() != null && !sale.getSellerName().isEmpty()) {

            Seller seller = sellers
                    .stream()
                    .filter((cur) ->
                            cur.getName().equals(sale.getSellerName()))
                    .findFirst()
                    .orElse(null);

            if (seller != null) {
                sale.setSeller(seller);
                sale.updateTotal();
                seller.addSalePrice(sale.getTotal());
            }

        } else {
            throw new FileSaleDataException("There is no information about seller in this sale");
        }

        sale.updateTotal();
        sales.add(sale);
    }	
	
    public Sale getGreaterSale(){
        Sale sale = this.sales
                .stream()
                .max(Comparator.comparing(Sale::getTotalValue))
                .get();
        return sale;
    }
    
    public Seller getWorstSeller(){

        Seller seller = this.sellers
                .stream()
                .sorted(Comparator.comparing(Seller::getSalesAmount))
                .findFirst()
                .orElse(null);

        return seller;
    }    

}
