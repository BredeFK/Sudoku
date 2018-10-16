package no.ntnu.imt3281.sudoku;

public class BadNumberException extends Exception {

	private static final long serialVersionUID = 409007870227134841L;
	private final int row;
	private final int col;
	private final String place;

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
		return String.format("%s Exception: Number already exists in Row: %d and Col: %d", place, row, col);
	}

}