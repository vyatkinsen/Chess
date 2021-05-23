package main.java.sample;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class ChessLogicTest {
	Board chessBoard = new Board();
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	LinkedList<Figure> whiteFiguresList;
	LinkedList<Figure> blackFiguresList;

	Pawn whitePawn0;
	Pawn whitePawn1;
	Pawn whitePawn2;
	Pawn whitePawn3;
	Pawn whitePawn4;
	Pawn whitePawn5;
	Pawn whitePawn6;
	Pawn whitePawn7;
	Rook whiteLeftRook;
	Rook whiteRightRook;
	Knight whiteLeftKnight;
	Knight whiteRightKnight;
	Bishop whiteLeftBishop;
	Bishop whiteRightBishop;
	Queen whiteQueen;
	King whiteKing;

	Pawn blackPawn0;
	Pawn blackPawn1;
	Pawn blackPawn2;
	Pawn blackPawn3;
	Pawn blackPawn4;
	Pawn blackPawn5;
	Pawn blackPawn6;
	Pawn blackPawn7;
	Rook blackLeftRook;
	Rook blackRightRook;
	Knight blackLeftKnight;
	Knight blackRightKnight;
	Bishop blackLeftBishop;
	Bishop blackRightBishop;
	Queen blackQueen;
	King blackKing;


	@Test
	void insideBoardTest(){
		chessBoard.initBoard();
		assertFalse(chessBoard.insideBoard(9, 0));
		assertFalse(chessBoard.insideBoard(-2, 8));
		assertFalse(chessBoard.insideBoard(2, 34));
		assertFalse(chessBoard.insideBoard(100, 110));
		assertTrue(chessBoard.insideBoard(3, 3));
		assertTrue(chessBoard.insideBoard(0, 2));
		assertTrue(chessBoard.insideBoard(2, 7));
		assertTrue(chessBoard.insideBoard(6, 4));
	}

	@Test
	void figureInCellTest(){
		chessBoard.initBoard();
		assertFalse(chessBoard.figureInCell(1, 1) instanceof Queen);
		assertFalse(chessBoard.figureInCell(7, 1) instanceof King);
		assertFalse(chessBoard.figureInCell(7, 3) instanceof Bishop);
		assertFalse(chessBoard.figureInCell(3, 3) instanceof Rook);
		assertTrue(chessBoard.figureInCell(1, 1) instanceof Pawn);
		assertTrue(chessBoard.figureInCell(0, 2) instanceof Bishop);
		assertTrue(chessBoard.figureInCell(7, 1) instanceof Knight);
		assertTrue(chessBoard.figureInCell(7, 4) instanceof King);
	}

	@Test
	void removeFromBoardTest(){
		chessBoard.initBoard();

		assertNotNull(chessBoard.figureInCell(0, 6));
		chessBoard.removeFromBoard(chessBoard.figureInCell(0, 6));
		assertNull(chessBoard.figureInCell(0, 6));

		assertNotNull(chessBoard.figureInCell(1, 2));
		chessBoard.removeFromBoard(chessBoard.figureInCell(1, 2));
		assertNull(chessBoard.figureInCell(1, 2));

		assertNotNull(chessBoard.figureInCell(7, 6));
		chessBoard.removeFromBoard(chessBoard.figureInCell(7, 6));
		assertNull(chessBoard.figureInCell(7, 6));

		assertNotNull(chessBoard.figureInCell(7, 0));
		chessBoard.removeFromBoard(chessBoard.figureInCell(7, 0));
		assertNull(chessBoard.figureInCell(7, 0));

		assertNotNull(chessBoard.figureInCell(6, 1));
		chessBoard.removeFromBoard(chessBoard.figureInCell(6, 1));
		assertNull(chessBoard.figureInCell(6, 1));

		assertNotNull(chessBoard.figureInCell(0, 5));
		chessBoard.removeFromBoard(chessBoard.figureInCell(0, 5));
		assertNull(chessBoard.figureInCell(0, 5));
	}

	@Test
	void setFigureOnBoardTest(){
		chessBoard.initBoard();

		assertNull(chessBoard.figureInCell(5, 5));
		chessBoard.setFigureOnBoard(new Bishop(chessBoard, WHITE, 5, 5), 5 , 5);
		assertTrue(chessBoard.figureInCell(5, 5) instanceof Bishop);

		assertNull(chessBoard.figureInCell(4, 6));
		chessBoard.setFigureOnBoard(new Queen(chessBoard, WHITE, 4, 6), 4 , 6);
		assertTrue(chessBoard.figureInCell(5, 5) instanceof Bishop);

		assertNull(chessBoard.figureInCell(3, 6));
		chessBoard.setFigureOnBoard(new Rook(chessBoard, BLACK, 3, 6), 3, 6);
		assertTrue(chessBoard.figureInCell(3, 6) instanceof Rook);
	}

	@Test
	void bishopCanMoveToTest(){
		Board bishopBoard = new Board();
		whiteLeftBishop = new Bishop(bishopBoard, WHITE, 4, 4);
		assertTrue(whiteLeftBishop.canMoveTo(0, 0));
		assertTrue(whiteLeftBishop.canMoveTo(1, 7));
		assertTrue(whiteLeftBishop.canMoveTo(7, 1));
		assertTrue(whiteLeftBishop.canMoveTo(7, 7));

		whitePawn0 = new Pawn(bishopBoard, WHITE, 2, 2);
		assertFalse(whiteLeftBishop.canMoveTo(0, 0));

		blackPawn0 = new Pawn(bishopBoard, BLACK, 6, 6);
		assertTrue(whiteLeftBishop.canMoveTo(6, 6));
	}

	@Test
	void kingCanMoveTest(){
		Board kingBoard = new Board();
		whiteKing = new King(kingBoard, WHITE, 4, 4);

		assertTrue(whiteKing.canMoveTo(4, 3));
		assertTrue(whiteKing.canMoveTo(4, 5));
		assertTrue(whiteKing.canMoveTo(3, 3));
		assertTrue(whiteKing.canMoveTo(3, 4));
		assertTrue(whiteKing.canMoveTo(3, 5));
		assertTrue(whiteKing.canMoveTo(5, 4));
		assertTrue(whiteKing.canMoveTo(5, 3));
		assertTrue(whiteKing.canMoveTo(5, 5));

		whitePawn0 = new Pawn(kingBoard, WHITE, 5, 5);
		assertFalse(whiteKing.canMoveTo(5, 5));

	}

	@Test
	void isKingInCheckTest(){
		Board kingBoard = new Board();
		LinkedList<Figure> enemyList = new LinkedList<>();
		blackKing = new King(kingBoard, BLACK, 0, 4);
		whiteQueen = new Queen(kingBoard, WHITE, 7, 3);
		enemyList.add(whiteQueen);
		assertFalse(blackKing.isKingInCheck(enemyList));

		whiteQueen.movingFigure(6, 4);
		assertTrue(blackKing.isKingInCheck(enemyList));
	}

	@Test
	void knightCanMoveTest(){
		Board knightBoard = new Board();
		whiteLeftKnight = new Knight(knightBoard, WHITE, 4, 4);

		assertTrue(whiteLeftKnight.canMoveTo(3, 2));
		assertTrue(whiteLeftKnight.canMoveTo(5, 2));
		assertTrue(whiteLeftKnight.canMoveTo(3, 6));
		assertTrue(whiteLeftKnight.canMoveTo(5, 6));

		whitePawn0 = new Pawn(knightBoard, WHITE, 3, 2);
		assertFalse(whiteLeftKnight.canMoveTo(3, 2));

		blackPawn0 = new Pawn(knightBoard, BLACK, 3, 2);
		assertTrue(whiteLeftKnight.canMoveTo(3, 2));
	}

	@Test
	void pawnCanMoveTest(){
		Board pawnBoard = new Board();
		blackPawn0 = new Pawn(pawnBoard, BLACK, 1, 0);

		assertTrue(blackPawn0.canMoveTo(3, 0));
		assertTrue(blackPawn0.canMoveTo(2, 0));
		assertFalse(blackPawn0.canMoveTo(2, 1));
		assertFalse(blackPawn0.canMoveTo(3, 1));

		blackPawn1 = new Pawn(pawnBoard, BLACK, 2, 0);
		assertFalse(blackPawn0.canMoveTo(2, 0));
		assertFalse(blackPawn0.canMoveTo(3, 0));
		blackPawn1.removeFigure();

		whitePawn0 = new Pawn(pawnBoard, WHITE, 2, 1);
		assertTrue(blackPawn0.canMoveTo(2, 1));
		whitePawn0.removeFigure();

		whitePawn1 = new Pawn(pawnBoard, WHITE, 3, 1);
		assertFalse(blackPawn0.canMoveTo(3, 1));
	}

	@Test
	void queenCanMoveToTest(){
		Board queenBoard = new Board();
		whiteQueen = new Queen(queenBoard, WHITE, 4, 4);
		assertTrue(whiteQueen.canMoveTo(0, 0));
		assertTrue(whiteQueen.canMoveTo(1, 7));
		assertTrue(whiteQueen.canMoveTo(7, 1));
		assertTrue(whiteQueen.canMoveTo(7, 7));

		assertTrue(whiteQueen.canMoveTo(0, 4));
		assertTrue(whiteQueen.canMoveTo(7, 4));
		assertTrue(whiteQueen.canMoveTo(4, 0));
		assertTrue(whiteQueen.canMoveTo(4, 7));

		whitePawn0 = new Pawn(queenBoard, WHITE, 2, 2);
		assertFalse(whiteQueen.canMoveTo(0, 0));

		whitePawn1 = new Pawn(queenBoard, WHITE, 2, 4);
		assertFalse(whiteQueen.canMoveTo(0, 4));

		whitePawn2 = new Pawn(queenBoard, WHITE, 4, 6);
		assertFalse(whiteQueen.canMoveTo(4, 7));

		blackPawn0 = new Pawn(queenBoard, BLACK, 6, 6);
		assertTrue(whiteQueen.canMoveTo(6, 6));
	}

	@Test
	void rookCanMoveToTest(){
		Board rookBoard = new Board();
		whiteLeftRook = new Rook(rookBoard, WHITE, 4, 4);
		assertTrue(whiteLeftRook.canMoveTo(0, 4));
		assertTrue(whiteLeftRook.canMoveTo(7, 4));
		assertTrue(whiteLeftRook.canMoveTo(4, 0));
		assertTrue(whiteLeftRook.canMoveTo(4, 7));

		whitePawn0 = new Pawn(rookBoard, WHITE, 4, 1);
		assertFalse(whiteLeftRook.canMoveTo(4, 0));

		whitePawn1 = new Pawn(rookBoard, WHITE, 2, 4);
		assertFalse(whiteLeftRook.canMoveTo(0, 4));

		whitePawn2 = new Pawn(rookBoard, WHITE, 4, 6);
		assertFalse(whiteLeftRook.canMoveTo(4, 7));

		blackPawn0 = new Pawn(rookBoard, BLACK, 7, 4);
		assertTrue(whiteLeftRook.canMoveTo(7, 4));
	}
}