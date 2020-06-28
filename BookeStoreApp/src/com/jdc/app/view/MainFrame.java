package com.jdc.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainFrame {

	@FXML
	private Label title;
	@FXML
	private Label footer;
	@FXML
	private StackPane viewHolder;
	@FXML
	private VBox leftMenu;

	public void showHome(MouseEvent event) {
		changeActivePage(event);
		loadView("My Book Store", "Home");
	}

	public void showCategory(MouseEvent event) {
		changeActivePage(event);
		loadView("Category List","CategoryList");
	}

	public void showBook(MouseEvent event) {
		changeActivePage(event);
		loadView("Book List", "BookList");
	}

	public void showAuthor(MouseEvent event) {
		changeActivePage(event);
		loadView("Author List","AuthorList");
	}
	
	public void showSaleHistory(MouseEvent event) {
		changeActivePage(event);
		loadView("Sale History","SaleHistory");
	}

	private void changeActivePage(MouseEvent event) {
		Node node = (Node) event.getSource();
		leftMenu.getChildren().stream()
							  .filter(n -> n.getStyleClass().contains("active"))
							  .findAny()
				              .ifPresent(n -> n.getStyleClass().remove("active"));

		node.getStyleClass().add("active");
	}
	
	public void loadView(String viewName, String viewFile) {
		title.setText(viewName);
		try {
			Parent view = FXMLLoader.load(getClass().getResource(viewFile.concat(".fxml")));
			viewHolder.getChildren().clear();
			viewHolder.getChildren().add(view);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		footer.setText("\u00A9 By JDC \u00A9");
		loadView("My Book Store", "Home");
	}
	
}
