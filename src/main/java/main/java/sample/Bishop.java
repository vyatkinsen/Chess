package main.java.sample;

public class Bishop extends Figure {
	public Bishop(Board board, int color, int y, int x){
		super(board, color, y, x);
		type = FigureType.BISHOP;
	}

	@Override
	public String toString(){
		return "Bishop";
	}

	@Override
	public boolean canMoveTo(int yPos, int xPos) {
		if(canMoveFigures(yPos,xPos)) {
			return diagonal(yPos, xPos);
		} else return false;
	}
}