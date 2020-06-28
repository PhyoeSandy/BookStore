package com.jdc.app;

import java.io.FileInputStream;

import com.jdc.app.view.MainFrame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(MainFrame.class.getResource("MainFrame.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Book Store");
		stage.getIcons().add(new Image(new FileInputStream("bookStore.png")));
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
