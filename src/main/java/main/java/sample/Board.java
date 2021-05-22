package main.java.sample;

import java.util.LinkedList;
import java.util.List;

public class Board {
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	private int yPawnBrokenCell = -1;
	private int xPawnBrokenCell = -1;
	private int colorOfPawnBrokenCell = -1;
	private final Figure[][] board;
	private LinkedList<Figure> blackFiguresList;
	private LinkedList<Figure> whiteFiguresList;
	private King blackKing;
	private King whiteKing;
	private boolean check;


	public Board() {
		board = new Figure[8][8];
	}

	public boolean insideBoard(int yPos, int xPos) {
		return yPos < 8 && xPos < 8 && yPos >= 0 && xPos >= 0;
	}

	public Figure figureInCell(int yPos, int xPos){
		if (insideBoard(yPos, xPos)) return board[yPos][xPos];
		else return null;
	}

	public void removeFromBoard(Figure removePiece) {
		if (insideBoard(removePiece.getY(), removePiece.getX())) {
			board[removePiece.getY()][removePiece.getX()] = null;
		}
	}

	public void setFigureOnBoard(Figure chessPiece, int yPos, int xPos) {
		if (insideBoard(yPos, xPos)) {
			board[yPos][xPos] = chessPiece;
			chessPiece.setY(yPos);
			chessPiece.setX(xPos);
		}
	}

	public boolean isCellBrokenByPawn(int yPos, int xPos, int currentPlayer) {
		LinkedList<Figure> enemyFigures;
		int offset;
		if (currentPlayer == BLACK) {
			enemyFigures = this.getWhiteFiguresList();
			offset = 1;
		} else {
			offset = -1;
			enemyFigures = this.getBlackFiguresList();
		}
		for (Figure enemyFigure : enemyFigures) {
			if (enemyFigure instanceof Pawn && enemyFigure.getY() == yPos + offset && (enemyFigure.getX() == xPos + 1 || enemyFigure.getX() == xPos - 1)){
				return true;
			}
		}
		return false;
	}

	public void addNewFigure(int yPos, int xPos, FigureType type, int color) {
		switch (type) {
			case PAWN -> addPawn(color, yPos, xPos);
			case KNIGHT -> addKnight(color, yPos, xPos);
			case BISHOP -> addBishop(color, yPos, xPos);
			case QUEEN -> addQueen(color, yPos, xPos);
			case ROOK -> {
				addRook(color, yPos, xPos);
				this.figureInCell(yPos, xPos).setIsMoved(true);
			}
			case KING -> {
				if (color == 0) {
					blackKing = new King(this, color, yPos, xPos);
					figuresListAdd(blackKing, color);
				} else {
					whiteKing = new King(this, color, yPos, xPos);
					figuresListAdd(whiteKing, color);
				}
			}
		}
	}

