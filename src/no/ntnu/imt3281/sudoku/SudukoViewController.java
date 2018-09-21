package no.ntnu.imt3281.sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Brede Fritjof Klausen
 */

public class SudukoViewController {

	private static final int NUMB_ROW = 9;
	private static final int GAP = 10;
	private static final int NUMB_COLUMN = NUMB_ROW;
	private static final int SUB_GRID = NUMB_ROW / 3;
	private TextField[][] textFields;

	public SudukoViewController() {
		textFields = new TextField[NUMB_ROW][NUMB_COLUMN];
		Platform.runLater(() -> {
			generateAndCheck();
		});
	}

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btn_Easy, btn_Medium;

	@FXML
	private GridPane gridID;

	@FXML
	private ToolBar toolBar;

	@FXML
	void onEasyClick(ActionEvent event) {
		System.out.println("Easy button");
		getJson();
	}

	@FXML
	void onMediumClick(ActionEvent event) {
		System.out.println("Medium button");
	}

	/**
	 * Generates the board and checks if the input is valid
	 */
	protected void generateAndCheck() {
		GridPane grid = new GridPane();
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {

				textFields[row][col] = new TextField();

				textFields[row][col].setMinHeight(((borderPane.getHeight() - toolBar.getHeight()) / NUMB_COLUMN) - GAP);

				// Max width is borderPane width divided by number of boxes minus the gap
				// between each box and the gap between grid and borderPane
				textFields[row][col].setMaxWidth((borderPane.getWidth() / NUMB_ROW) - GAP - (NUMB_ROW / 5));

				textFields[row][col].setAlignment(Pos.CENTER);
				textFields[row][col].setFont(Font.font("Verdana", FontWeight.BLACK, 20));
				grid.setPadding(new Insets(5, 5, 0, 5));
				grid.setVgap(GAP);
				grid.setHgap(GAP);
				grid.add(textFields[row][col], col, row);

				final int selectedRow = row;
				final int selectedCol = col;

				textFields[row][col].textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {

						// Make sure input isn't empty and is an number
						if (!newValue.isEmpty() && newValue.matches("\\d")) {

							// Parse to integer
							int number = Integer.parseInt(newValue);

							// Make sure its the right input
							if (number >= 1 && number <= NUMB_ROW) {

								// Check rows, columns and boxes
								if (!isValid(selectedRow, selectedCol, newValue)) {
									Platform.runLater(() -> {
										textFields[selectedRow][selectedCol].clear();
									});
								}

							} else {
								// Source: https://stackoverflow.com/a/32893573/8883030
								Platform.runLater(() -> {
									textFields[selectedRow][selectedCol].clear();
								});
							}
						} else {
							Platform.runLater(() -> {
								textFields[selectedRow][selectedCol].clear();
							});
						}
					}
				});
			}
		}
		borderPane.setCenter(grid);
	}

	/**
	 * Checks if the input is valid
	 * <p>
	 * Checks the row, column and sub grid box if the input is valid
	 * </p>
	 * 
	 * @param row   from changed textField
	 * @param col   from changed textField
	 * @param value from changed textField
	 * 
	 * @return false if input is not valid
	 */
	private boolean isValid(int row, int col, String value) {
		// Check row
		for (int i = 0; i < NUMB_COLUMN; i++) {
			if (col != i) {
				if (textFields[row][i].getText().equals(value)) {
					System.out.println("Row: nei");
					return false;
				}
			}
		}

		// Check column
		for (int j = 0; j < NUMB_ROW; j++) {
			if (row != j) {
				if (textFields[j][col].getText().equals(value)) {
					System.out.println("Col: nei");
					return false;
				}
			}
		}

		// Check box
		// Source: https://www.baeldung.com/java-sudoku
		int startRow = (row / SUB_GRID) * SUB_GRID;
		int startCol = (col / SUB_GRID) * SUB_GRID;

		int endRow = startRow + SUB_GRID;
		int endCol = startCol + SUB_GRID;

		for (int r = startRow; r < endRow; r++) {
			for (int c = startCol; c < endCol; c++) {
				if (r != row && c != col) {
					if (textFields[r][c].getText().equals(value)) {
						System.out.println("SubGrid: nei");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Reads the JSONarray
	 * 
	 * @return parsed jsonArray
	 */
	private int[][] getJson() {
		int[][] array = new int[NUMB_ROW][NUMB_COLUMN];

		try {
			BufferedReader buffer = new BufferedReader(new FileReader("board.json"));
			StringBuffer sb = new StringBuffer();
			String line;
			int j = 0;
			String[] temp = null;
			int sum = 0;
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}

			JSONArray json = new JSONArray(sb);

		} catch (FileNotFoundException e) {
			System.out.println("no");
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("nopp");
		}

		return array;
	}

}
