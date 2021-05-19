package sample;

import static java.lang.Math.abs;

public abstract class Figure {
	private int y;
	private int x;
	private final int color;
	protected Board board;
	protected boolean isMoved;

	public Figure(Board board, int color, int y, int x) {
		this.board = board;
		this.color = color;
		this.y = y;
		this.x = x;
		this.isMoved = false;
		this.board.setFigureOnBoard(this, y, x);
	}

	public boolean isPieceWasMoved(){
		return isMoved;
	}

	public boolean canMoveTo(int y, int x) {
		return canMoveFigures(y, x);
	}

	protected boolean canMoveFigures(int yPos, int xPos) {
		if (board.insideBoard(yPos, xPos)) {
			Figure location = board.figureInCell(yPos, xPos);
			return location == null || location.getPlayerColor() != this.color;
		} else return false;
	}

	public void movingFigure(int yPos, int xPos) {
		if (board.figureInCell(y, x) == this) {
			board.removeFromBoard(this);
		}
		this.y = yPos;
		this.x = xPos;

		if (board.figureInCell(yPos, xPos) != null) {
			board.figureInCell(yPos, xPos).removeFigure();
		}

		board.setFigureOnBoard(this, y, x);
		isMoved = true;
	}

	public void removeFigure() {
		board.removeFromBoard(this);
		y = x = -1;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getPlayerColor() {
		return color;
	}

	protected boolean verticalAndHorizontal(int yPos, int xPos) {
		int yFigurePos = this.getY();
		int xFigurePos = this.getX();

		int countX, limitX, countY, limitY;

		if (yFigurePos == yPos && xFigurePos != xPos || yFigurePos != yPos && xFigurePos == xPos) {
			if (xFigurePos > xPos) {
				countX = xPos + 1;
				limitX = xFigurePos;
			} else {
				countX = xFigurePos + 1;
				limitX = xPos;
			}
			for(; countX < limitX; countX++) {
				if (board.figureInCell(yFigurePos, countX) != null) {
					return false;
				}
			}

			if (yFigurePos > yPos) {
				countY = yPos + 1;
				limitY = yFigurePos;
			} else {
				countY = yFigurePos + 1;
				limitY = yPos;
			}
			for(; countY < limitY; countY++) {
				if (board.figureInCell(countY, xFigurePos) != null) {
					return false;
				}
			}
			return true;
		} else return false;
	}

	protected boolean diagonal(int yPos, int xPos) {
		int row = this.getY();
		int col = this.getX();

		if (abs(xPos - col) == abs(yPos - row)) {
			int rowOffset, colOffset;

			if(row < yPos) {
				rowOffset = 1;
			} else rowOffset = -1;

			if(col < xPos) {
				colOffset = 1;
			} else colOffset = -1;

			int x = col + colOffset;
			for(int y = row + rowOffset; y != yPos; y += rowOffset) {
				if (board.figureInCell(y, x) != null) {
					return false;
				}
				x += colOffset;
			} return true;
		} else return false;
	}
}
