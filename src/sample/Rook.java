package sample;

public class Rook extends Figure {
	public Rook(Board board, int color, int y, int x){
		super(board, color, y, x);
	}

	@Override
	public String toString(){ return "Rook"; }
	
	public boolean canMoveTo(int yPos, int xPos){
		if(canMoveGenerics(yPos,xPos)) return moves(yPos, xPos);
		return false;
	}

	private boolean moves(int yPos, int xPos){ return verticalAndHorizontal(yPos, xPos); }
}