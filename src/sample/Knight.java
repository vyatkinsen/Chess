package sample;

public class Knight extends Figure {
	public Knight(Board board, int color, int y, int x){ super(board, color, y, x);	}

	@Override
	public String toString(){ return "Knight";}

	public boolean canMoveTo(int yPos, int xPos){
		if(canMoveGenerics(yPos,xPos)) return knightMovement(yPos, xPos);
		return false;
	}

	private boolean knightMovement(int yPos, int xPos){
		if (Math.abs(this.getY() - yPos) == 2 && Math.abs(this.getX() - xPos) == 1) return true;
		if (Math.abs(this.getY() - yPos) == 1 && Math.abs(this.getX() - xPos) == 2) return true;
		return false;
	}
}
