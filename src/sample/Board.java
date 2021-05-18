package sample;

public class Board {
	private final Figure[][] board;
	
	public Board(){ board = new Figure[8][8]; }

	public boolean insideBoard(int yPos, int xPos){ return yPos < 8 && xPos < 8 && yPos >= 0 && xPos >= 0; }

	public Figure figureInCell(int yPos, int xPos){
		if (insideBoard(yPos, xPos)) return board[yPos][xPos];
		else return null;
	}

	public void removeFromBoard(Figure removePiece){ board[removePiece.getY()][removePiece.getX()] = null; }

	public void placePiece(Figure chessPiece, int yPos, int xPos){ if (insideBoard(yPos, xPos)) board[yPos][xPos] = chessPiece; }

	public void displayBoard(){
		for (int xBoard = 0; xBoard < 8; xBoard++){
			for (int yBoard = 0; yBoard < 8; yBoard++){
				if (board[xBoard][yBoard] == null)
					System.out.print(".");
				else{
					if (board[xBoard][yBoard] instanceof Pawn)
						System.out.print("p");
					else if (board[xBoard][yBoard] instanceof Knight)
						System.out.print("k");
					else if (board[xBoard][yBoard] instanceof Queen)
						System.out.print("q");
					else if (board[xBoard][yBoard] instanceof King)
						System.out.print("K");
					else if (board[xBoard][yBoard] instanceof Rook)
						System.out.print("r");
					else if (board[xBoard][yBoard] instanceof Bishop)
						System.out.print("b");
					else
						System.out.print("x");
				}
			}
			System.out.println();
		}
	}
}
