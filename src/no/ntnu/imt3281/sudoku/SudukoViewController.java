package no.ntnu.imt3281.sudoku;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SudukoViewController {

	private static final int NUMB_ROW = 9;
	private static final int NUMB_COLUMN = NUMB_ROW;
	private static final int SUB_GRID = NUMB_ROW / 3;
	private TextField[][] textFields;

	public SudukoViewController() {
		textFields = new TextField[NUMB_ROW][NUMB_COLUMN];
	}

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btn_Easy;

	@FXML
	private Button btn_Medium;

	@FXML
	private GridPane gridID;

	@FXML
	private ToolBar toolBar;

	@FXML
	void onEasyClick(ActionEvent event) {
		System.out.println("Easy button");
		generate();
	}

	@FXML
	void onMediumClick(ActionEvent event) {
		System.out.println("Medium button");
	}

	private void generate() {
		GridPane grid = new GridPane();
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {

				textFields[row][col] = new TextField();

				textFields[row][col].setMinHeight((borderPane.getHeight() - toolBar.getHeight()) / NUMB_COLUMN);
				textFields[row][col].setMaxWidth(borderPane.getWidth() / NUMB_ROW);

				grid.add(textFields[row][col], col, row);

				final int selectedRow = row;
				final int selectedCol = col;
				// field.setText(selectedRow + ", " + selectedCol);

				textFields[row][col].textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {

						// Make sure input isn't empty and is an number
						if (!newValue.isEmpty() && newValue.matches("\\d")) {

							// Parse to int
							int number = Integer.parseInt(newValue);

							// Make sure its the right input
							if (number >= 1 && number <= NUMB_ROW) {

								// Check rows, columns and boxes
								check(selectedRow, selectedCol, newValue);

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

	private void check(int row, int col, String value) {
		// Check row
		for (int i = 0; i < NUMB_COLUMN; i++) {
			if (col != i) {
				if (textFields[row][i].getText().equals(value)) {
					Platform.runLater(() -> {
						textFields[row][col].clear();
					});

					System.out.println("Row: nei");
				}
			}
		}

		// Check column
		for (int j = 0; j < NUMB_ROW; j++) {
			if (row != j) {
				if (textFields[j][col].getText().equals(value)) {
					Platform.runLater(() -> {
						textFields[row][col].clear();
					});
					System.out.println("Col: nei");
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
						Platform.runLater(() -> {
							textFields[row][col].clear();
						});
						System.out.println("SubGrid: nei");
					}
				}
			}
		}

	}
}
