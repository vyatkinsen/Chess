package main.java.sample;
import java.util.LinkedList;

public class King extends Figure {

	public King(Board board, int color, int y, int x) {
		super(board, color, y, x);
		type = FigureType.KING;
	}

	@Override
	public String toString() {
		return "King";
	}

	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveFigures(yPos, xPos)) {
			int yMove = Math.abs(yPos - this.getY());
			int xMove = Math.abs(xPos - this.getX());

			if (xMove == 2 && yMove == 0 && !isMoved) {
				switch (this.getY()) {
					case 0 -> {
						if (xPos == 6 && board.figureInCell(yPos, 7) != null && !board.figureInCell(yPos, 7).getIsMoved() &&
								board.figureInCell(0, 5) == null && board.figureInCell(0, 6) == null &&
								!isCastling && board.figureInCell(0, 7) != null && board.figureInCell(0, 7).getType() == FigureType.ROOK &&
								!board.figureInCell(0, 7).getIsMoved() && board.isCellNotBroken(0, 0, 5)) {
							return true;
						} else if (xPos == 2 && board.figureInCell(0, 1) == null && board.figureInCell(0, 3) == null && !this.isCastling &&
								board.figureInCell(0, 0) != null && board.figureInCell(0, 0).getType() == FigureType.ROOK &&
								!board.figureInCell(0, 0).getIsMoved() && board.isCellNotBroken(0, 0, 3)) {
							return true;
						}
					}
					case 7 -> {
						if (xPos == 6 && board.figureInCell(yPos, 7) != null && !board.figureInCell(yPos, 7).getIsMoved() &&
								board.figureInCell(7, 5) == null && board.figureInCell(7, 6) == null &&
								!isCastling && board.figureInCell(7, 7) != null && board.figureInCell(7, 7).getType() == FigureType.ROOK &&
								!board.figureInCell(7, 7).getIsMoved() && board.isCellNotBroken(1, 7, 5)) {
							return true;
						} else if (xPos == 2 && board.figureInCell(7, 1) == null && board.figureInCell(7, 3) == null && !isCastling &&
								board.figureInCell(7, 0) != null && board.figureInCell(7, 0).getType() == FigureType.ROOK &&
								!board.figureInCell(7, 0).getIsMoved() && board.isCellNotBroken(1, 7, 3)) {
							return true;
						}
					}
				}
			}
			if (yMove <= 1 && xMove <= 1) return yMove != 0 || xMove != 0;
			else return false;
		} else return false;
	}

	public boolean isKingInCheck(LinkedList<Figure> enemyFigures) {
		for (Figure enemyFigure : enemyFigures) if (enemyFigure.canMoveTo(this.getY(), this.getX())) return true;
		return false;
	}
}