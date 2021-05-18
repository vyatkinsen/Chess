package sample;

public class Queen extends Figure {
	public Queen(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "Queen";
	}

	@Override
	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveGenerics(yPos, xPos)) {
			return verticalAndHorizontal(yPos, xPos) || diagonal(yPos, xPos);
		} else return false;
	}
}
