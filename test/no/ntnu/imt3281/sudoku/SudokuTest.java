package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertEquals;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

public class SudokuTest {
	private Sudoku sudoku = new Sudoku();
	private static final Logger logger = Logger.getLogger(Sudoku.class.getName());

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

	}

	@Test
	public void testIsValid() {

	}

	@Test
	public void testGetIteratorRow() {

	}

	@Test
	public void testGetIteratorCol() {

	}

	@Test
	public void testGetIteratorBox() {

	}

	@Test
	public void testGetJson() {

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
