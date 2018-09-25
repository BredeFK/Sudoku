package no.ntnu.imt3281.sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	public void generateAndCheck(BorderPane borderPane, ToolBar toolBar, Pane layoutPane) {
		GridPane grid = new GridPane();
		double gridHeight = borderPane.getHeight() - toolBar.getHeight();
		Line[] lines = new Line[4];
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {

				textFields[row][col] = new TextField();

				textFields[row][col].setMinHeight((gridHeight / NUMB_COLUMN) - GAP);

				// Max width is borderPane width divided by number of boxes minus the gap
				// between each box and the gap between grid and borderPane
				textFields[row][col].setMaxWidth((borderPane.getWidth() / NUMB_ROW) - GAP - (NUMB_ROW / 5));
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

						// Check rows, columns and boxes
						switch (isValid(selectedRow, selectedCol, newValue)) {
						// Input gets deleted
						case -1:
							Platform.runLater(() -> {
								textFields[selectedRow][selectedCol].clear();
							});
							break;
						// Input field gets marked red and not deleted
						case 0:
							textFields[selectedRow][selectedCol].setStyle(styleRed);
							break;

						// Input is valid and marked to default colour
						case 1:
							textFields[selectedRow][selectedCol].setStyle(styleWhite);
							checkIfCompleted();
							break;

						// default should never be triggered, but delete in case
						default:
							Platform.runLater(() -> {
								textFields[selectedRow][selectedCol].clear();
							});
							break;
						}
					}
				});

				textFields[row][col].focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (isLocked(selectedRow, selectedCol)) {
							System.out.println("Låst");
						}
					}
				});

			}
		}

		// I have to have -4 on the vertical and +4 on the horizontal for some reason
		// this works and I don't know why
		lines[0] = new Line((borderPane.getWidth() - 4) / 3, 0, (borderPane.getWidth() - 4) / 3, gridHeight);
		lines[1] = new Line((borderPane.getWidth() - 4) / 1.5f, 0, (borderPane.getWidth() - 4) / 1.5f, gridHeight);
		lines[2] = new Line(0, (gridHeight + 4) / 3, borderPane.getWidth(), (gridHeight + 4) / 3);
		lines[3] = new Line(0, (gridHeight + 4) / 1.5f, borderPane.getWidth(), (gridHeight + 4) / 1.5f);

		for (Line line : lines)
			line.setStrokeWidth(3);

		borderPane.setCenter(grid);
		layoutPane.getChildren().addAll(lines);
	}

	/**
	 * Checks if the input is valid
	 * <p>
	 * Checks the row, column and sub grid box if the input is valid. Returns -1 if
	 * its illegal input, 0 if it legal but wrong and 1 if it's the right number
	 * </p>
	 * 
	 * Source for checking sub-grid: {@link https://www.baeldung.com/java-sudoku}
	 * 
	 * 
	 * @param row   from changed textField
	 * @param col   from changed textField
	 * @param value from changed textField
	 * 
	 * @return integer -1, 0 or 1
	 */
	private int isValid(int row, int col, String value) {

		if (value.isEmpty()) {
			return -1;
		} else if (!value.matches("\\d")) {
			System.out.println("Not number");
			return -1;
		} else if (Integer.parseInt(value) < 1 || Integer.parseInt(value) > 9) {
			System.out.println("Not 1-9");
			return -1;
		} else {
			// Check row
			for (int i = 0; i < NUMB_COLUMN; i++) {
				if (col != i) {
					if (textFields[row][i].getText().equals(value)) {
						System.out.println("Row: nei");
						return 0;
					}
				}
			}

			// Check column
			for (int j = 0; j < NUMB_ROW; j++) {
				if (row != j) {
					if (textFields[j][col].getText().equals(value)) {
						System.out.println("Col: nei");
						return 0;
					}
				}
			}

			// Check sub-grid
			int startRow = (row / SUB_GRID) * SUB_GRID;
			int startCol = (col / SUB_GRID) * SUB_GRID;

			int endRow = startRow + SUB_GRID;
			int endCol = startCol + SUB_GRID;

			for (int r = startRow; r < endRow; r++) {
				for (int c = startCol; c < endCol; c++) {
					if (r != row && c != col) {
						if (textFields[r][c].getText().equals(value)) {
							System.out.println("SubGrid: nei");
							return 0;
						}
					}
				}
			}
			return 1;
		}
	}

	/**
	 * Reads the JSONarray and converts it to a 2d int array. <br>
	 * <br>
	 * Source for converting stringBuffer to integer values
	 * {@link https://stackoverflow.com/questions/29717963/converting-a-stringbuilder-to-integer-values-in-java}
	 * 
	 * @return parsed jsonArray
	 */
	private int[][] getJson() {
		int[][] array = new int[NUMB_ROW][NUMB_COLUMN];

		try {
			BufferedReader buffer = new BufferedReader(new FileReader("board.json"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
			buffer.close();
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

			// COnvert 1d array to 2d array
			int index = 0;
			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					array[row][col] = intNumbers[index++];
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}

		return array;
	}

	/**
	 * Fill the board with numbers
	 */
	public void newBoard() {
		int[][] temp = getJson();

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

	/**
	 * Unlocks all the elements on the board
	 */
	public void initializeBoard() {
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				unlockElement(row, col);
			}
		}
	}

	/**
	 * Mirror the board
	 */
	public void mirrorBoard() {
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
	public void flipBoard() {
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
	public void flipBlueBoard() {
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
	public void flipRedBoard() {
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
	public void switchNumbersOnBoard() {
		int[][] temp = convertTo2dInt(textFields);
		ArrayList<Integer> numbers = getRandomNumbers();
		boolean list[][] = new boolean[9][9];

		initializeBoard();
		Platform.runLater(() -> {
			int number = -1;

			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					if (temp[row][col] != -1) {

						switch (temp[row][col]) {
						case 1:
							number = numbers.get(0);
							break;
						case 2:
							number = numbers.get(1);
							break;
						case 3:
							number = numbers.get(2);
							break;
						case 4:
							number = numbers.get(3);
							break;
						case 5:
							number = numbers.get(4);
							break;
						case 6:
							number = numbers.get(5);
							break;
						case 7:
							number = numbers.get(6);
							break;
						case 8:
							number = numbers.get(7);
							break;
						case 9:
							number = numbers.get(8);
							break;
						}
						lockElement(row, col, number + "");
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
	private int[][] convertTo2dInt(TextField[][] array) {
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
	private void checkIfCompleted() {
		boolean finished = true;
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (textFields[row][col].getStyle().equals(styleRed)) {
					finished = false;
				} else if (textFields[row][col].getText().isEmpty()) {
					finished = false;
				}
			}
		}
		if (finished) {
			System.out.println("YAY, you completed the board!");
		}
	}

	/**
	 * @param row from textFields
	 * @param col from textFields
	 * 
	 * @return true/false if element is locked
	 */
	private boolean isLocked(int row, int col) {
		return textFields[row][col].getStyle().equals(styleGray);
	}

	/**
	 * @param row   from textFields
	 * @param col   from textFields
	 * @param value from textFields
	 */
	private void lockElement(int row, int col, String value) {
		textFields[row][col].setText(value);
		textFields[row][col].setStyle(styleGray);
		textFields[row][col].setEditable(false);
	}

	/**
	 * @param row from textFields
	 * @param col from textFields
	 */
	private void unlockElement(int row, int col) {
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
	 * @return ArrayList<Integer> with random numbers 1-9 (no dups)
	 */
	private ArrayList<Integer> getRandomNumbers() {
		ArrayList<Integer> numbers = new ArrayList<>();
		Random rnd = new Random(System.currentTimeMillis());
		int index = 0;
		int sum = 0;
		while (numbers.size() < NUMB_ROW) {
			int number = rnd.nextInt(NUMB_ROW) + 1;
			sum++;
			if (!numbers.contains(number) && number != (index + 1)) {
				System.out.println("match: " + number + " " + (index + 1));
				numbers.add(index++, number);

			} else if (index == (NUMB_ROW - 1) && number == NUMB_ROW && !numbers.contains(NUMB_ROW)) {
				// If this triggers, it means that the random number 9 isn't in the list and the
				// value its going to be assigned to is 9. This then crashes the application
				// since it searches forever. This switches the number from index 7 to index 8
				// and the other way.

				int temp = numbers.get(index - 1);
				numbers.add(index - 1, number);
				numbers.add(index++, temp);
				System.out.printf("---FIX---\nmatch: %d %d\nmatch: %d %d\n", number, (index - 1), temp, index);
			}
		}
		System.out.println("int's generated: " + sum);

		return numbers;
	}

}
