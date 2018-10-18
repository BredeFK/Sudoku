package no.ntnu.imt3281.sudoku;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final Logger logger = Logger.getLogger(SudukoViewController.class.getName());
	private static final int GAP = 10;
	private static TextField[][] textFields = new TextField[9][9];
	private TextField completedText;
	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	// Source:
	// https://www.programcreek.com/java-api-examples/?api=javafx.scene.layout.Background
	private String styleGray = "-fx-control-inner-background: rgba(187, 187, 187, 1);";
	private String styleWhite = "-fx-control-inner-background: rgba(255, 255, 255, 1);";
	private String styleRed = "-fx-control-inner-background: rgba(255, 0, 0, 0.5);";

	private GridPane grid;

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
			generateBoard();
		});
	}

	/**
	 * This is treated as another empty constructor where the parameter i is
	 * pointless
	 * 
	 * <p>
	 * I made this constructor so that the empty constructor isn't run more than
	 * once. This is a bodge(fixed the error badly.), but I couldn't think of
	 * another way to to it. I tried to call the function generateBoard() once, but
	 * I couldn't get it to work.
	 * </p>
	 * 
	 * @param i int useless
	 */
	public SudukoViewController(int i) {
	}

	/**
	 * Generates the board
	 * 
	 * <p>
	 * Generates all the 81 TextFields and the four lines on the board.
	 * </p>
	 */
	public void generateBoard() {
		grid = new GridPane();
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

						// sudoku.updateArray(selectedRow, selectedCol, newValue);
						checkIfValidInput(selectedRow, selectedCol, newValue);
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
		completedText.setText(bundle.getString("congrats"));
		completedText.setPrefWidth(375);
		completedText.setTranslateX(80);
		completedText.setTranslateY(250);

		// Add grid to borderPane and lines and completedText to layoutPane
		borderPane.setCenter(grid);
		layoutPane.getChildren().addAll(lines);
		layoutPane.getChildren().add(completedText);
	}

	/**
	 * 
	 * @return temp String[][]
	 */
	protected String[][] convertTo2dString() {
		String[][] temp = new String[NUMB_ROW][NUMB_COLUMN];
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				temp[row][col] = textFields[row][col].getText();
			}
		}
		return temp;
	}

	/**
	 * @param stringArray String[][]
	 * 
	 * @return textFieldArray TextField[][]
	 */
	protected TextField[][] convertTo2dTextField(String[][] stringArray) {
		TextField[][] textFieldArray = new TextField[NUMB_ROW][NUMB_COLUMN];

		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				textFieldArray[row][col] = new TextField();
				textFieldArray[row][col].setText(stringArray[row][col]);
			}
		}

		return textFieldArray;
	}

	/**
	 * This function only updates the array when it's a valid input.
	 * 
	 * @param row
	 * @param col
	 * @param newValue
	 */
	protected void checkIfValidInput(int row, int col, String newValue) {
		String[][] array = convertTo2dString();

		try {
			boolean result = sudoku.isValid(row, col, newValue, array);
			if (result) {
				sudoku.updateArray(row, col, newValue);
				setStyleRight(row, col);
				completedText.setVisible(checkIfCompleted());
			} else {
				unlockElement(row, col);
				completedText.setVisible(checkIfCompleted());
			}
		} catch (BadNumberException e) {
			sudoku.updateArray(row, col, newValue);
			setStyleWrong(row, col);
			logger.log(Level.WARNING, e.getMessage());
		}
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
	 * @param row from textFields
	 * @param col from textFields
	 * 
	 * @return true/false if element is locked
	 */
	protected boolean isLocked(int row, int col) {
		return textFields[row][col].getStyle().equals(styleGray);
	}

	/**
	 * Checks if the board is completed
	 */
	protected boolean checkIfCompleted() {
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (textFields[row][col].getStyle().equals(styleRed) || textFields[row][col].getText().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets completed message TextField to visible or unvisible
	 * 
	 * @param isCompleted boolean
	 */
	protected void setVisibilityCompleted(boolean isCompleted) {
		completedText.setVisible(isCompleted);
	}

	/**
	 * Sets the style to the wrong color: red
	 * 
	 * @param row
	 * @param col
	 */
	protected void setStyleWrong(int row, int col) {
		textFields[row][col].setStyle(styleRed);
	}

	/**
	 * Sets the style to the right color: white
	 * 
	 * @param row
	 * @param col
	 */
	protected void setStyleRight(int row, int col) {
		textFields[row][col].setStyle(styleWhite);
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
	 * @param row
	 * @param col
	 * @param value
	 * @throws Exception
	 */
	protected void setElement(int row, int col, int value) throws ElementIsLockedException {
		if (sudoku.isLocked(row, col)) {
			textFields[row][col].setText(value + "");
		} else {
			throw new ElementIsLockedException(row, col);
		}
	}

	/**
	 * Creates a new game on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onNewClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.newBoard(array);
		fillBoardList(array);
	}

	/**
	 * Mirror the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onMirrorClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.mirrorBoard(array);
		fillBoardList(array);
	}

	/**
	 * Flips the board upside down
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onFlipClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.flipBoard(array);
		fillBoardList(array);
	}

	/**
	 * Flips the board diagonally from top-left to bottom-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onBlueFlipClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.flipBlueBoard(array);
		fillBoardList(array);
	}

	/**
	 * Flips the board diagonally from bottom-left to top-right
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onRedFlipClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.flipRedBoard(array);
		fillBoardList(array);
	}

	/**
	 * Switches the numbers on the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onSwitchClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.switchNumbersOnBoard(array);
		fillBoardList(array);
	}

	/**
	 * Initialize the board
	 * 
	 * @param event ActionEvent
	 */
	@FXML
	private void onClearClick(ActionEvent event) {
		String[][] array = convertTo2dString();
		array = sudoku.initializeBoard(array);
		fillBoardList(array);
	}

	private void fillBoardList(String[][] array) {

		// Initialize GUI board first
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				textFields[row][col].clear();
			}
		}

		// And then fill GUI board with new inputs
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				textFields[row][col].setText(array[row][col]);
				if (!textFields[row][col].getText().isEmpty()) {
					lockElement(row, col, textFields[row][col].getText());
				}
			}
		}
	}
}
