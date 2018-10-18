package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.junit.Test;

/**
 * 
 * @author Fritjof
 *
 */
public class BadNumberExceptionTest {
	private int row = 0;
	private int col = 0;
	private String place = "Box";
	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	@Test
	public void testConstructor() {
		BadNumberException exception = new BadNumberException(row, col, place);
		assertTrue(exception instanceof BadNumberException);
	}

	@Test
	public void testGetMessage() {
		BadNumberException exception = new BadNumberException(row, col, place);
		String expectedMessage = String.format("%s %s %d %s %d", place, bundle.getString("badNumberException1"), row,
				bundle.getString("badNumberException2"), col);
		String message = String.format("%s Exception: Number already exists in Row: %d and Column: %d", place, row,
				col);
		assertEquals(expectedMessage, exception.getMessage());
	}

}
