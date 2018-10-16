package no.ntnu.imt3281.sudoku;

/**
 * @author Brede Fritjof Klausen
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.application.Platform;

public class SudukoViewControllerTest {
	@Test
	public void testSudukoViewController() {
		Platform.startup(() -> {
			SudukoViewController controller = new SudukoViewController();
			assertTrue(controller instanceof SudukoViewController);
		});
	}

}
