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
	void createNewBoard(){
		whiteFiguresList = new LinkedList<>();
		blackFiguresList = new LinkedList<>();
		whitePawn0 = new Pawn(chessBoard, WHITE, 6, 0);
		whitePawn1 = new Pawn(chessBoard, WHITE, 6, 1);
		whitePawn2 = new Pawn(chessBoard, WHITE, 6, 2);
		whitePawn3 = new Pawn(chessBoard, WHITE, 6, 3);
		whitePawn4 = new Pawn(chessBoard, WHITE, 6, 4);
		whitePawn5 = new Pawn(chessBoard, WHITE, 6, 5);
		whitePawn6 = new Pawn(chessBoard, WHITE, 6, 6);
		whitePawn7 = new Pawn(chessBoard, WHITE, 6, 7);
		whiteLeftRook = new Rook(chessBoard, WHITE, 7, 0);
		whiteRightRook = new Rook(chessBoard, WHITE, 7, 7);
		whiteLeftKnight = new Knight(chessBoard, WHITE, 7, 1);
		whiteRightKnight = new Knight(chessBoard, WHITE, 7, 6);
		whiteLeftBishop = new Bishop(chessBoard, WHITE, 7, 2);
		whiteRightBishop = new Bishop(chessBoard, WHITE, 7, 5);
		whiteQueen = new Queen(chessBoard, WHITE, 7, 3);
		whiteKing = new King(chessBoard, WHITE, 7, 4);

		blackPawn0 = new Pawn(chessBoard, BLACK, 1, 0);
		blackPawn1 = new Pawn(chessBoard, BLACK, 1, 1);
		blackPawn2 = new Pawn(chessBoard, BLACK, 1, 2);
		blackPawn3 = new Pawn(chessBoard, BLACK, 1, 3);
		blackPawn4 = new Pawn(chessBoard, BLACK, 1, 4);
		blackPawn5 = new Pawn(chessBoard, BLACK, 1, 5);
		blackPawn6 = new Pawn(chessBoard, BLACK, 1, 6);
		blackPawn7 = new Pawn(chessBoard, BLACK, 1, 7);
		blackLeftRook = new Rook(chessBoard, BLACK, 0, 0);
		blackRightRook = new Rook(chessBoard, BLACK, 0, 7);
		blackLeftKnight = new Knight(chessBoard, BLACK, 0, 1);
		blackRightKnight = new Knight(chessBoard, BLACK, 0, 6);
		blackLeftBishop = new Bishop(chessBoard, BLACK, 0, 2);
		blackRightBishop = new Bishop(chessBoard, BLACK, 0, 5);
		blackQueen = new Queen(chessBoard, BLACK, 0, 3);
		blackKing = new King(chessBoard, BLACK, 0, 4);

		for (int y = 6; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				whiteFiguresList.add(chessBoard.figureInCell(y, x));
			}
		}

		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 8; x++) {
				blackFiguresList.add(chessBoard.figureInCell(y, x));
			}
		}
	}

	@Test
	void insideBoardTest(){
		createNewBoard();
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
		createNewBoard();
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
		createNewBoard();

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

		assertThrows(NullPointerException.class,
				()-> chessBoard.removeFromBoard(chessBoard.figureInCell(5, 5)));
	}

	@Test
	void setFigureOnBoardTest(){
		createNewBoard();

		assertNull(chessBoard.figureInCell(5, 5));
		chessBoard.setFigureOnBoard(new Bishop(chessBoard, WHITE, 5, 5), 5 , 5);
		assertTrue(chessBoard.figureInCell(5, 5) instanceof Bishop);

		assertNull(chessBoard.figureInCell(4, 6));
		chessBoard.setFigureOnBoard(new Queen(chessBoard, WHITE, 4, 6), 4 , 6);
		assertTrue(chessBoard.figureInCell(5, 5) instanceof Bishop);

		assertNull(chessBoard.figureInCell(3, 6));
		chessBoard.setFigureOnBoard(new Rook(chessBoard, BLACK, 3, 6), 3, 6);
		assertTrue(chessBoard.figureInCell(3, 6) instanceof Rook);

		assertNull(chessBoard.figureInCell(5, 7));
		assertThrows(IllegalArgumentException.class,
				()-> chessBoard.setFigureOnBoard(new Pawn(chessBoard, BLACK, -1, -1), 5 , 7));
		assertFalse(chessBoard.figureInCell(5, 7) instanceof Pawn);
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

		blackKing = new King(kingBoard, BLACK, 0, 4);
		assertTrue(blackKing.canMoveTo(0, 6)); //Проверка на возможность рокировки
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

	@Test
	void someGameTest(){
		createNewBoard();
		assertTrue(chessBoard.figureInCell(1,1) instanceof Pawn);
		assertFalse(whiteLeftKnight.canMoveTo(5, 1));
		assertTrue(whiteLeftKnight.canMoveTo(5, 2));
		whitePawn0.movingFigure(4, 0);
		whitePawn0.movingFigure(3, 0);
		whitePawn0.movingFigure(2, 0);
		assertTrue(whitePawn0.canMoveTo(1, 1));
		assertEquals(2, whitePawn0.getY());
		assertNull(chessBoard.figureInCell(6, 0));
		assertFalse(chessBoard.insideBoard(8, 8));
		blackPawn4.movingFigure(3, 3);
		assertTrue(blackQueen.canMoveTo(3, 6));
		blackQueen.movingFigure(3, 6);
		whiteLeftKnight.movingFigure(5, 2);
		assertTrue(chessBoard.figureInCell(3,3) instanceof Pawn);
		whiteLeftKnight.movingFigure(3, 3);
		assertTrue(chessBoard.figureInCell(3,3) instanceof Knight);
		blackQueen.movingFigure(3, 3);
		assertTrue(chessBoard.figureInCell(3,3) instanceof Queen);
		assertEquals(BLACK, chessBoard.figureInCell(3,3).getFigureColor());
		blackQueen.movingFigure(4, 1);
		whitePawn3.movingFigure(4, 3);
		assertTrue(whiteKing.isKingInCheck(blackFiguresList));
		whiteQueen.movingFigure(6, 3);
		assertEquals("Queen", chessBoard.figureInCell(6, 3).toString());
		assertFalse(whiteKing.isKingInCheck(blackFiguresList));
		blackQueen.movingFigure(6, 3);
		assertTrue(whiteLeftBishop.canMoveTo(6, 3));
		whiteLeftBishop.movingFigure(6,  3);
		assertFalse(whiteKing.isKingInCheck(blackFiguresList));
	}
}