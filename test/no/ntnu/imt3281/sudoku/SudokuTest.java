package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertEquals;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
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
	public void testGetElement() {

		/*
		try {
			sudoku.setElement(0, 0, 9);
			String result = sudoku.getElement(0, 0);
			assertEquals("9", result);
			logger.log(Level.INFO, "Expected result: 9%nActual result: %d", result);
		} catch (ElementIsLockedException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
		*/

	}

	@Test
	public void testSetElement() {

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
	public void testCheckIfCompleted() {

	}

	@Test
	public void testIsLocked() {

	}

	@Test
	public void testLockElement() {

	}

	@Test
	public void testUnlockElement() {

	}

	@Test
	public void testGetRandomNumbers() {

	}

}
