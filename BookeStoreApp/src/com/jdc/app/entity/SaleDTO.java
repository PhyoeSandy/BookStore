package com.jdc.app.entity;

import java.util.List;

public class SaleDTO {

	private Sale sale;
	private List<SaleDetails> saleDetails;

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public List<SaleDetails> getSaleDetails() {
		return saleDetails;
	}

	public void setSaleDetails(List<SaleDetails> saleDetails) {
		this.saleDetails = saleDetails;
	}

}
