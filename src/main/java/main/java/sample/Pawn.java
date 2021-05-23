package main.java.sample;

public class Pawn extends Figure {
	public Pawn(Board board, int color, int y, int x) {
		super(board, color, y, x);
		type = FigureType.PAWN;
	}

	@Override
	public String toString() {
		return "Pawn";
	}
	
	public boolean canMoveTo(int yPos, int xPos){
		if (canMoveFigures(yPos, xPos)) {
			int offset;
			if (this.getFigureColor() == WHITE) offset = -1;
			else offset = 1;

			if (Math.abs(this.getY() - yPos) == 2 && this.getFigureColor() == BLACK && this.getY() == 1 && this.getX() == xPos &&
					board.figureInCell(this.getY() + 1, this.getX()) == null &&
					board.figureInCell(this.getY() + 2, this.getX()) == null ) return true;

			if (Math.abs(this.getY() - yPos) == 2 && this.getFigureColor() == 1 && this.getY() == 6 && this.getX() == xPos &&
					board.figureInCell(this.getY() - 1, this.getX()) == null &&
					board.figureInCell(this.getY() - 2, this.getX()) == null) return true;

			if (board.insideBoard(this.getY() + offset, xPos) && board.figureInCell(this.getY() + offset, xPos) == null &&
					Math.abs(this.getY() - yPos) == 1 && this.getX() == xPos &&
					(this.getFigureColor() == BLACK && this.getY() < yPos || this.getFigureColor() == WHITE && this.getY() > yPos)) return true;

			if ((this.getFigureColor() == BLACK && this.getY() < yPos || this.getFigureColor() == WHITE && this.getY() > yPos) &&
					(Math.abs(this.getY() - yPos) == 1 && Math.abs(this.getX() - xPos) == 1 &&
					((board.insideBoard(this.getY() + offset, this.getX() + 1) &&
							board.figureInCell(this.getY() + offset, this.getX() + 1) != null &&
					board.figureInCell(this.getY() + offset, this.getX() + 1).getFigureColor() != this.getFigureColor()) ||
					(this.getY() + offset == board.getyPawnBrokenCell() && this.getX() + 1 == board.getxPawnBrokenCell() &&
							this.getFigureColor() != board.getColorOfPawnBrokenCell())))) return true;

			return (this.getFigureColor() == BLACK && this.getY() < yPos || this.getFigureColor() == WHITE && this.getY() > yPos) &&
					(Math.abs(this.getY() - yPos) == 1 && Math.abs(this.getX() - xPos) == 1 &&
							((board.insideBoard(this.getY() + offset, this.getX() - 1) &&
									board.figureInCell(this.getY() + offset, this.getX() - 1) != null &&
									board.figureInCell(this.getY() + offset, this.getX() - 1).getFigureColor() != this.getFigureColor()) ||
									(this.getY() + offset == board.getyPawnBrokenCell() && this.getX() - 1 == board.getxPawnBrokenCell() &&
											this.getFigureColor() != board.getColorOfPawnBrokenCell())));
		}
		return false;
	}
}