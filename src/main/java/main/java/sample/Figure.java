package main.java.sample;

import static java.lang.Math.abs;

enum FigureType{
	ROOK, KNIGHT, BISHOP, QUEEN, KING, PAWN
}

public abstract class Figure {
	private int y;
	private int x;
	private final int color;
	protected Board board;
	protected boolean isMoved;
	protected FigureType type;
	protected boolean isCastling;

	public Figure(Board board, int color, int y, int x) {
		this.board = board;
		this.color = color;
		this.y = y;
		this.x = x;
		this.isMoved = false;
		this.board.setFigureOnBoard(this, y, x);
	}

	public boolean getIsMoved(){
		return isMoved;
	}

	public void setIsMoved(boolean isMoved){
		this.isMoved = isMoved;
	}

	public boolean canMoveTo(int y, int x) {
		return canMoveFigures(y, x);
	}

	protected boolean canMoveFigures(int yPos, int xPos) {
		if (board.insideBoard(yPos, xPos)) {
			Figure location = board.figureInCell(yPos, xPos);
			return location == null || location.getFigureColor() != this.color;
		} else return false;
	}

	public void movingFigure(int yPos, int xPos) {
		board.setIsCheck(false);
		int offset;
		if (color == 0) offset = -1;
		else offset = 1;
		Figure tempFig = this;

		if (Math.abs(yPos - y) == 2 && board.isCellBrokenByPawn(yPos + offset, xPos, color)) {
			board.setyPawnBrokenCell(yPos + offset);
			board.setxPawnBrokenCell(xPos);
			board.setColorOfPawnBrokenCell(color);
		}

		if (type == FigureType.PAWN && ((yPos == 0 && color == 1) || (yPos == 7 && color == 0))){
			if (color == 1) {
				if (board.figureInCell(0, xPos) != null) board.figureInCell(0, xPos).removeFigureWithCheck();
				board.addNewFigure(0, xPos, FigureType.QUEEN, 1);
				tempFig = board.figureInCell(0, xPos);
			}
			else {
				if (board.figureInCell(7, xPos) != null) board.figureInCell(7, xPos).removeFigureWithCheck();
				board.addNewFigure(7, xPos, FigureType.QUEEN, 0);
				tempFig = board.figureInCell(7, xPos);
			}
		}

		int col = -1;
		int col2 = -1;
		int prevY = y;
		int prevX = x;
		if (board.figureInCell(y, x) != null) col = color;

		Figure secTempFig = board.figureInCell(yPos, xPos);

		if (type == FigureType.PAWN && board.figureInCell(yPos + offset, xPos) != null &&
				board.figureInCell(yPos + offset, xPos).getFigureColor() != color &&
				xPos == board.getxPawnBrokenCell() && yPos == board.getyPawnBrokenCell() && color != board.getColorOfPawnBrokenCell()) {
			if (board.figureInCell(yPos + offset, xPos).getType() == FigureType.PAWN) {
				board.figureInCell(yPos + offset, xPos).removeFigureWithCheck();
				board.setxPawnBrokenCell(-1);
				board.setyPawnBrokenCell(-1);
				board.setColorOfPawnBrokenCell(-1);
			}
		}

		if (type == FigureType.PAWN && (yPos == board.getyPawnBrokenCell() && xPos == board.getxPawnBrokenCell() && this.getFigureColor() != board.getColorOfPawnBrokenCell())){
			board.setxPawnBrokenCell(-1);
			board.setyPawnBrokenCell(-1);
			board.setColorOfPawnBrokenCell(-1);
		}

		if (board.figureInCell(yPos, xPos) != null) col2 = board.figureInCell(yPos, xPos).getFigureColor();

		if (type == FigureType.KING) {
			switch (prevY) {
				case 0 -> {
					if (xPos == 6 && board.figureInCell(yPos, 7) != null && !board.figureInCell(yPos, 7).getIsMoved() &&
							board.figureInCell(0, 5) == null &&	board.figureInCell(0, 6) == null &&
							!isCastling && board.figureInCell(0, 7) != null && board.figureInCell(0, 7).getType() == FigureType.ROOK &&
							!board.figureInCell(0, 7).getIsMoved() && board.isCellNotBroken(0, 0, 5)) {
						board.figureInCell(0, 7).removeFigureWithCheck();
						board.addNewFigure(0, 5, FigureType.ROOK, 0);
					} else if (xPos == 2 && board.figureInCell(0, 1) == null &&	board.figureInCell(0, 3) == null && !isCastling &&
							board.figureInCell(0, 0) != null && board.figureInCell(0, 0).getType() == FigureType.ROOK &&
							!board.figureInCell(0, 0).getIsMoved() && board.isCellNotBroken(0, 0, 3)) {
						board.figureInCell(0, 7).removeFigureWithCheck();
						board.addNewFigure(0, 3, FigureType.ROOK, 0);
					}
				}
				case 7 -> {
					if (xPos == 6 && board.figureInCell(yPos, 7) != null && !board.figureInCell(yPos, 7).getIsMoved() &&
							board.figureInCell(7, 5) == null &&	board.figureInCell(7, 6) == null &&
							!isCastling && board.figureInCell(7, 7) != null && board.figureInCell(7, 7).getType() == FigureType.ROOK &&
							!board.figureInCell(7, 7).getIsMoved() && board.isCellNotBroken(1, 7, 5)) {
						board.figureInCell(7, 7).removeFigureWithCheck();
						board.addNewFigure(7, 5, FigureType.ROOK, 1);
					} else if (xPos == 2 && board.figureInCell(7, 1) == null &&	board.figureInCell(7, 3) == null && !isCastling &&
							board.figureInCell(7, 0) != null &&	board.figureInCell(7, 0).getType() == FigureType.ROOK &&
							!board.figureInCell(7, 0).getIsMoved() && board.isCellNotBroken(1, 7, 3)) {
						board.figureInCell(7, 0).removeFigureWithCheck();
						board.addNewFigure(7, 3, FigureType.ROOK, 1);
					}
				}
			}
			isCastling = true;
		}

		if (board.figureInCell(yPos, xPos) != null) board.figureInCell(yPos, xPos).removeFigureWithCheck();

		if (board.figureInCell(y, x) == this) board.removeFromBoard(this);

		this.y = yPos;
		this.x = xPos;

		board.setFigureOnBoard(tempFig, y, x);

		if (color == 0 && board.isKingInCheck(0) || color == 1 && board.isKingInCheck(1)) {
			board.figureInCell(yPos, xPos).removeFigureWithCheck();
			board.addNewFigure(prevY, prevX, tempFig.getType(), col);
			if (secTempFig != null) board.addNewFigure(yPos, xPos, secTempFig.getType(), col2);
			board.setIsCheck(true);
		}
		isMoved = true;
	}

	public void moveWithoutCheck(int yPos, int xPos) {
		Figure tempFig = this;
		if (board.figureInCell(y, x) == this) board.removeFromBoard(this);
		this.y = yPos;
		this.x = xPos;
		if (board.figureInCell(yPos, xPos) != null) board.figureInCell(yPos, xPos).removeFigure();
		board.setFigureOnBoard(tempFig, y, x);
	}

	public void removeFigure() {
		board.removeFromBoard(this);
		y = x = -1;
	}

	public void removeFigureWithCheck() {
		board.removeFromBoardWithCheck(this);
		y = x = -1;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean getIsCastling() {
		return isCastling;
	}

	public void setIsCastling(boolean isCastling) {
		this.isCastling = isCastling;
	}

	public FigureType getType() {
		return type;
	}

	public int getFigureColor() {
		return color;
	}

	public String getStringPlayerColor() {
		if (color == 0) return "Black";
		else return "White";
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

	public boolean equal(Figure fig) {
		return fig.getX() == x && fig.getY() == y && fig.getFigureColor() == color && fig.getType() == type && fig.board.equals(board);
	}
}