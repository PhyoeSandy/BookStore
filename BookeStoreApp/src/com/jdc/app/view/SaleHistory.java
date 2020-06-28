package com.jdc.app.view;

import java.util.List;

import com.jdc.app.entity.Category;
import com.jdc.app.entity.SaleDetails;
import com.jdc.app.service.CategoryService;
import com.jdc.app.service.SaleService;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SaleHistory {
	
	@FXML
	private ComboBox<Category> category;
	@FXML
	private TextField book;
	@FXML
	private DatePicker dateFrom;
	@FXML
	private DatePicker dateTo;
	@FXML
	private TableView<SaleDetails> tblList;
	
	private CategoryService categoryService;
	private SaleService saleService;
	
	public void search() {
		tblList.getItems().clear();
		List<SaleDetails> sdList = saleService.findSaleDetails(category.getValue(), 
					book.getText().trim(), dateFrom.getValue(), dateTo.getValue(),0);
		tblList.getItems().addAll(sdList);
		
	}
	
	public void clear() {
		category.setValue(null);
		category.setPromptText("Choose Category");

		book.clear();
		dateFrom.setValue(null);
		dateTo.setValue(null);
		
	}
	
	@FXML
	private void initialize() {
		saleService = saleService.getInstance();
		categoryService = categoryService.getInstance();
		
		// default value
		category.getItems().addAll(categoryService.findByName(null));
		
		// observe
		category.valueProperty().addListener( (observable,newValue,oldValue) -> search());
		book.textProperty().addListener( (observable,newValue,oldValue) -> search());
		dateFrom.valueProperty().addListener( (observable,newValue,oldValue) -> search());
		dateTo.valueProperty().addListener( (observable,newValue,oldValue) -> search());
	}
}
