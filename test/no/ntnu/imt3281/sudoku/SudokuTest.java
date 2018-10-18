package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class SudokuTest {
	private Sudoku sudoku = new Sudoku();
	private static int NUMB_ROW = Sudoku.NUMB_ROW;
	private static int NUMB_COLUMN = Sudoku.NUMB_COLUMN;
	private static int SUB_GRID = Sudoku.SUB_GRID;
	private String emptyElement = " ";

	/**
	 * Helping function to get a board in string array
	 * 
	 * @return boardStringArray String[][]
	 */
	private String[][] getJsonBoardStringArray() {
		int[][] boardIntArray = sudoku.getJson("board.json", "UTF-8");
		String[][] boardStringArray = new String[NUMB_ROW][NUMB_COLUMN];
		for (int i = 0; i < NUMB_ROW; i++) {
			for (int j = 0; j < NUMB_COLUMN; j++) {
				if (boardIntArray[i][j] != -1) {
					boardStringArray[i][j] = (boardIntArray[i][j] + "");
				} else {
					boardStringArray[i][j] = emptyElement;
				}
			}
		}

		return boardStringArray;
	}

	@Test
	public void testEmptyConstructor() {
		Sudoku sudoku = new Sudoku();
		assertTrue(sudoku instanceof Sudoku);
	}

	@Test
	public void testGenerateAndCheck() {
		// Probably not going to test this function :/
	}

	@Test
	public void testGetElementInArray() {
		String result = sudoku.getElementInArray(0, 0);

		assertEquals("", result);
	}

	@Test
	public void testSetElementInArray() {
		String value = "9";

		sudoku.setElementinArray(5, 5, value);
		String result = sudoku.getElementInArray(5, 5);

		assertEquals(value, result);

	}

	@Test
	public void testUpdateArray() {
		// Probably not going to test this function :/
	}

	@Test
	public void testIsValid() {
		String[][] testArray = getJsonBoardStringArray();
		try {
			// Test empty input
			boolean result = sudoku.isValid(0, 0, "", testArray);
			assertEquals(false, result);

			// Test not number input
			result = sudoku.isValid(0, 0, "b", testArray);
			assertEquals(false, result);

			// Test below 1 input
			result = sudoku.isValid(0, 0, "0", testArray);
			assertEquals(false, result);

			// Test above 9 input
			result = sudoku.isValid(0, 0, "10", testArray);
			assertEquals(false, result);

			// Test valid input
			result = sudoku.isValid(0, 2, "1", testArray);
			assertEquals(true, result);
		} catch (BadNumberException e) {

		}

	}

	@Test
	public void testValidRow() {

		String[][] testArray = getJsonBoardStringArray();

		try {
			sudoku.isValid(0, 6, "3", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains("Row") && e.getMessage().contains("Row: 0 and Col: 1"));
		}
	}

	@Test
	public void testValidCol() {
		String[][] testArray = getJsonBoardStringArray();

		try {
			sudoku.isValid(0, 7, "6", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains("Column") && e.getMessage().contains("Row: 2 and Col: 7"));
		}

	}

	@Test
	public void testValidBox() {
		String[][] testArray = getJsonBoardStringArray();

		try {
			sudoku.isValid(5, 3, "3", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains("Box") && e.getMessage().contains("Row: 4 and Col: 5"));
		}
	}

	@Test
	public void testGetIteratorRow() {
		String[][] testArray = getJsonBoardStringArray();
		Iterator<String> testIterator = sudoku.getIteratorRow(7, testArray);
		int col = 0;

		while (testIterator.hasNext()) {
			assertEquals(testArray[7][col], testIterator.next());
			col++;
		}

	}

	@Test
	public void testGetIteratorCol() {
		String[][] testArray = getJsonBoardStringArray();
		Iterator<String> testIterator = sudoku.getIteratorCol(0, testArray);
		int row = 0;

		while (testIterator.hasNext()) {
			assertEquals(testArray[row][0], testIterator.next());
			row++;
		}
	}

	@Test
	public void testGetIteratorBox() {
		String[][] testArray = getJsonBoardStringArray();
		Iterator<String> testIterator = sudoku.getIteratorBox(4, 4, testArray);

		int startRow = (4 / SUB_GRID) * SUB_GRID;
		int startCol = (4 / SUB_GRID) * SUB_GRID;

		for (int row = startRow; row < (startRow + SUB_GRID); row++) {
			for (int col = startCol; col < (startCol + SUB_GRID); col++) {
				assertEquals(testArray[row][col], testIterator.next());
			}
		}

	}

	@Test
	public void testGetJson() {
		int[][] result = sudoku.getJson("board.json", "UTF-8");
		int[][] expected = { { 5, 3, -1, -1, 7, -1, -1, -1, -1 }, { 6, -1, -1, 1, 9, 5, -1, -1, -1 },
				{ -1, 9, 8, -1, -1, -1, -1, 6, -1 }, { 8, -1, -1, -1, 6, -1, -1, -1, 3 },
				{ 4, -1, -1, 8, -1, 3, -1, -1, 1 }, { 7, -1, -1, -1, 2, -1, -1, -1, 6 },
				{ -1, 6, -1, -1, -1, -1, 2, 8, -1 }, { -1, -1, -1, 4, 1, 9, -1, -1, 5 },
				{ -1, -1, -1, -1, 8, -1, -1, 7, 9 } };

		for (int i = 0; i < NUMB_ROW; i++) {
			for (int j = 0; j < NUMB_COLUMN; j++) {
				assertEquals(expected[i][j], result[i][j]);
			}
		}

		result = sudoku.getJson("Wrong Name", "UTF-8");
		assertNull(result);

		result = sudoku.getJson("board.json", "Wrong Name");
		assertNull(result);

	}

	@Test
	public void testNewBoard() {

	}

	@Test
	public void testInitializeBoard() {

	}

	@Test
	public void testMirrorBoard() {

	}

	@Test
	public void testFlipBoard() {

	}

	@Test
	public void testFlipBlueBoard() {

	}

	@Test
	public void testSwitchNumbersOnBoard() {

	}

	@Test
	public void testConvertTo2dInt() {

	}

	@Test
	public void testGetRandomNumbers() {

	}

}
