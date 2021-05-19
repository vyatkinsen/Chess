package sample;

public class Board {
	private final Figure[][] board;
	
	public Board() {
		board = new Figure[8][8];
	}

	public boolean insideBoard(int yPos, int xPos) {
		return yPos < 8 && xPos < 8 && yPos >= 0 && xPos >= 0;
	}

	public Figure figureInCell(int yPos, int xPos){
		if (insideBoard(yPos, xPos)) {
			return board[yPos][xPos];
		} else return null;
	}

	public void removeFromBoard(Figure removePiece){ board[removePiece.getY()][removePiece.getX()] = null; }

	public void setFigureOnBoard(Figure chessPiece, int yPos, int xPos){ if (insideBoard(yPos, xPos)) board[yPos][xPos] = chessPiece; }
}
