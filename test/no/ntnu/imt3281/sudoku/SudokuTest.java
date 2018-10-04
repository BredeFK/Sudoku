package no.ntnu.imt3281.sudoku;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.application.Platform;

public class SudokuTest {

	@Test
	public void testEmptyConstructor() {
		Sudoku sudoku = new Sudoku();
		assertTrue(sudoku instanceof Sudoku);
	}

	@Test
	public void testSudukoViewController() {
		Platform.startup(() -> {
			SudukoViewController controller = new SudukoViewController();
			assertTrue(controller instanceof SudukoViewController);
		});
	}
}
