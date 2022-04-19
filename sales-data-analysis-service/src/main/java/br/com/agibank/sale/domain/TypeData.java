package br.com.agibank.sale.domain;

public enum TypeData {

	SELLER("001"),
	CUSTOMER("002"),
	SALES("003");
	
	private final String codeSplit;

	private TypeData(final String codeSplit) {
		this.codeSplit = codeSplit;
	}

	public String getCodeSplit() {
		return codeSplit;
	}
}
