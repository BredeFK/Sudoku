package no.ntnu.imt3281.sudoku;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the logic for the Sudoku application
 * 
 * @author Fritjof
 *
 */
public class Sudoku {

	protected static final int NUMB_ROW = 9;
	protected static final int NUMB_COLUMN = NUMB_ROW;
	protected static final int SUB_GRID = NUMB_ROW / 3;
	private static final Logger logger = Logger.getLogger(Sudoku.class.getName());
	private static String[][] stringArray = new String[NUMB_ROW][NUMB_COLUMN];
	private String file = "board.json";
	private static String defaultLan = Main.defaultLan;
	private String encoding = "UTF-8";
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);
	private boolean[][] isLocked = new boolean[NUMB_ROW][NUMB_COLUMN];

	/**
	 * Constructor for the class Sudoku
	 */
	public Sudoku() {
		// Initialize stringArray
		for (int i = 0; i < NUMB_ROW; i++) {
			for (int j = 0; j < NUMB_COLUMN; j++) {
				stringArray[i][j] = "";
				isLocked[i][j] = false;
			}
		}
	}

	/**
	 * Sets an element in array if it's not locked
	 * 
	 * @param row      int for the selected element
	 * @param col      int for the selected element
	 * @param newValue String for the selected element
	 * @throws ElementIsLockedException if element is locked
	 */
	protected void setElementinArray(int row, int col, String newValue) throws ElementIsLockedException {
		if (!isLocked(row, col)) {
			stringArray[row][col] = newValue;
			lockElement(row, col);
		} else {
			throw new ElementIsLockedException(row, col);
		}

	}

	/**
	 * Gets the element in the array
	 * 
	 * @param row int for the selected element
	 * @param col int for the selected element
	 * 
	 * @return stringArray[row][col] String Element on row,col
	 */
	protected String getElementInArray(int row, int col) {
		return stringArray[row][col];
	}

	/**
	 * Updates the String array
	 *
	 * @param row      for the selected element
	 * @param col      for the selected element
	 * @param newValue for the selected element
	 */
	protected void updateArray(int row, int col, String newValue) {
		stringArray[row][col] = newValue;
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
	protected boolean isValid(int row, int col, String value, String[][] array) throws BadNumberException {

		// Check if empty, is not a number and number is between 1 and 9
		if (value.isEmpty() || !value.matches("\\d") || (Integer.parseInt(value) < 1 || Integer.parseInt(value) > 9)) {
			return false;
		}

		// Check row
		int i = 0;
		Iterator<String> rowIterator = getIteratorRow(row, array);
		while (rowIterator.hasNext()) {
			if (rowIterator.next().equals(value) && i != col) {
				// If the number is not the selected number
				throw new BadNumberException(row, i, bundle.getString("row"));
			}
			i++;
		}

		// Check column
		i = 0;
		Iterator<String> colIterator = getIteratorCol(col, array);
		while (colIterator.hasNext()) {
			if (colIterator.next().equals(value) && i != row) {
				// If the number is not the selected number
				throw new BadNumberException(i, col, bundle.getString("col"));
			}
			i++;
		}

		// Check sub-grid
		int startRow = (row / SUB_GRID) * SUB_GRID;
		int startCol = (col / SUB_GRID) * SUB_GRID;
		int j;
		Iterator<String> boxIterator = getIteratorBox(row, col, array);
		for (i = startRow; i < startRow + SUB_GRID; i++) {
			for (j = startCol; j < startCol + SUB_GRID; j++) {
				if (boxIterator.next().equals(value) && i != row && j != col) {
					// If the number is not the selected number
					throw new BadNumberException(i, j, bundle.getString("box"));
				}
			}
		}

		// Input is valid
		return true;
	}

	/**
	 * Returns an iterator object for the row
	 * 
	 * @param row int The selected row
	 * 
	 * @return Iterator<String> the preferred row
	 */
	protected Iterator<String> getIteratorRow(int row, String[][] array) {
		ArrayList<String> arrayListRow = new ArrayList<>();
		for (int col = 0; col < NUMB_COLUMN; col++) {
			arrayListRow.add(array[row][col]);
		}
		return arrayListRow.iterator();
	}

	/**
	 * Returns an iterator object for the column
	 * 
	 * @param col int The selected column
	 * 
	 * @return Iterator<String> the preferred column
	 */
	protected Iterator<String> getIteratorCol(int col, String[][] array) {
		ArrayList<String> arrayListCol = new ArrayList<>();
		for (int row = 0; row < NUMB_ROW; row++) {
			arrayListCol.add(array[row][col]);
		}
		return arrayListCol.iterator();
	}

	/**
	 * Returns an iterator object for the sub-grid box<br>
	 * 
	 * Source for checking sub-grid: {@link https://www.baeldung.com/java-sudoku}
	 * 
	 * @param row int The selected row
	 * @param col int The selected column
	 * 
	 * @return Iterator<String> The preferred box-grid
	 * 
	 */
	protected Iterator<String> getIteratorBox(int row, int col, String[][] array) {
		ArrayList<String> arrayListBox = new ArrayList<>();
		int startRow = (row / SUB_GRID) * SUB_GRID;
		int startCol = (col / SUB_GRID) * SUB_GRID;

		int endRow = startRow + SUB_GRID;
		int endCol = startCol + SUB_GRID;

		for (int r = startRow; r < endRow; r++) {
			for (int c = startCol; c < endCol; c++) {
				arrayListBox.add(array[r][c]);
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
	 * @param fileName String name of the file
	 * @param encoding String name of encoding
	 * @return array int[][] converted json file
	 */
	protected int[][] getJson(String fileName, String encoding) {
		int[][] array = new int[NUMB_ROW][NUMB_COLUMN];

		try (BufferedReader buffer = new BufferedReader(
				new InputStreamReader(new FileInputStream(fileName), encoding))) {
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
			logger.log(Level.WARNING, String.format("%s %s%n", bundle.getString("notFound"), e.getMessage()));
		} catch (IOException e) {
			array = null;
			logger.log(Level.WARNING, String.format("%s %s%n", bundle.getString("ioException"), e.getMessage()));
		}

		return array;
	}

	/**
	 * Unlocks all the elements on the board
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] initializeBoard(String[][] array) {
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				array[row][col] = "";
				isLocked[row][col] = false;
			}
		}
		return array;
	}

	/**
	 * Fill the board with numbers
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] newBoard(String[][] array) {
		int[][] temp = getJson(file, encoding);

		if (temp != null) {
			array = initializeBoard(array);

			for (int row = 0; row < NUMB_ROW; row++) {
				for (int col = 0; col < NUMB_COLUMN; col++) {
					if (temp[row][col] != -1) {
						// if number is empty
						array[row][col] = (temp[row][col] + "");
						lockElement(row, col);
					}
				}
			}
		}

		return array;
	}

	/**
	 * Mirror the board
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] mirrorBoard(String[][] array) {
		int[][] temp = convertTo2dInt(array);

		array = initializeBoard(array);
		int mirrorCol = 0;
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				// -1 because it goes to from 0 to 8
				mirrorCol = (NUMB_COLUMN - 1) - col;
				if (temp[row][mirrorCol] != -1) {
					// if number is empty
					array[row][col] = (temp[row][mirrorCol] + "");
					lockElement(row, col);
				}
			}
		}
		return array;
	}

	/**
	 * Flip the board upside down
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] flipBoard(String[][] array) {
		int[][] temp = convertTo2dInt(array);

		array = initializeBoard(array);
		int mirrorRow = 0;
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				// -1 because it goes to from 0 to 8
				mirrorRow = (NUMB_ROW - 1) - row;

				if (temp[mirrorRow][col] != -1) {
					// if number is empty
					array[row][col] = (temp[mirrorRow][col] + "");
					lockElement(row, col);
				}
			}
		}
		return array;
	}

	/**
	 * Flips the board from top-right to bottom-left
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] flipBlueBoard(String[][] array) {
		int[][] temp = convertTo2dInt(array);

		array = initializeBoard(array);
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (temp[col][row] != -1) {
					// if number is empty
					array[row][col] = (temp[col][row] + "");
					lockElement(row, col);
				}
			}
		}
		return array;
	}

	/**
	 * Flips the board from bottom-left to top-right
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] flipRedBoard(String[][] array) {
		int[][] temp = convertTo2dInt(array);

		array = initializeBoard(array);
		int newRow = 0;
		int newCol = 0;
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				newRow = (NUMB_ROW - 1) - row;
				newCol = (NUMB_COLUMN - 1) - col;

				if (temp[newCol][newRow] != -1) {
					// if number is empty
					array[row][col] = (temp[newCol][newRow] + "");
					lockElement(row, col);
				}
			}
		}
		return array;
	}

	/**
	 * Switches numbers on the board
	 * 
	 * @param array String[][] array to change
	 * @return array String[][] changed array
	 */
	protected String[][] switchNumbersOnBoard(String[][] array) {
		int[][] temp = convertTo2dInt(array);
		ArrayList<Integer> numbers = getRandomNumbers();

		array = initializeBoard(array);

		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (temp[row][col] != -1) {
					// if number is empty
					array[row][col] = (numbers.get(temp[row][col] - 1) + "");
					lockElement(row, col);
				}
			}
		}
		return array;
	}

	/**
	 * Checks if the element in array is locked
	 * 
	 * @param row int selected row
	 * @param col int selected column
	 * 
	 * @return true/false if locked
	 */
	protected boolean isLocked(int row, int col) {
		return isLocked[row][col];
	}

	/**
	 * Locks the element in String array
	 * 
	 * @param row selected row
	 * @param col selected column
	 */
	protected void lockElement(int row, int col) {
		isLocked[row][col] = true;
	}

	/**
	 * Converts String array to int array where empty is -1
	 * 
	 * @param array String[][] from textFields
	 * 
	 * @return returnArray int[][] converted to int array
	 */
	protected int[][] convertTo2dInt(String[][] array) {
		int[][] returnArray = new int[NUMB_ROW][NUMB_COLUMN];
		for (int row = 0; row < NUMB_ROW; row++) {
			for (int col = 0; col < NUMB_COLUMN; col++) {
				if (isLocked(row, col)) {
					returnArray[row][col] = Integer.parseInt(array[row][col]);
				} else {
					returnArray[row][col] = -1;
				}
			}
		}
		return returnArray;
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
				// if list doesn't contain number and number isn't the same value as the number
				// it's going to change: add number
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
