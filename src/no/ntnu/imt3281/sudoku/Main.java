package no.ntnu.imt3281.sudoku;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Merges the fxml file with the controller
 * 
 * @author Fritjof
 *
 */
public class Main extends Application {
	@SuppressWarnings("unused")
	private static String norwegian = "no.ntnu.imt3281.sudoku.MessagesBundle_nb_NO";
	private static String english = "no.ntnu.imt3281.sudoku.MessagesBundle";
	protected final static String defaultLan = english;
	private ResourceBundle bundle;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		bundle = ResourceBundle.getBundle(defaultLan);
		Parent root = FXMLLoader.load(getClass().getResource("SudukoView.fxml"), bundle);
		Scene scene = new Scene(root);
		primaryStage.setTitle(bundle.getString("title"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
