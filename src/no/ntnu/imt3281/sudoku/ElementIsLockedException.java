package no.ntnu.imt3281.sudoku;

import java.util.ResourceBundle;

/**
 * @author Brede Fritjof Klausen
 */
public class ElementIsLockedException extends Exception {

	private static final long serialVersionUID = -1122364685372394575L;
	private final int row;
	private final int col;
	private static String defaultLan = Main.defaultLan;// = "no.ntnu.imt3281.sudoku.MessagesBundle";
	private ResourceBundle bundle = ResourceBundle.getBundle(defaultLan);

	/**
	 * @param row
	 * @param col
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
