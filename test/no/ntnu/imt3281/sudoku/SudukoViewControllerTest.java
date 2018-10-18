package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.junit.Test;

import javafx.application.Platform;

public class SudukoViewControllerTest {

	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	@Test
	public void testSudukoViewController() {
		Platform.startup(() -> {
			SudukoViewController controller = new SudukoViewController();
			assertTrue(controller instanceof SudukoViewController);
		});
	}

	@Test
	public void testGenerateBoard() {

	}

	@Test
	public void testLockElement() {

	}

	@Test
	public void testUnlockElement() {

	}

	@Test
	public void testIsLocked() {

	}

	@Test
	public void testCheckIfCompleted() {

	}

	@Test
	public void testSetVisibilityCompleted() {

	}

	@Test
	public void testSetStyleWrong() {

	}

	@Test
	public void testSetStyleRed() {

	}

	@Test
	public void testgetElement() {

	}

	@Test
	public void testSetElement() {

	}

}