	public boolean noMovesLeft(int color) {
		int prevY, prevX;
		Figure tempFigure;
		List<Figure> correctFiguresList;
		if (color == BLACK) correctFiguresList = new LinkedList<>(blackFiguresList);
		else correctFiguresList = new LinkedList<>(whiteFiguresList);

		for (Figure currFigure: correctFiguresList) {
			boolean move = currFigure.getIsMoved();
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					if (currFigure.canMoveTo(j, i)) {
						tempFigure = this.figureInCell(j, i);
						prevY = currFigure.getY();
						prevX = currFigure.getX();

						currFigure.moveWithoutCheck(j, i);

						if (!isKingInCheck(color)) {
							currFigure.moveWithoutCheck(prevY, prevX);
							if (tempFigure != null) {
								this.addNewFigure(j, i, tempFigure.getType(), tempFigure.getFigureColor());
							}
							currFigure.setIsMoved(move);
							if (currFigure.getType() == FigureType.KING){
								if (Math.abs(prevX - i) == 2 && Math.abs(prevY - j) == 0 && !move) {
									switch (prevY) {
										case 0 -> {
											if (i == 6) this.addNewFigure(0, 7, FigureType.ROOK, 0);
											else if (i == 2) this.addNewFigure(0, 0, FigureType.ROOK, 0);
										}
										case 7 -> {
											if (i == 6) this.addNewFigure(7, 7, FigureType.ROOK, 1);
											else if (i == 2) this.addNewFigure(7, 0, FigureType.ROOK, 1);
										}
									}
								}
							}

							return false;
						} else {
							System.out.println("NO MOVE");
							currFigure.moveWithoutCheck(prevY, prevX);
							if (tempFigure != null) this.addNewFigure(j, i, tempFigure.getType(), tempFigure.getFigureColor());
						}
					}
				}
			}
		}
		return true;
	}

	public void addQueen(int color, int y, int x){
		Queen queen = new Queen(this, color, y, x);
		figuresListAdd(queen, color);
	}

	private void addKnight(int color, int y, int x){
		Knight knight = new Knight(this, color, y, x);
		figuresListAdd(knight, color);
	}

	private void addRook(int color, int y, int x){
		Rook rook = new Rook(this, color, y, x);
		figuresListAdd(rook, color);
	}

	private void addBishop(int color, int y, int x){
		Bishop bishop = new Bishop(this, color, y, x);
		figuresListAdd(bishop, color);
	}

	private void addPawn(int color, int y, int x){
		Pawn pawn = new Pawn(this, color, y, x);
		figuresListAdd(pawn, color);
	}

	private void figuresListAdd(Figure piece, int color){
		System.out.println(piece);
		if (color == BLACK) blackFiguresList.add(piece);
		else whiteFiguresList.add(piece);
	}

	public boolean isKingInCheck(int color) {
		if (blackKing != null && whiteKing != null) {
			if (color == BLACK) {
				return blackKing.isKingInCheck(this.getWhiteFiguresList());
			} else {
				return whiteKing.isKingInCheck(this.getBlackFiguresList());
			}
		} return false;
	}

	public boolean isCellBroken(int color, int yPos, int xPos) {
		LinkedList<Figure> enemyFigures;
		if (color == BLACK) {
			enemyFigures = this.getWhiteFiguresList();
		} else {
			enemyFigures = this.getBlackFiguresList();
		}
		for (Figure enemyFigure : enemyFigures) {
			if (enemyFigure.canMoveTo(yPos, xPos)) {
				return false;
			}
		}
		return true;
	}

	public void initBoard(){
		blackKing = new King(this, BLACK, 0, 4);
		whiteKing = new King(this, WHITE, 7, 4);
		blackFiguresList = new LinkedList<>();
		whiteFiguresList = new LinkedList<>();
		blackFiguresList.add(blackKing);
		whiteFiguresList.add(whiteKing);
		addQueen(BLACK, 0, 3);
		addQueen(WHITE, 7, 3);
		addBishop(BLACK, 0, 2);
		addBishop(WHITE, 7, 2);
		addBishop(BLACK, 0, 5);
		addBishop(WHITE, 7, 5);
		addKnight(BLACK, 0, 1);
		addKnight(WHITE, 7, 1);
		addKnight(BLACK, 0, 6);
		addKnight(WHITE, 7, 6);
		addRook(BLACK, 0, 0);
		addRook(BLACK, 0, 7);
		addRook(WHITE, 7, 0);
		addRook(WHITE, 7, 7);
		for (int x = 0; x < 8; x++){
			addPawn(BLACK, 1, x);
			addPawn(WHITE, 6, x);
		}
	}

	public LinkedList<Figure> getBlackFiguresList(){
		return blackFiguresList;
	}

	public LinkedList<Figure> getWhiteFiguresList(){
		return whiteFiguresList;
	}

	public King getBlackKing(){
		return blackKing;
	}

	public King getWhiteKing(){
		return whiteKing;
	}

	public boolean getCheck(){
		return check;
	}

	public int getxPawnBrokenCell() {
		return xPawnBrokenCell;
	}

	public int getyPawnBrokenCell() {
		return yPawnBrokenCell;
	}

	public void setIsCheck(boolean check) {
		this.check = check;
	}

	public void setWhiteFiguresList(LinkedList<Figure> list){
		whiteFiguresList = list;
	}

	public void setBlackFiguresList(LinkedList<Figure> list){
		blackFiguresList = list;
	}

	public void setxPawnBrokenCell(int xPawnBrokenCell) {
		this.xPawnBrokenCell = xPawnBrokenCell;
	}

	public void setyPawnBrokenCell(int yPawnBrokenCell) {
		this.yPawnBrokenCell = yPawnBrokenCell;
	}

	public int getColorOfPawnBrokenCell() {
		return colorOfPawnBrokenCell;
	}

	public void setColorOfPawnBrokenCell(int colorOfPawnBrokenCell) {
		this.colorOfPawnBrokenCell = colorOfPawnBrokenCell;
	}

	public void printBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (board[y][x] == null) {
					System.out.print(".  ");
				} else {
					if (board[y][x] instanceof Pawn)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "p ");
					else if (board[y][x] instanceof Knight)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "k ");
					else if (board[y][x] instanceof Queen)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "q ");
					else if (board[y][x] instanceof King)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "K ");
					else if (board[y][x] instanceof Rook)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "r ");
					else if (board[y][x] instanceof Bishop)
						System.out.print(board[y][x].getStringPlayerColor().charAt(0) + "b ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}