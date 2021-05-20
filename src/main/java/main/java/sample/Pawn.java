package main.java.sample;

public class Pawn extends Figure {
	public Pawn(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "Pawn";
	}
	
	public boolean canMoveTo(int yPos, int xPos){
		if(canMoveFigures(yPos, xPos)) {
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

			if (yPos - this.getY() == oneCell && (xPos == this.getX() && target == null || Math.abs(this.getX() - xPos) == 1 && target != null) || canBrokeCell) {
				canBrokeCell = false;
				return true;
			} else if (!isMoved && yPos - this.getY() == twoCell && xPos == this.getX() &&  board.figureInCell(this.getY() + oneCell, xPos) == null && board.figureInCell(this.getY() + twoCell, xPos) == null) {
				return true;
			} else return false;
		}
		else return false;
	}
}