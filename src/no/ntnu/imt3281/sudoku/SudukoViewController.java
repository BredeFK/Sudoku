package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

	/**
	 * Generates the board <br>
	 * <br>
	 * 
	 * Source for Platform.runLater:
	 * {@link https://stackoverflow.com/a/32893573/8883030}
	 */
	public SudukoViewController() {
		sudoku = new Sudoku();

		Platform.runLater(() -> {
			sudoku.generateAndCheck(borderPane, toolBar, layoutPane);
		});
	}

	/**
	 * Creates a new game on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onNewClick(ActionEvent event) {
		sudoku.newBoard();
	}

	/**
	 * Mirror the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onMirrorClick(ActionEvent event) {
		sudoku.mirrorBoard();
	}

	/**
	 * Flips the board upside down
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onFlipClick(ActionEvent event) {
		sudoku.flipBoard();
	}

	/**
	 * Flips the board diagonally from top-left to bottom-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onBlueFlipClick(ActionEvent event) {
		sudoku.flipBlueBoard();
	}

	/**
	 * Flips the board diagonally from bottom-left to top-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onRedFlipClick(ActionEvent event) {
		sudoku.flipRedBoard();
	}

	/**
	 * Switches the numbers on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onSwitchClick(ActionEvent event) {
		sudoku.switchNumbersOnBoard();
	}

	/**
	 * Initialize the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	void onClearClick(ActionEvent event) {
		sudoku.initializeBoard();
	}
}
