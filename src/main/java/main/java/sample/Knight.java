package main.java.sample;

public class Knight extends Figure {
	public Knight(Board board, int color, int y, int x) {
		super(board, color, y, x);
		type = FigureType.KNIGHT;
	}

	@Override
	public String toString() {
		return "Knight";
	}

	public boolean canMoveTo(int yPos, int xPos) {
		if (canMoveFigures(yPos,xPos)) {
			if (Math.abs(this.getY() - yPos) == 2 && Math.abs(this.getX() - xPos) == 1) return true;
			return Math.abs(this.getY() - yPos) == 1 && Math.abs(this.getX() - xPos) == 2;
		}
		return false;
	}
}