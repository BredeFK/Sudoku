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
public class ElementIsLockedExceptionTest {
	private int row = 0;
	private int col = 0;
	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	@Test
	public void testConstructor() {
		ElementIsLockedException exception = new ElementIsLockedException(row, col);
		assertTrue(exception instanceof ElementIsLockedException);
	}

	@Test
	public void testGetMessage() {
		ElementIsLockedException exception = new ElementIsLockedException(row, col);
		String expectedMessage = String.format("%s%d, %d%s", bundle.getString("lockedElement"), row, col,
				bundle.getString("locked"));
		String message = String.format("LockedElement Exception: Element(%d, %d) is locked!", row, col);
		assertEquals(expectedMessage, exception.getMessage());
	}

}
