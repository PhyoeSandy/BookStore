package com.jdc.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jdc.app.entity.Author;
import com.jdc.app.entity.Book;
import com.jdc.app.entity.Category;
import com.jdc.app.service.AuthorService;
import com.jdc.app.service.BookService;
import com.jdc.app.service.CategoryService;
import com.jdc.app.util.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookEdit {

	@FXML
	private Label error;
	@FXML
	private Label title;
	@FXML
	private ComboBox<Category> category;
	@FXML
	private ComboBox<Author> author;
	@FXML
	private TextField bookName;
	@FXML
	private TextField price;
	@FXML
	private DatePicker releasedDate;
	@FXML
	private TextArea remark;

	private Book book;
	private static Stage stage;
	
	private BookService bookService;
	private CategoryService categoryService;
	private AuthorService authorService;

	public static void show(Book book) {

		try {
			FXMLLoader loader = new FXMLLoader(BookEdit.class.getResource("BookEdit.fxml"));
			Parent view = loader.load();
			BookEdit controller = loader.getController(); // got book obj
			stage = new Stage();
			controller.setData(book);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL); // no access other
			// stage.initStyle(StageStyle.UNDECORATED); // no max,min bar
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setData(Book book) throws FileNotFoundException {
		this.book = book;
		if (book != null) {
			// edit
			category.setValue(book.getCategory());
			author.setValue(book.getAuthor());
			bookName.setText(book.getName());
			price.setText(String.valueOf(book.getPrice()));
			releasedDate.setValue(book.getReleasedDate());
			remark.setText(book.getRemark());

			title.setText("Edit Book");
			stage.setTitle("Edit Book");
			stage.getIcons().add(new Image(new FileInputStream("edit.png")));

		} else {
			// add
			title.setText("Add Book");
			stage.setTitle("Add Book");
			stage.getIcons().add(new Image(new FileInputStream("add.png")));
		}
	}

	public void save() {
		boolean newBook = book==null;
		try {

			if(newBook) 
				book = new Book();
			
			if(category.getValue() == null)
				throw new ApplicationException("Please Choose Category!");
			book.setCategory(category.getValue());
			
			if(author.getValue() == null)
				throw new ApplicationException("Please Choose Author!");
			book.setAuthor(author.getValue());
			
			if(bookName.getText() == null && bookName.getText().isEmpty())
				throw new ApplicationException("Please Enter Book Name!");
			book.setName(bookName.getText().trim());
			
			if(price.getText() == null && price.getText().isEmpty()) 
				throw new ApplicationException("Please Enter Price!");
			book.setPrice(Double.parseDouble(price.getText()));
			
			if(releasedDate.getValue() == null) 
				throw new ApplicationException("Please Pick Released Date!");
			book.setReleasedDate(releasedDate.getValue());
			
			book.setRemark(remark.getText().trim());
			
			if(newBook) 
				bookService.getInstance().add(book);
			else 
				bookService.getInstance().update(book);
			
			close();
			
		} catch (Exception e) {
			error.setText(e.getMessage());
		}
	}

	public void close() {
		error.getScene().getWindow().hide();
	}
	
	@FXML
	private void initialize() {
		category.getItems().addAll(categoryService.getInstance().findByName(null));
		author.getItems().addAll(authorService.getInstance().findAll());
	}

}
