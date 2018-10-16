package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BadNumberExceptionTest {
	private int row = 0;
	private int col = 0;
	private String place = "Box";

	@Test
	public void testConstructor() {
		BadNumberException exception = new BadNumberException(row, col, place);
		assertTrue(exception instanceof BadNumberException);
	}

	@Test
	public void testGetMessage() {
		BadNumberException exception = new BadNumberException(row, col, place);
		String message = String.format("%s Exception: Number already exists in Row: %d and Col: %d", place, row, col);
		assertEquals(message, exception.getMessage());
	}

}
