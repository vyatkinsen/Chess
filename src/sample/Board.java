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

	public void removeFromBoard(Figure removePiece) {
		board[removePiece.getY()][removePiece.getX()] = null;
	}

	public void setFigureOnBoard(Figure chessPiece, int yPos, int xPos) {
		if (insideBoard(yPos, xPos)) board[yPos][xPos] = chessPiece;
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
