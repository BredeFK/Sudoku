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
	private static final int GAP = 10;
	private TextField[][] textFields;

	// Source:
	// https://www.programcreek.com/java-api-examples/?api=javafx.scene.layout.Background
	private String styleGray = "-fx-control-inner-background: rgba(187, 187, 187, 1);";
	private String styleWhite = "-fx-control-inner-background: rgba(255, 255, 255, 1);";
	private String styleRed = "-fx-control-inner-background: rgba(255,0,0,0.5);";

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
		textFields = new TextField[Sudoku.NUMB_ROW][Sudoku.NUMB_COLUMN];

		Platform.runLater(() -> {
			generateBoard();
		});

	}
	
	public SudukoViewController(boolean notFirst) {
		
	}

	/**
	 * Generates the TextFields on the board
	 */
	private void generateBoard() {
		GridPane grid = new GridPane();
		TextField[][] textFields = new TextField[Sudoku.NUMB_ROW][Sudoku.NUMB_COLUMN];
		double gridHeight = borderPane.getHeight() - toolBar.getHeight();
		Line[] lines = new Line[4];

		// I have to have -13 on the vertical and +4 on the horizontal for some reason
		// this works and I don't know why
		lines[0] = new Line((borderPane.getWidth() - 13) / 3, 0, (borderPane.getWidth() - 13) / 3, gridHeight);
		lines[1] = new Line((borderPane.getWidth() - 13) / 1.5f, 0, (borderPane.getWidth() - 13) / 1.5f, gridHeight);
		lines[2] = new Line(0, (gridHeight + 4) / 3, borderPane.getWidth(), (gridHeight + 4) / 3);
		lines[3] = new Line(0, (gridHeight + 4) / 1.5f, borderPane.getWidth(), (gridHeight + 4) / 1.5f);

		for (Line line : lines)
			line.setStrokeWidth(3);

		for (int row = 0; row < Sudoku.NUMB_ROW; row++) {
			for (int col = 0; col < Sudoku.NUMB_COLUMN; col++) {

				textFields[row][col] = new TextField();

				textFields[row][col].setText("");
				textFields[row][col].setMinHeight((gridHeight / Sudoku.NUMB_COLUMN) - GAP);

				// Max width is borderPane width divided by number of boxes minus the gap
				// between each box and the gap between grid and borderPane
				textFields[row][col]
						.setMaxWidth((borderPane.getWidth() / Sudoku.NUMB_ROW) - GAP - ((long) Sudoku.NUMB_ROW / 5.0f));
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
						sudoku.updateArray(selectedRow, selectedCol, textFields[selectedRow][selectedCol].getText());
						/*
						 * try { boolean result = isValid(selectedRow, col, newValue); if (result) {
						 * textFields[selectedRow][selectedCol].setStyle(styleWhite);
						 * checkIfCompleted(); } else { Platform.runLater(() -> {
						 * unlockElement(selectedRow, selectedCol); }); } } catch (BadNumberException e)
						 * { textFields[selectedRow][selectedCol].setStyle(styleRed);
						 * logger.log(Level.WARNING, e.getMessage()); }
						 */
					}
				});
			}
		}

		borderPane.setCenter(grid);
		layoutPane.getChildren().addAll(lines);
		// sudoku.boardListener(textFields);

	}

	/**
	 * @param row from textFields
	 * @param col from textFields
	 * 
	 * @return true/false if element is locked
	 */
	protected boolean isLocked(int row, int col) {
		return textFields[row][col].getStyle().equals(styleGray);
	}

	/**
	 * @param row from textFields
	 * @param col from textFields
	 * 
	 * @return true/false if element is locked
	 */
	protected boolean isWrong(int row, int col) {
		return textFields[row][col].getStyle().equals(styleRed);
	}

	/**
	 * @param row   from textFields
	 * @param col   from textFields
	 * @param value from textFields
	 */
	protected void lockElement(int row, int col, String value) {
		textFields[row][col].setText(value);
		textFields[row][col].setStyle(styleGray);
		textFields[row][col].setEditable(false);
	}

	/**
	 * @param row from textFields
	 * @param col from textFields
	 */
	protected void unlockElement(int row, int col) {
		textFields[row][col].clear();
		textFields[row][col].setEditable(true);
		textFields[row][col].setStyle(styleWhite);
	}

	/**
	 * @param row
	 * @param col
	 * @param value
	 * @throws Exception
	 */
	protected void setElement(int row, int col, int value) throws ElementIsLockedException {
		if (!isLocked(row, col)) {
			textFields[row][col].setText(value + "");
		} else {
			throw new ElementIsLockedException(row, col);
		}
	}

	/**
	 * @param row int
	 * @param col int
	 * 
	 * @return element String
	 */
	protected String getElement(int row, int col) {
		return textFields[row][col].getText();
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
