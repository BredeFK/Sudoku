package no.ntnu.imt3281.sudoku;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.swing.text.ChangedCharSetException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

public class SudukoViewController {
	
	private static final int NUMB_ROW = 9;
	private static final int NUMB_COLUMN = 9;
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
		for(int row = 0; row < NUMB_ROW; row++) {
			for(int col = 0; col < NUMB_COLUMN; col++) {
				
				TextField field = new TextField();
				field.setMinHeight((borderPane.getHeight()-toolBar.getHeight())/NUMB_COLUMN);
				field.setMaxWidth(borderPane.getWidth()/NUMB_ROW);
				textFields[row][col] = field;
				
				grid.add(field, col, row);
				
				final int selectedRow = row;
				final int selectedCol = col;
				
				textFields[row][col].textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						
						// Make sure input isn't empty and is an number
						if(!newValue.isEmpty() && newValue.matches("\\d+")) {
							
								// Parse to int
								int number = Integer.parseInt(newValue);
								
								// Make sure its the right input
								if(number >= 1 && number <= 9) {
									
									// Check rows, columns and boxes
									check(selectedRow, selectedCol, newValue);
									
								} else {
									textFields[selectedRow][selectedCol].setText("");
								}
							} else {
								textFields[selectedRow][selectedCol].setText("");
							}
					}
				});
				
			}
			
		}
		
		borderPane.setCenter(grid);
    }
    
    private void check(int row, int col, String value) {
    	// Check row
    	for(int i = 0; i < NUMB_COLUMN; i++) {
    		if(col != i) {
	    		if(textFields[row][i].getText().equals(value)){
	    			textFields[row][col].setText("");
	    			System.out.println("Row: nei");
	    		} 
    		}
    	}
    	
    	// Check column
    	for(int j = 0; j < NUMB_ROW; j++) {
    		if(row != j) {
    			if(textFields[j][col].getText().equals(value)) {
    				textFields[row][col].setText("");
    				System.out.println("Col: nei");
    			}
    		}
    	}
    	
    	// Check box
    	
    }

}
