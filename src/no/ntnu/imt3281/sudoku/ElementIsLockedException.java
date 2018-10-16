package no.ntnu.imt3281.sudoku;

public class ElementIsLockedException extends Exception {

	private static final long serialVersionUID = -1122364685372394575L;
	private final int row;
	private final int col;

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
		return String.format("LockedElement Exception: Element(%d, %d) is locked!", row, col);
	}

}
