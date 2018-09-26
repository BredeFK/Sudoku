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
	private Button btnNew;
	@FXML
	private Button btnMirror;
	@FXML
	private Button btnFlip;
	@FXML
	private Button btnBlue;
	@FXML
	private Button btnRed;
	@FXML
	private Button btnSwitch;
	@FXML
	private Button btnClear;
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
		sudoku.newBoard();
	}

	/**
	 * Mirror the board
	 */
	@FXML
	void onMirrorClick(ActionEvent event) {
		sudoku.mirrorBoard();
	}

	/**
	 * Flips the board upside down
	 */
	@FXML
	void onFlipClick(ActionEvent event) {
		sudoku.flipBoard();
	}

	/**
	 * Flips the board diagonally from top-left to bottom-right
	 */
	@FXML
	void onBlueFlipClick(ActionEvent event) {
		sudoku.flipBlueBoard();
	}

	/**
	 * Flips the board diagonally from bottom-left to top-right
	 */
	@FXML
	void onRedFlipClick(ActionEvent event) {
		sudoku.flipRedBoard();
	}

	/**
	 * Switches the numbers on the board
	 */
	@FXML
	void onSwitchClick(ActionEvent event) {
		sudoku.switchNumbersOnBoard();
	}

	/**
	 * Initialize the board
	 */
	@FXML
	void onClearClick(ActionEvent event) {
		sudoku.initializeBoard();
	}
}
