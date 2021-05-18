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
		if(canMoveGenerics(yPos,xPos)) {
			return kingMove(yPos, xPos);
		} else return false;
	}

	private boolean kingMove(int yPos, int xPos) {
		int absoluteX = Math.abs(yPos - this.getY());
		int absoluteY = Math.abs(xPos - this.getX());
		
		if (absoluteX <= 1 && absoluteY <= 1){
			if (absoluteX == 0 && absoluteY == 0) return false;
			return true;
		} else return false;
	}
}
