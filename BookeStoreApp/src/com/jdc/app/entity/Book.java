package com.jdc.app.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String image;
	private double price;
	private LocalDate releasedDate;
	private String remark;
	
	private Category category;
	private Author author;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(LocalDate releasedDate) {
		this.releasedDate = releasedDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	/*
	 * public String getCategoryName() { return category.getName(); }
	 * 
	 * public String getAuthorName() { return author.getName(); }
	 */
	
	@Override
	public String toString() {
		return name;
	}

}
