package main.java.sample;

import javafx.scene.image.ImageView;

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
				if ((yPos == 0 && this.getFigureColor() == 1) || (yPos == 7 && this.getFigureColor() == 0)){
					if (this.getFigureColor() == 1) {
						board.setFigureOnBoard(new Queen(board, 1, yPos, xPos), yPos, xPos);
					} else {
						board.setFigureOnBoard(new Queen(board, 0, yPos, xPos), yPos, xPos);
					}
				}
				return true;
			} else return !isMoved && yPos - this.getY() == twoCell && xPos == this.getX() && board.figureInCell(this.getY() + oneCell, xPos) == null && board.figureInCell(this.getY() + twoCell, xPos) == null;
		}
		else return false;
	}
}