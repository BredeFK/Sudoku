package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * 
 * @author Fritjof
 *
 */
public class SudokuTest {
	private Sudoku sudoku = new Sudoku();
	private static int NUMB_ROW = Sudoku.NUMB_ROW;
	private static int NUMB_COLUMN = Sudoku.NUMB_COLUMN;
	private static int SUB_GRID = Sudoku.SUB_GRID;
	private String emptyElement = "";
	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);
	private String row = bundle.getString("row");
	private String col = bundle.getString("col");
	private String box = bundle.getString("box");
	private String andColumn = bundle.getString("badNumberException2");

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
	public void testGetElementInArray() {
		String result = sudoku.getElementInArray(0, 0);

		assertEquals("", result);
	}

	@Test
	public void testSetElementInArray() {
		String value = "9";

		try {
			sudoku.setElementinArray(5, 5, value);
		} catch (ElementIsLockedException e) {
			// do nothing
		}
		String result = sudoku.getElementInArray(5, 5);

		try {
			sudoku.setElementinArray(5, 5, value);
		} catch (ElementIsLockedException e) {
			String expectedMessage = String.format("%s%d, %d%s", bundle.getString("lockedElement"), 5, 5,
					bundle.getString("locked"));
			assertEquals(expectedMessage, e.getMessage());
		}

		assertEquals(value, result);

	}

	@Test
	public void testUpdateArray() {
		sudoku.updateArray(8, 8, "0");
		String result = sudoku.getElementInArray(8, 8);

		assertEquals("0", result);
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
		String rowAndColumn = String.format("%s: 0 %s 1", row, andColumn);

		try {
			sudoku.isValid(0, 6, "3", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains(row) && e.getMessage().contains(rowAndColumn));
		}
	}

	@Test
	public void testValidCol() {
		String[][] testArray = getJsonBoardStringArray();
		String rowAndColumn = String.format("%s: 2 %s 7", row, andColumn);

		try {
			sudoku.isValid(0, 7, "6", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains(col) && e.getMessage().contains(rowAndColumn));
		}

	}

	@Test
	public void testValidBox() {
		String[][] testArray = getJsonBoardStringArray();
		String rowAndColumn = String.format("%s: 4 %s 5", row, andColumn);

		try {
			sudoku.isValid(5, 3, "3", testArray);
		} catch (BadNumberException e) {
			assertEquals(true, e.getMessage().contains(box) && e.getMessage().contains(rowAndColumn));
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
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.newBoard(array);

		assertEquals("5", result[0][0]);
		assertEquals("6", result[3][4]);
		assertEquals("8", result[4][3]);
		assertEquals("3", result[4][5]);
		assertEquals("2", result[5][4]);
		assertEquals("9", result[8][8]);

	}

	@Test
	public void testInitializeBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		array[0][0] = "1";
		array[8][0] = "2";
		array[0][8] = "3";
		array[8][8] = "4";

		String[][] result = sudoku.initializeBoard(array);
		assertEquals("", result[0][0]);
		assertEquals("", result[8][0]);
		assertEquals("", result[0][8]);
		assertEquals("", result[8][8]);
	}

	@Test
	public void testMirrorBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.mirrorBoard(sudoku.newBoard(array));

		assertEquals("5", result[0][8]);
		assertEquals("6", result[3][4]);
		assertEquals("8", result[4][5]);
		assertEquals("3", result[4][3]);
		assertEquals("2", result[5][4]);
		assertEquals("9", result[8][0]);
	}

	@Test
	public void testFlipBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.flipBoard(sudoku.newBoard(array));

		assertEquals("5", result[8][0]);
		assertEquals("6", result[5][4]);
		assertEquals("8", result[4][3]);
		assertEquals("3", result[4][5]);
		assertEquals("2", result[3][4]);
		assertEquals("9", result[0][8]);
	}

	@Test
	public void testFlipBlueBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.flipBlueBoard(sudoku.newBoard(array));

		assertEquals("5", result[0][0]);
		assertEquals("6", result[4][3]);
		assertEquals("8", result[3][4]);
		assertEquals("3", result[5][4]);
		assertEquals("2", result[4][5]);
		assertEquals("9", result[8][8]);
	}

	@Test
	public void testFlipRedBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.flipRedBoard(sudoku.newBoard(array));

		assertEquals("5", result[8][8]);
		assertEquals("6", result[4][5]);
		assertEquals("8", result[5][4]);
		assertEquals("3", result[3][4]);
		assertEquals("2", result[4][3]);
		assertEquals("9", result[0][0]);
	}

	@Test
	public void testSwitchNumbersOnBoard() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		String[][] result = sudoku.switchNumbersOnBoard(sudoku.newBoard(array));
		String[][] beforeSwitch = getJsonBoardStringArray();

		for (int i = 0; i < NUMB_ROW; i++) {
			for (int j = 0; j < NUMB_COLUMN; j++) {
				if (beforeSwitch[i][j] != "" && result[i][j] != "") {
					assertNotEquals(beforeSwitch[i][j], result[i][j]);
				}
			}
		}
	}

	@Test
	public void testIsLocked() {
		sudoku.lockElement(3, 4);

		boolean result = sudoku.isLocked(3, 4);
		assertTrue(result);
	}

	@Test
	public void testConvertTo2dInt() {
		String[][] array = new String[NUMB_ROW][NUMB_COLUMN];
		int[][] result = sudoku.convertTo2dInt(sudoku.newBoard(array));
		String[][] beforeConverting = getJsonBoardStringArray();

		for (int i = 0; i < NUMB_ROW; i++) {
			for (int j = 0; j < NUMB_COLUMN; j++) {
				if (result[i][j] != -1 && beforeConverting[i][j] != "") {
					assertEquals(beforeConverting[i][j], (result[i][j] + ""));
				} else {
					assertEquals(-1, result[i][j]);
				}
			}
		}
	}
}
