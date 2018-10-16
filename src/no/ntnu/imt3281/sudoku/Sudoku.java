package no.ntnu.imt3281.sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * @author Brede Fritjof Klausen
 */

public class Sudoku extends Application {

	private static final int NUMB_ROW = 9;
	private static final int GAP = 10;
	private static final int NUMB_COLUMN = NUMB_ROW;
	private static final int SUB_GRID = NUMB_ROW / 3;
	private static final Logger logger = Logger.getLogger(Sudoku.class.getName());
	private TextField[][] textFields;
	// Source:
	// https://www.programcreek.com/java-api-examples/?api=javafx.scene.layout.Background
	private String styleGray = "-fx-control-inner-background: rgba(187, 187, 187, 1);";
	private String styleWhite = "-fx-control-inner-background: rgba(255, 255, 255, 1);";
	private String styleRed = "-fx-control-inner-background: rgba(255,0,0,0.5);";

	/**
	 * Constructor for the class Sudoku
	 */
	public Sudoku() {
		textFields = new TextField[NUMB_ROW][NUMB_COLUMN];
	}

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

	/**
	 * Generates the board and checks if the input is valid
	 */
	protected void generateAndCheck(BorderPane borderPane, ToolBar toolBar, Pane layoutPane) {
		GridPane grid = new GridPane();
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

						try {
							boolean result = isValid(selectedRow, selectedCol, newValue);
							if (result) {
								textFields[selectedRow][selectedCol].setStyle(styleWhite);
								checkIfCompleted();
							} else {
								Platform.runLater(() -> {
									unlockElement(selectedRow, selectedCol);
								});
							}
						} catch (BadNumberException e) {
							textFields[selectedRow][selectedCol].setStyle(styleRed);
							logger.log(Level.WARNING, e.getMessage());
						}
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

		borderPane.setCenter(grid);
		layoutPane.getChildren().addAll(lines);
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
		if (!isLocked(row, col)) {
			textFields[row][col].setText(value + "");
		} else {
			throw new ElementIsLockedException(row, col);
		}
	}

	/**
	 * Checks if the input is valid
	 * <p>
	 * Checks the row, column and sub grid box if the input is valid. Throws
	 * BadNumberException if the number is already on the board, false if it's
	 * illegal input and true if it's legal input and the right number
	 * </p>
	 * 
	 * @param row   from changed textField
	 * @param col   from changed textField
	 * @param value from changed textField
	 * 
	 * @return boolean true or false
	 * @throws BadNumberException if number already exists
	 */
	protected boolean isValid(int row, int col, String value) throws BadNumberException {

		// Check if empty, is not a number and number is between 1 and 9
		if (value.isEmpty() || !value.matches("\\d") || (Integer.parseInt(value) < 1 || Integer.parseInt(value) > 9)) {
			return false;
		}

		// Check row
		int i = 0;
		Iterator<String> rowIterator = getIteratorRow(row);
		while (rowIterator.hasNext()) {
			if (rowIterator.next().equals(value) && i != col) {
				throw new BadNumberException(row, i, "Row");
			}
			i++;
		}

		// Check column
		i = 0;
		Iterator<String> colIterator = getIteratorCol(col);
		while (colIterator.hasNext()) {
			if (colIterator.next().equals(value) && i != row) {
				throw new BadNumberException(i, col, "Column");
			}
			i++;
		}

		// Check sub-grid
		int startRow = (row / SUB_GRID) * SUB_GRID;
		int startCol = (col / SUB_GRID) * SUB_GRID;
		int j;
		Iterator<String> boxIterator = getIteratorBox(row, col);
		for (i = startRow; i < startRow + SUB_GRID; i++) {
			for (j = startCol; j < startCol + SUB_GRID; j++) {
				if (boxIterator.next().equals(value) && i != row && j != col) {
					throw new BadNumberException(i, j, "Box");
				}
			}
		}

		// Input is valid
		return true;
	}

	/**
	 * Returns an iterator object for the row
	 * 
	 * @param row
	 * 
	 * @return Iterator<String>
	 */
	protected Iterator<String> getIteratorRow(int row) {
		ArrayList<String> arrayListRow = new ArrayList<>();
		for (int col = 0; col < NUMB_COLUMN; col++) {
			arrayListRow.add(textFields[row][col].getText());
		}
		return arrayListRow.iterator();
	}

