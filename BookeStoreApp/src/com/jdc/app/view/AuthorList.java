package com.jdc.app.view;

import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.service.AuthorService;
import com.jdc.app.util.IntColumnConverter;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

public class AuthorList {

	@FXML
	private TextField name;
	@FXML
	private TextField age;
	@FXML
	private TextField country;
	@FXML
	private TableView<Author> tblList;
	@FXML
	private TableColumn<Author, String> nameCol;
	@FXML
	private TableColumn<Author, Integer> ageCol;
	@FXML
	private TableColumn<Author, String> countryCol;
	
	private AuthorService authorService;

	public void add() {
		Author a = new Author();
		
		if(!name.getText().isEmpty())
			a.setName(name.getText().trim());
		
		if(!age.getText().isEmpty())
			a.setAge(Integer.parseInt(age.getText()));

		if(!country.getText().isEmpty())
			a.setCountry(country.getText().trim());
		
		authorService.add(a);
		clear(); 
		search();
	}

	public void search() {
		tblList.getItems().clear();
		int age = this.age.getText().isEmpty() ? 0 : Integer.parseInt(this.age.getText());
		List<Author> aList = authorService.findByParams(name.getText().trim(),
				age, country.getText().trim());
		tblList.getItems().addAll(aList);
	}

	public void clear() {
		name.clear();
		age.clear();
		country.clear();
	}
	
	@FXML
	private void initialize() {
		authorService = authorService.getInstance();
		
		name.textProperty().addListener( (a,b,c) -> search());
		country.textProperty().addListener( (a,b,c) -> search());
		
		age.textProperty().addListener( (observable, oldValue, newValue) -> {
			 if (!newValue.matches("\\d*")) {
		            age.setText(newValue.replaceAll("[^\\d]", ""));
		     }
			 search();
		});
		
		// edit columns
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(e -> {
			Author data = e.getRowValue();
			data.setName(e.getNewValue());
			authorService.update(data);
			search();
		});
		
		countryCol.setCellFactory(TextFieldTableCell.forTableColumn());
		countryCol.setOnEditCommit(e -> {
			Author data = e.getRowValue();
			data.setCountry(e.getNewValue());
			authorService.update(data);
			search();
		});
		
		ageCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntColumnConverter()));
		ageCol.setOnEditCommit(e -> {
			Author data = e.getRowValue();
			data.setAge(e.getNewValue());
			authorService.update(data);
			search();
		});
		
		
		// create menu
		MenuItem delete = new MenuItem("Delete");
		tblList.setContextMenu(new ContextMenu(delete));
		delete.setOnAction(e -> {
			Author author = tblList.getSelectionModel().getSelectedItem();
			authorService.delete(author);
			search();
		});
		
	}
}
