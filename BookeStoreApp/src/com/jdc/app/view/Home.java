package com.jdc.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.entity.Book;
import com.jdc.app.entity.Category;
import com.jdc.app.entity.Sale;
import com.jdc.app.entity.SaleDetails;
import com.jdc.app.service.BookService;
import com.jdc.app.service.CategoryService;
import com.jdc.app.service.SaleService;
import static com.jdc.app.util.CommonUtil.details;
import static com.jdc.app.util.CommonUtil.sale;
import static com.jdc.app.util.CommonUtil.sdList;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Home {

	@FXML
	private ComboBox<Category> category;
	@FXML
	private TextField book;
	@FXML
	private TableView<Book> tblList;

	// cart
	@FXML
	private TableView<SaleDetails> cartList;
	@FXML
	private Label total1;
	@FXML
	private Label total2;
	@FXML
	private Label subTotal;
	@FXML
	private Label tax;

	private SaleService saleService;
	private CategoryService categoryService;
	private BookService bookService;

	static int saleId;

	public void search() {
		tblList.getItems().clear();
		List<Book> bList = bookService.findByParams(category.getValue(), null, book.getText().trim(), null);
		tblList.getItems().addAll(bList);
	}

	public void clear() {
		category.setValue(null);
		book.clear();
		tblList.getItems().clear();
		total1.setText("0.0");
		total2.setText("0.0");
		subTotal.setText("0.0");
		tax.setText("0.0");
		cartList.getItems().clear();

		if (sdList.size() > 0) {
			saleService.delete(sdList);
			sdList = saleService.findSaleDetails(null, null, null, null, saleId);
		}
	}

	public void paid() throws FileNotFoundException {
		saleId = 0;
		clear();
		Image image = new Image(new FileInputStream("thankyou.jpg"));

		ImageView imageView = new ImageView(image);

		// Setting the position of the image

		imageView.setX(100);
		imageView.setY(50);

		// setting the fit height and width of the image view
		imageView.setFitHeight(200);
		imageView.setFitWidth(200);

		// Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);

		// Creating a Group object
		Group root = new Group(imageView);

		Scene scene = new Scene(root, 200, 200);
		Stage stage = new Stage();
		stage.setTitle("Book Store");
		stage.getIcons().add(new Image(new FileInputStream("bookStore.png")));
		stage.setScene(scene);
		stage.show();
	}

	public void addToCart(MouseEvent event) {

		cartList.getItems().clear();
		double totalValue = 0, subTotalValue = 0, taxValue = 0;

		Book book = tblList.getSelectionModel().getSelectedItem();

		// new item
		if (saleId == 0) {
			sale = new Sale();
			sale.setSaleDate(LocalDate.now());
			sale.setSaleTime(LocalTime.now());
			saleId = saleService.addSale(sale);
			sale.setId(saleId);
			details = new SaleDetails();
			sdList = new ArrayList<SaleDetails>();
		}

		details = saleService.getDetails(saleId, book.getId());
		if (details != null && details.getId() > 0) {
			// add existing details
			details.setQuantity(details.getQuantity() + 1);
			details.setSale(sale);
			saleService.update(details.getId(), details.getQuantity());
		} else {
			// add new sale details
			Author author = new Author();
			author.setId(book.getAuthor().getId());

			Category category = new Category();
			category.setId(book.getCategory().getId());

			details.setAuthor(author);
			details.setBook(book);
			details.setCategory(category);
			details.setSale(sale);

			details.setUnitPrice(book.getPrice());
			details.setQuantity(1);
			saleService.addDetails(details);
		}

		sdList = saleService.findSaleDetails(null, null, null, null, saleId);
		cartList.getItems().addAll(sdList);

		if (sdList.size() > 0) {
			for (SaleDetails sd : sdList) {
				subTotalValue += sd.getSubTotal();
				taxValue += sd.getSaleTax();
				totalValue += sd.getTotal();
			}
		}
		subTotal.setText(String.valueOf(subTotalValue));
		tax.setText(String.valueOf(taxValue));
		total1.setText(String.valueOf(totalValue));
		total2.setText(String.valueOf(totalValue));
	}

	@FXML
	private void initialize() {
		bookService = bookService.getInstance();
		categoryService = categoryService.getInstance();
		saleService = saleService.getInstance();

		// default value
		category.getItems().addAll(categoryService.findByName(null));

		// observe
		category.valueProperty().addListener((observable, oldValue, newValue) -> search());
		book.textProperty().addListener((observable, oldValue, newValue) -> search());

		// store chosen data
		cartList.getItems().addAll(sdList);
	}

}
