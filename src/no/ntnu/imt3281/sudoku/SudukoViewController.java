package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SudukoViewController {

	private Sudoku sudoku;
	private static final int NUMB_ROW = Sudoku.NUMB_ROW;
	private static final int NUMB_COLUMN = Sudoku.NUMB_COLUMN;
	private static final int GAP = 10;
	private static TextField[][] textFields = new TextField[9][9];
	private static TextField completedText;

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
			// generateBoard();
		});
	}

	/**
	 * Generates the board
	 * 
	 * <p>
	 * Generates all the 81 TextFields and the four lines on the board.
	 * </p>
	 */
	private void generateBoard() {
		GridPane grid = new GridPane();
		completedText = new TextField();
		double gridHeight = borderPane.getHeight() - toolBar.getHeight();
		Line[] lines = new Line[4];

		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {

				textFields[row][col] = new TextField();
				textFields[row][col].setMinHeight((gridHeight / NUMB_COLUMN) - GAP);

				// Max width is borderPane width divided by number of boxes minus the gap
				// between each box and the gap between grid and borderPane
				textFields[row][col].setMaxWidth((borderPane.getWidth() / NUMB_ROW) - GAP - ((long) NUMB_ROW / 5.0f));
				textFields[row][col].setAlignment(Pos.CENTER);
				textFields[row][col].setFont(Font.font("Verdana", FontWeight.BLACK, 20));
				grid.setPadding(new Insets(5, 5, 5, 5));
				grid.setVgap(GAP);
				grid.setHgap(GAP);
				grid.add(textFields[row][col], col, row);

				final int selectedRow = row;
				final int selectedCol = col;

				textFields[row][col].textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {

						sudoku.updateArray(selectedRow, selectedCol, newValue);

						/*
						 * try { boolean result = isValid(selectedRow, selectedCol, newValue); if
						 * (result) { textFields[selectedRow][selectedCol].setStyle(styleWhite);
						 * completedText.setVisible(checkIfCompleted()); } else { Platform.runLater(()
						 * -> { unlockElement(selectedRow, selectedCol);
						 * completedText.setVisible(checkIfCompleted()); }); } } catch
						 * (BadNumberException e) {
						 * textFields[selectedRow][selectedCol].setStyle(styleRed);
						 * logger.log(Level.WARNING, e.getMessage()); }
						 */
					}
				});
			}
		}

		// I have to have -13 on the vertical and +4 on the horizontal for some reason
		// this works and I don't know why
		lines[0] = new Line((borderPane.getWidth() - 13) / 3, 0, (borderPane.getWidth() - 13) / 3, gridHeight);
		lines[1] = new Line((borderPane.getWidth() - 13) / 1.5f, 0, (borderPane.getWidth() - 13) / 1.5f, gridHeight);
		lines[2] = new Line(0, (gridHeight + 4) / 3, borderPane.getWidth(), (gridHeight + 4) / 3);
		lines[3] = new Line(0, (gridHeight + 4) / 1.5f, borderPane.getWidth(), (gridHeight + 4) / 1.5f);

		for (Line line : lines)
			line.setStrokeWidth(3);

		// Create completed board TextField
		completedText.setVisible(false);
		completedText.setEditable(false);
		completedText.setFont(Font.font("Verdana", FontWeight.BLACK, 15));
		completedText.setText("Congratulations! You completed the Board");
		completedText.setPrefWidth(375);
		completedText.setTranslateX(80);
		completedText.setTranslateY(250);

		// Add grid to borderPane and lines and completedText to layoutPane
		borderPane.setCenter(grid);
		layoutPane.getChildren().addAll(lines);
		layoutPane.getChildren().add(completedText);
	}

	/**
	 * Creates a new game on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onNewClick(ActionEvent event) {
		sudoku.newBoard();
	}

	/**
	 * Mirror the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onMirrorClick(ActionEvent event) {
		sudoku.mirrorBoard();
	}

	/**
	 * Flips the board upside down
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onFlipClick(ActionEvent event) {
		sudoku.flipBoard();
	}

	/**
	 * Flips the board diagonally from top-left to bottom-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onBlueFlipClick(ActionEvent event) {
		sudoku.flipBlueBoard();
	}

	/**
	 * Flips the board diagonally from bottom-left to top-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onRedFlipClick(ActionEvent event) {
		sudoku.flipRedBoard();
	}

	/**
	 * Switches the numbers on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onSwitchClick(ActionEvent event) {
		sudoku.switchNumbersOnBoard();
	}

	/**
	 * Initialize the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onClearClick(ActionEvent event) {
		sudoku.initializeBoard();
	}
}
