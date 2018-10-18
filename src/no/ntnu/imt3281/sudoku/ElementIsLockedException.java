package no.ntnu.imt3281.sudoku;

import java.util.ResourceBundle;

/**
 * Exception that is called for when an element is locked
 * 
 * @author Fritjof
 *
 */
public class ElementIsLockedException extends Exception {

	private static final long serialVersionUID = -1122364685372394575L;
	private final int row;
	private final int col;
	private static String defaultLan = Main.defaultLan;
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	/**
	 * Constructor for the class
	 * 
	 * @param row int for the element
	 * @param col int for the element
	 */
	public ElementIsLockedException(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public String getMessage() {
		return String.format("%s%d, %d%s", bundle.getString("lockedElement"), row, col, bundle.getString("locked"));
	}

}
