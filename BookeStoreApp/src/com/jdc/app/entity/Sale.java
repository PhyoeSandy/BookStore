package com.jdc.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Sale implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private LocalDate saleDate;
	private LocalTime saleTime;
	private double tax;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}

	public LocalTime getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(LocalTime saleTime) {
		this.saleTime = saleTime;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

}
