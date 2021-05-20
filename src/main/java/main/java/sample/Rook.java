package main.java.sample;

public class Rook extends Figure {
	public Rook(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "Rook";
	}
	
	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveFigures(yPos,xPos)) {
			return verticalAndHorizontal(yPos, xPos);
		} else return false;
	}
}