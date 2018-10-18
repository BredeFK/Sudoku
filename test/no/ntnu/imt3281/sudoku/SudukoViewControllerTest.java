package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.junit.Test;

import javafx.application.Platform;

/**
 * 
 * @author Fritjof
 *
 */
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
}
