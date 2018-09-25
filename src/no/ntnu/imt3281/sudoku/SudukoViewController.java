package no.ntnu.imt3281.sudoku;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * @author Brede Fritjof Klausen
 */

public class SudukoViewController {

	private Sudoku sudoku;

	@FXML
	private Pane layoutPane;

	@FXML
	private BorderPane borderPane;

	@FXML
	private ToolBar toolBar;

	@FXML
	private Button btn_New, btn_Mirror, btn_Flip, btn_Blue, btn_Red, btn_Switch, btn_Clear;

	@FXML
	private GridPane gridID;

	public SudukoViewController() {
		sudoku = new Sudoku();
		// Source for Platform.runLater: https://stackoverflow.com/a/32893573/8883030
		Platform.runLater(() -> {
			sudoku.generateAndCheck(borderPane, toolBar, layoutPane);
		});
	}

	/**
	 * Creates a new game on the board
	 */
	@FXML
	void onNewClick(ActionEvent event) {
		System.out.println("onNewClick");
		sudoku.newBoard();
	}

	/**
	 * Mirror the board
	 */
	@FXML
	void onMirrorClick(ActionEvent event) {
		System.out.println("onMirrorClick");
		sudoku.mirrorBoard();
	}

	/**
	 * Flips the board upside down
	 */
	@FXML
	void onFlipClick(ActionEvent event) {
		System.out.println("onFlipClick");
		sudoku.flipBoard();
	}

	/**
	 * Flips the board diagonally from top-left to bottom-right
	 */
	@FXML
	void onBlueFlipClick(ActionEvent event) {
		System.out.println("onBlueFlipClick");
		sudoku.flipBlueBoard();
	}

	/**
	 * Flips the board diagonally from bottom-left to top-right
	 */
	@FXML
	void onRedFlipClick(ActionEvent event) {
		System.out.println("onRedFlipClick");
		sudoku.flipRedBoard();
	}

	/**
	 * Switches the numbers on the board
	 */
	@FXML
	void onSwitchClick(ActionEvent event) {
		System.out.println("onSwitchClick");
		sudoku.switchNumbersOnBoard();
	}

	/**
	 * Initializes the board
	 */
	@FXML
	void onClearClick(ActionEvent event) {
		System.out.println("onClearClick");
		sudoku.initializeBoard();
	}
}
