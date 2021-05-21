package main.java.sample;
import javafx.scene.image.ImageView;

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
	public FigureType type;

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
		board.check = false;
		int offset;
		if (color == 0) offset = -1;
		else offset = 1;

		if (Math.abs(yPos - y) == 2 && board.isCellBrokenByPawn(yPos + offset, xPos, -offset)) { //Сохранение ячейки битого поля для взятия на проходе
			board.setyPawnBrokenCell(yPos + offset);
			board.setxPawnBrokenCell(xPos);
		}
		int col = -1;
		int col2 = -1;
		int prevY = y;
		int prevX = x;
		Figure tempFig = this;
		Figure secTempFig = board.figureInCell(yPos, xPos);

		if (board.figureInCell(y, x) != null) col = color;

		if (board.figureInCell(yPos, xPos) != null) col2 = board.figureInCell(yPos, xPos).getFigureColor();

		if (board.figureInCell(y, x) == this) board.removeFromBoard(this);

		this.y = yPos;
		this.x = xPos;

		if (board.figureInCell(yPos, xPos) != null) board.figureInCell(yPos, xPos).removeFigure();

		board.setFigureOnBoard(this, y, x);

		if (type == FigureType.PAWN && board.figureInCell(yPos + offset, xPos) != null && board.figureInCell(yPos + offset, xPos).getFigureColor() != col && xPos == board.getxPawnBrokenCell() && yPos == board.getyPawnBrokenCell()) {
			if (board.figureInCell(yPos + offset, xPos).getType() == FigureType.PAWN) {
				board.figureInCell(yPos + offset, xPos).removeFigure();
			}
		}

		if (type == FigureType.PAWN && (yPos == 0 && color == 1 || yPos == 7 && color == 0)){ //pawnToQueenCheck
			if (color == 1) board.addNewFigure(0, xPos, FigureType.QUEEN, 1);
			else board.addNewFigure(7, xPos, FigureType.QUEEN, 0);
		}

		if (col == 0 && board.isKingInCheck(0) || col == 1 && board.isKingInCheck(1)) {
			board.figureInCell(yPos, xPos).removeFigure();
			board.addNewFigure(prevY, prevX, tempFig.getType(), col);
			if (secTempFig != null) board.addNewFigure(yPos, xPos, secTempFig.getType(), col2);
			board.check = true;
		}
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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
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
}