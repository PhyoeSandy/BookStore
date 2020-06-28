package com.jdc.app.view;

import java.io.File;
import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.entity.Book;
import com.jdc.app.entity.Category;
import com.jdc.app.service.AuthorService;
import com.jdc.app.service.BookService;
import com.jdc.app.service.CategoryService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class BookList {

	@FXML
	private ComboBox<Category> category;
	@FXML
	private ComboBox<Author> author;
	@FXML
	private DatePicker releasedDate;
	@FXML
	private TableView<Book> tblList;

	private CategoryService categoryService;
	private AuthorService authorService;
	private BookService bookService;

	public void add() {
		BookEdit.show(null);
		search();
	}

	public void search() {
		tblList.getItems().clear();
		List<Book> bList = bookService.findByParams(category.getValue(), 
				author.getValue(), null, releasedDate.getValue());
		tblList.getItems().addAll(bList);
	}

	public void clear() {
		category.setValue(null);
		author.setValue(null);
		releasedDate.setValue(null);
	}
	
	private void createMenu() {
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");
		MenuItem imgUpload = new MenuItem("Image Upload");
		MenuItem details = new MenuItem("Show Details");
		
		ContextMenu menu = new ContextMenu(edit,delete,imgUpload,details);
		tblList.setContextMenu(menu);
		
		edit.setOnAction(e -> edit());
		delete.setOnAction(e -> delete());
		imgUpload.setOnAction(e -> imgUpload(e));
		details.setOnAction(e -> showDetails());
	}

	private void showDetails() {
		Book book = tblList.getSelectionModel().getSelectedItem();
		BookDetails.show(book);
	}

	public void imgUpload(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Upload Book Cover");
		fc.setSelectedExtensionFilter(new ExtensionFilter("Book Cover", "*.png","*.jpg"));
		fc.setInitialDirectory(new File(System.getProperty("user.home"),"Desktop"));
		File file = fc.showOpenDialog(category.getScene().getWindow());
		System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));

		Book book = tblList.getSelectionModel().getSelectedItem();
		book.setImage(file.getAbsolutePath());
		bookService.imageUpload(book);
	}

	private void edit() {
		Book book = tblList.getSelectionModel().getSelectedItem();
		BookEdit.show(book);
		bookService.update(book);
		search();
	}
	
	private void delete() {
		Book book = tblList.getSelectionModel().getSelectedItem();
		bookService.delete(book);
		search();
	}

	@FXML
	private void initialize() {
		categoryService = categoryService.getInstance();
		authorService = authorService.getInstance();
		bookService = bookService.getInstance();
		
		category.getItems().addAll(categoryService.findByName(null));
		author.getItems().addAll(authorService.findAll());
		createMenu();
		
		category.valueProperty().addListener((a,b,c) -> search());
		author.valueProperty().addListener((a,b,c) -> search());
		releasedDate.valueProperty().addListener( (a,b,c) -> search());
		
	}

}
