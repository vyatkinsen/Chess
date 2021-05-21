package main.java.sample;

import javafx.scene.image.ImageView;

import java.util.LinkedList;

public class King extends Figure {
	public King(Board board, int color, int y, int x) {
		super(board, color, y, x);
	}

	@Override
	public String toString() {
		return "King";
	}

	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveFigures(yPos, xPos)) {
			int yMove = Math.abs(yPos - this.getY());
			int xMove = Math.abs(xPos - this.getX());

			if (xMove == 2 && yMove ==0 && !isMoved) {
				switch (yPos) {
					case 0 -> {
						if (xPos == 6) {
							board.setFigureOnBoard(new Rook(board, 0, 0, 5), 0, 5);
							board.figureInCell(0, 7).removeFigure();
						}
						else {
							board.setFigureOnBoard(new Rook(board, 0, 0, 3), 0, 3);
							board.figureInCell(0, 0).removeFigure();
						}
					}
					case 7 -> {
						if (xPos == 6) {
							board.setFigureOnBoard(new Rook(board, 1, 7, 5), 7, 5);
							board.figureInCell(7, 7).removeFigure();
						}
						else {
							board.setFigureOnBoard(new Rook(board, 1, 7, 3), 7, 3);
							board.figureInCell(7, 0).removeFigure();
						}
					}
				}
				return true;
			}
			if (yMove <= 1 && xMove <= 1){
				return yMove != 0 || xMove != 0;
			} else return false;
		} else return false;
	}

	public boolean isKingInCheck(LinkedList<Figure> enemyFigures) {
		for (Figure enemyFigure : enemyFigures) {
			if (enemyFigure.canMoveTo(this.getY(), this.getX())) {
				return true;
			}
		}
		return false;
	}
}