	/**
	 * Returns an iterator object for the column
	 * 
	 * @param col
	 * 
	 * @return Iterator<String>
	 */
	protected Iterator<String> getIteratorCol(int col) {
		ArrayList<String> arrayListCol = new ArrayList<>();
		for (int row = 0; row < NUMB_ROW; row++) {
			arrayListCol.add(textFields[row][col].getText());
		}
		return arrayListCol.iterator();
	}

	/**
	 * Returns an iterator object for the sub-grid box
	 * 
	 * Source for checking sub-grid: {@link https://www.baeldung.com/java-sudoku}
	 * 
	 * @param row
	 * @param col
	 * 
	 * @return Iterator<String>
	 * 
	 */
	protected Iterator<String> getIteratorBox(int row, int col) {
		ArrayList<String> arrayListBox = new ArrayList<>();
		int startRow = (row / SUB_GRID) * SUB_GRID;
		int startCol = (col / SUB_GRID) * SUB_GRID;

		int endRow = startRow + SUB_GRID;
		int endCol = startCol + SUB_GRID;

		for (int r = startRow; r < endRow; r++) {
			for (int c = startCol; c < endCol; c++) {
				arrayListBox.add(textFields[r][c].getText());
			}
		}

		return arrayListBox.iterator();
	}

	/**
	 * Reads the JSONarray and converts it to a 2d int array. <br>
	 * <br>
	 * Source for converting stringBuffer to integer values
	 * {@link https://stackoverflow.com/questions/29717963/converting-a-stringbuilder-to-integer-values-in-java}
	 * 
	 * @return parsed jsonArray
	 */
	protected int[][] getJson() {
		int[][] array = new int[NUMB_ROW][NUMB_COLUMN];

		try (BufferedReader buffer = new BufferedReader(new FileReader("board.json"))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
			// Split string where there aren't a number
			String[] numbers = sb.toString().split("[^-?1-9]");
			int[] intNumbers = new int[NUMB_ROW * NUMB_COLUMN];
			int i = 0;

			// Remove empty spaces and put them in 1d array
			for (String number : numbers) {
				if (!number.equals("")) {
					intNumbers[i++] = Integer.parseInt(number);
				}
			}

			// Convert 1d array to 2d array
			int index = 0;
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					array[row][col] = intNumbers[index++];
				}
			}

		} catch (FileNotFoundException e) {
			array = null;
			logger.log(Level.WARNING, String.format("File not found: %s%n", e.getMessage()));
		} catch (IOException e) {
			array = null;
			logger.log(Level.WARNING, String.format("IOException: %s%n", e.getMessage()));
		}

		return array;
	}

	/**
	 * Fill the board with numbers
	 */
	protected void newBoard() {
		int[][] temp = getJson();

		if (temp != null) {
			initializeBoard();
			Platform.runLater(() -> {
				for (int row = 0; row < NUMB_ROW; row++) {
					for (int col = 0; col < NUMB_COLUMN; col++) {
						// if number is -1 it's empty
						if (temp[row][col] != -1) {
							lockElement(row, col, temp[row][col] + "");
						}
					}
				}
			});
		}
	}

	/**
	 * Unlocks all the elements on the board
	 */
	protected void initializeBoard() {
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				unlockElement(row, col);
			}
		}
	}

	/**
	 * Mirror the board
	 */
	protected void mirrorBoard() {
		int[][] temp = convertTo2dInt(textFields);

		initializeBoard();
		Platform.runLater(() -> {
			int mirrorCol = 0;
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					// -1 because it goes to from 0 to 8
					mirrorCol = (NUMB_COLUMN - 1) - col;
					if (temp[row][mirrorCol] != -1) {
						lockElement(row, col, temp[row][mirrorCol] + "");
					}
				}
			}
		});

	}

	/**
	 * Flip the board upside down
	 */
	protected void flipBoard() {
		int[][] temp = convertTo2dInt(textFields);

		initializeBoard();
		Platform.runLater(() -> {
			int mirrorRow = 0;
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					// -1 because it goes to from 0 to 8
					mirrorRow = (NUMB_ROW - 1) - row;

					if (temp[mirrorRow][col] != -1) {
						lockElement(row, col, temp[mirrorRow][col] + "");
					}
				}
			}
		});
	}

	/**
	 * Flips the board from top-right to bottom-left
	 */
	protected void flipBlueBoard() {
		int[][] temp = convertTo2dInt(textFields);

		initializeBoard();
		Platform.runLater(() -> {
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					if (temp[col][row] != -1) {
						lockElement(row, col, temp[col][row] + "");
					}
				}
			}
		});

	}

	/**
	 * Flips the board from bottom-left to top-right
	 */
	protected void flipRedBoard() {
		int[][] temp = convertTo2dInt(textFields);

		initializeBoard();
		Platform.runLater(() -> {
			int newRow = 0;
			int newCol = 0;
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					newRow = (NUMB_ROW - 1) - row;
					newCol = (NUMB_COLUMN - 1) - col;

					if (temp[newCol][newRow] != -1) {
						lockElement(row, col, temp[newCol][newRow] + "");
					}
				}
			}
		});

	}

	/**
	 * Switches numbers on the board
	 */
	protected void switchNumbersOnBoard() {
		int[][] temp = convertTo2dInt(textFields);
		ArrayList<Integer> numbers = getRandomNumbers();

		initializeBoard();
		Platform.runLater(() -> {

			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					if (temp[row][col] != -1) {
						lockElement(row, col, numbers.get(temp[row][col] - 1) + "");
					}
				}
			}
		});
	}

	/**
	 * @param array from textFields
	 * 
	 * @return returnArray converted to int[][]
	 */
	protected int[][] convertTo2dInt(TextField[][] array) {
		int[][] returnArray = new int[NUMB_ROW][NUMB_COLUMN];
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (array[row][col].getStyle().equals(styleGray)) {
					returnArray[row][col] = Integer.parseInt(textFields[row][col].getText());
				} else {
					returnArray[row][col] = -1;
				}
			}
		}
		return returnArray;
	}

	/**
	 * Checks if the board is completed
	 */
	protected void checkIfCompleted() {
		boolean finished = true;
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (textFields[row][col].getStyle().equals(styleRed) || textFields[row][col].getText().isEmpty()) {
					finished = false;
				}
			}
		}
		// TODO : write this on the fxml application and not in the logger
		if (finished) {
			logger.info("YAY, you completed the board!");
		}
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
	 * Generates random list with numbers and no duplicates
	 * 
	 * <p>
	 * This function creates an ArrayList list with random numbers from 1-NUMB_ROW
	 * and no duplicates. It also makes sure that the number doesn't equal to the
	 * number it's going to be assigned to (index+1). When it's on it's last index
	 * (NUMB_ROW-1) and the number NUMB_ROW isn't in the list, it switches the value
	 * with the index under (NUMB_ROW-1-1) so it doesn't loop forever.
	 * </p>
	 * 
	 * Source for no no duplicates:
	 * {@link https://stackoverflow.com/a/10136855/8883030}
	 * 
	 * @return ArrayList<Integer> with random numbers 1-9 (no duplications)
	 */
	protected ArrayList<Integer> getRandomNumbers() {
		ArrayList<Integer> numbers = new ArrayList<>();
		Random rnd = new Random(System.currentTimeMillis());
		int index = 0;
		while (numbers.size() < NUMB_ROW) {
			int number = rnd.nextInt(NUMB_ROW) + 1;
			if (!numbers.contains(number) && number != (index + 1)) {
				numbers.add(index++, number);

			} else if (index == (NUMB_ROW - 1) && number == NUMB_ROW && !numbers.contains(NUMB_ROW)) {
				// If this triggers, it means that the random number 9 isn't in the list and the
				// value its going to be assigned to is 9. This then crashes the application
				// since it searches forever. This switches the number from index 7 to index 8
				// and the other way.

				int temp = numbers.get(index - 1);
				numbers.add(index - 1, number);
				numbers.add(index++, temp);
			}
		}
		return numbers;
	}
}
