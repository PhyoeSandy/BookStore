package com.jdc.app.entity;

import java.io.Serializable;

public class SaleDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Book book;
	private Category category;
	private Author author;
	private Sale sale;
	private int quantity;
	private double unitPrice;
	
	private double total;
	private boolean delete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public String getCategoryName() {
		return category.getName();
	}
	
	public String getAuthorName() {
		return author.getName();
	}
	
	public String getBookName() {
		return book.getName();
	}
	
	public double getSaleTax() {
		return getQuantity() * getUnitPrice() * 0.1;
	}
	
	public double getSubTotal() {
		return getQuantity() * getUnitPrice();
	}
	
	public double getTotal() {
		return getSubTotal() + getSaleTax();
	}

}
