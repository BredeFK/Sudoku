package no.ntnu.imt3281.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SudukoView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Velkommen til Suduko");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}