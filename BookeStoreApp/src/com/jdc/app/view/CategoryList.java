package com.jdc.app.view;

import java.io.File;
import java.util.List;

import com.jdc.app.entity.Category;
import com.jdc.app.service.CategoryService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CategoryList {
	@FXML
	private TextField name;
	@FXML
	private FlowPane categoryBoxHolder;
	
	private CategoryService categoryService;

	public void add() {
		Category c = new Category();
		c.setName(name.getText().trim());
		categoryService.add(c);
		search();
	}
	
	public void upload() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Upload Category");
		fc.setSelectedExtensionFilter(new ExtensionFilter("Category", "*.txt","*.csv","*.tsv"));
		File file = fc.showOpenDialog(name.getScene().getWindow());
		if(file != null) {
			categoryService.upload(file);
			search();
		}
	}
	
	public void search() {
		categoryBoxHolder.getChildren().clear();
		List<Category> cList = categoryService.findByName(name.getText().trim());
		/*
		cList.stream().map(c-> new CategoryBox(c))
					  .forEach(c -> categoryBoxHolder.getChildren().add(c));
		*/
		cList.stream().map(CategoryBox::new)
					  .forEach(categoryBoxHolder.getChildren()::addAll);
	}
	
	private class CategoryBox extends HBox {
		CategoryBox(Category c) {
			getStyleClass().add("category-box");
			Label categoryName = new Label(c.getName());
			getChildren().add(categoryName);
		}
	}
	
	public void clear() {
		name.clear();
		categoryBoxHolder.getChildren().clear();
	}
	
	@FXML
	private void initialize() {
		categoryService = categoryService.getInstance();
		name.textProperty().addListener( (a,b,c) -> search());
	
		name.setOnKeyPressed( e -> {
			if(e.getCode().equals(KeyCode.ENTER)) {
				add();
			}
		});
		
	}
}
