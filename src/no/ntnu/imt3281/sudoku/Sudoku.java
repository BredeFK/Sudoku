package no.ntnu.imt3281.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Sudoku extends Application  {
	private static final int NUMB_ROW = 9;
	private static final int NUMB_COLUMN = 9;
	
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
