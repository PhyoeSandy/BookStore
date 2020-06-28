package com.jdc.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.jdc.app.entity.Book;
import com.jdc.app.entity.SaleDetails;
import com.jdc.app.service.BookService;
import com.jdc.app.util.CommonUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookDetails {

	@FXML
	private ImageView image;
	@FXML
	private Label category;
	@FXML
	private Label author;
	@FXML
	private Label book;
	@FXML
	private Label price;
	@FXML
	private Label releasedDate;
	@FXML
	private Label remark;
	
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static void show(Book book) {
		try {
			FXMLLoader loader = new FXMLLoader(BookDetails.class.getResource("BookDetails.fxml"));
			Stage stage = new Stage();
			Parent view = loader.load();
			BookDetails controller = loader.getController();
			controller.setData(book);
			
			stage.setTitle("Book Details");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(view));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void setData(Book b) throws FileNotFoundException {
		if(book != null) {
			category.setText(b.getCategory().getName());
			author.setText(b.getAuthor().getName());
			book.setText(b.getName());
			price.setText(String.valueOf(b.getPrice()));
			remark.setText(b.getRemark());
			image.setImage(new Image(new FileInputStream(BookService.getInstance().getImage(b))));
			//releasedDate.setText(String.valueOf(b.getReleasedDate()));
			releasedDate.setText(String.valueOf(DF.format(b.getReleasedDate())));
		}
	}

	public void buy() {
		/*
		try {
			FXMLLoader loader = new FXMLLoader(Home.class.getResource("Home.fxml"));
			Stage stage = new Stage();
			Parent view = loader.load();
			BookDetails controller = loader.getController();
			
			stage.setScene(new Scene(view));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	public void close() {
		category.getScene().getWindow().hide();
	}

}
