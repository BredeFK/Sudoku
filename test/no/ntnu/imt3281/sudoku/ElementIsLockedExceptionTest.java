package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ElementIsLockedExceptionTest {
	private int row = 0;
	private int col = 0;

	@Test
	public void testConstructor() {
		ElementIsLockedException exception = new ElementIsLockedException(row, col);
		assertTrue(exception instanceof ElementIsLockedException);
	}

	@Test
	public void testGetMessage() {
		ElementIsLockedException exception = new ElementIsLockedException(row, col);
		String message = String.format("LockedElement Exception: Element(%d, %d) is locked!", row, col);
		assertEquals(message, exception.getMessage());
	}

}
