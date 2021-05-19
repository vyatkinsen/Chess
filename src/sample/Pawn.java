package sample;

public class Pawn extends Figure {
	
	public Pawn(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "Pawn";
	}
	
	public boolean canMoveTo(int yPos, int xPos){
		if(canMoveFigures(yPos, xPos)) return pawnMove(yPos, xPos);
		else return false;
	}


	private boolean pawnMove(int yPos, int xPos){
		int oneCell;
		int twoCell;
		Figure target = board.figureInCell(yPos, xPos);
		
		if (this.getFigureColor() == 0) {
			oneCell = 1;
			twoCell = 2;
		} else {
			oneCell = -1;
			twoCell = -2;
		}
		if (yPos - this.getY() == oneCell && (xPos == this.getX() && target == null || Math.abs(this.getX() - xPos) == 1 && target != null)) {
			return true;
		} else if (!isMoved && yPos - this.getY() == twoCell && xPos == this.getX() && board.figureInCell(yPos + twoCell, xPos) == null) {
			return true;
		} else return false;
	}
}
