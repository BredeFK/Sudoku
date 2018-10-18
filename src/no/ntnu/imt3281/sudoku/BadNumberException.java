package no.ntnu.imt3281.sudoku;

import java.util.ResourceBundle;

/**
 * @author Brede Fritjof Klausen
 */
public class BadNumberException extends Exception {

	private static final long serialVersionUID = 409007870227134841L;
	private final int row;
	private final int col;
	private final String place;
	private static String defaultLan = Main.defaultLan;// "no.ntnu.imt3281.sudoku.MessagesBundle";
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	/**
	 * 
	 * @param row   int row number
	 * @param col   int column number
	 * @param place String row/col/box
	 * 
	 */
	public BadNumberException(int row, int col, String place) {
		this.row = row;
		this.col = col;
		this.place = place;
	}

	@Override
	public String getMessage() {
		return String.format("%s %s %d %s %d", place, bundle.getString("badNumberException1"), row,
				bundle.getString("badNumberException2"), col);
	}

}
