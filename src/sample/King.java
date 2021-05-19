package sample;

public class King extends Figure {
	public King(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "King";
	}

	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveFigures(yPos,xPos)) {
			return kingMove(yPos, xPos);
		} else return false;
	}

	private boolean kingMove(int yPos, int xPos) {
		int yMove = Math.abs(yPos - this.getY());
		int xMove = Math.abs(xPos - this.getX());

		if (xMove == 2 && yMove ==0) return true;
		if (yMove <= 1 && xMove <= 1){
			if (yMove == 0 && xMove == 0) return false;
			return true;
		} else return false;
	}
}
