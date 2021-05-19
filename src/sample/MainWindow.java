package sample;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.LinkedList;

public class MainWindow extends Application {
	public static final int BLACK = 0;
	public static final int WHITE = 1;

	private int currentPlayer = WHITE;
	private Board chessBoard;
	private LinkedList<Figure> blackFiguresList;
	private LinkedList<Figure> whiteFiguresList;
	private King blackKing;
	private King whiteKing;

	private final Pane root = new Pane();

	private Button previousButton;
	private boolean isMoving = false;

	private final Button[][] buttons = new Button[8][8];
	private final int[][] boardForRendering = {
			{5,4,3,2,1,3,4,5 },
			{6,6,6,6,6,6,6,6 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{16,16,16,16,16,16,16,16 },
			{15,14,13,12,11,13,14,15 }
	};

	@Override
	public void start(Stage boardStage) {
		chessBoard = new Board();
		currentPlayer = WHITE;
		blackFiguresList = new LinkedList<>();
		whiteFiguresList = new LinkedList<>();
		blackKing = new King(chessBoard, BLACK, 0, 4);
		whiteKing = new King(chessBoard, WHITE, 7, 4);
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
		buttonOnBoard();
		boardStage.setScene(new Scene(root, 800, 800));
		boardStage.show();
	}

	private void buttonOnBoard(){
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Button cell = new Button();
				buttons[j][i] = cell;

				cell.setMinHeight(100);
				cell.setMinWidth(100);
				cell.setLayoutX(i * 100);
				cell.setLayoutY(j * 100);

				if (chessBoard.figureInCell(j, i) != null) {
					cell.setGraphic(new ImageView("resources/" + chessBoard.figureInCell(j, i) + chessBoard.figureInCell(j, i).getPlayerColor() + ".png"));
				}

				if ((i + j) % 2 == 0) {
					cell.setStyle("-fx-background-color: white");
				} else {
					cell.setStyle("-fx-background-color: grey");
				}

				cell.setOnMouseClicked(event -> press(cell));
				root.getChildren().add(cell);
			}
		}
	}

	private void press(Button bt){
		int clckdX = (int) (bt.getLayoutX()/100);
		int clckdY = (int) (bt.getLayoutY()/100);
		int clickedButton = boardForRendering[clckdY][clckdX];

		if (clickedButton != 0 && clickedButton / 10 == currentPlayer) {
			cleanBoard();
			bt.setStyle("-fx-background-color: red");
			enableButtons();
			bt.setDisable(false);
			freeMoveGraphic(clckdY, clckdX, clickedButton);

			if (isMoving) {
				cleanBoard();
				isMoving = false;
			} else isMoving = true;
		} else if (isMoving) {
			int prevY = (int) (previousButton.getLayoutY()/100);
			int prevX = (int) (previousButton.getLayoutX()/100);
			Figure tempFig = chessBoard.figureInCell(prevY, prevX);
			int col = chessBoard.figureInCell(prevY, prevX).getPlayerColor();

			if (chessBoard.figureInCell(prevY, prevX).canMoveTo(clckdY, clckdX)) {
				chessBoard.figureInCell(prevY, prevX).movingFigure(clckdY, clckdX);
			}

			if (currentPlayer == BLACK && isKingInCheck(BLACK) || currentPlayer == WHITE && isKingInCheck(WHITE)) {
				chessBoard.figureInCell(clckdY, clckdX).removeFigure();
				addNewFigure(prevY, prevX, tempFig.toString(), col);
				cleanBoard();
				isMoving = false;
				return;
			} else {
				boardForRendering[clckdY][clckdX] = boardForRendering[prevY][prevX];
				boardForRendering[prevY][prevX] = 0;

				bt.setGraphic(previousButton.getGraphic());
				previousButton.setGraphic(null);

				if (boardForRendering[clckdY][clckdX] == 6 || boardForRendering[clckdY][clckdX] == 16) {
					pawnToQueenCheck(clckdY, clckdX);
				}
				if (boardForRendering[clckdY][clckdX] == 1 || boardForRendering[clckdY][clckdX] == 11){
					isCastling(prevY, clckdX);
				}

				isMoving = false;
				cleanBoard();
				changePlayer();
			}
		}
//		if (noMovesLeft(currentPlayer)) {
//			cleanBoard();
//			enableButtons();
//			showWinner();
//		}
		previousButton = bt;
	}

	private void isCastling(int yKing, int xMove){
		switch (yKing) {
			case 0 -> {
				if (xMove == 6){
					boardForRendering[0][5] = boardForRendering[0][7];
					boardForRendering[0][7] = 0;
					chessBoard.figureInCell(0, 7).removeFigure();
					addRook(BLACK, 0, 5);
					buttons[0][5].setGraphic(new ImageView("resources/Rook0.png"));
					buttons[0][7].setGraphic(null);
				} else if (xMove == 2){
					boardForRendering[0][3] = boardForRendering[0][0];
					boardForRendering[0][0] = 0;
					chessBoard.figureInCell(0, 0).removeFigure();
					addRook(BLACK, 0, 3);
					buttons[0][3].setGraphic(new ImageView("resources/Rook0.png"));
					buttons[0][0].setGraphic(null);
				}
			}
			case 7 -> {
				if (xMove == 6){
					boardForRendering[7][5] = boardForRendering[7][7];
					boardForRendering[7][7] = 0;
					chessBoard.figureInCell(7, 7).removeFigure();
					addRook(WHITE, 7, 5);
					buttons[7][5].setGraphic(new ImageView("resources/Rook1.png"));
					buttons[7][7].setGraphic(null);
				} else if (xMove == 2){
					boardForRendering[7][3] = boardForRendering[7][0];
					boardForRendering[7][0] = 0;
					chessBoard.figureInCell(7, 0).removeFigure();
					addRook(WHITE, 7, 3);
					buttons[7][3].setGraphic(new ImageView("resources/Rook1.png"));
					buttons[7][0].setGraphic(null);
				}
			}
		}
	}

	private void freeMoveGraphic(int yPos, int xPos, int figure) {
		int offset;
		if (currentPlayer == WHITE) {
			offset = -1;
		} else offset = 1;

		switch (figure % 10) {
			case 1 -> {
				verticalAndHorizontalMovesGraphic(yPos, xPos, true);
				diagonalMovesGraphic(yPos, xPos, true);
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 5) == null &&
						!blackKing.getIsMoved() && !chessBoard.figureInCell(0, 7).getIsMoved()){
					freeCellCheck(0, 6);
				}
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 1) == null &&
						chessBoard.figureInCell(0, 3) == null && !blackKing.getIsMoved() &&
						!chessBoard.figureInCell(0, 0).getIsMoved()){
					freeCellCheck(0, 2);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 5) == null &&
						!whiteKing.getIsMoved() && !chessBoard.figureInCell(7, 7).getIsMoved()){
					freeCellCheck(7, 6);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 1) == null &&
						chessBoard.figureInCell(7, 3) == null && !whiteKing.getIsMoved() &&
						!chessBoard.figureInCell(7, 0).getIsMoved()){
					freeCellCheck(7, 2);
				}
			}
			case 2 -> {
				verticalAndHorizontalMovesGraphic(yPos, xPos, false);
				diagonalMovesGraphic(yPos, xPos, false);
			}
			case 3 -> diagonalMovesGraphic(yPos, xPos,false);
			case 4 -> horseMovesGraphic(yPos, xPos);
			case 5 -> verticalAndHorizontalMovesGraphic(yPos, xPos, false);
			case 6 -> {
				if (currentPlayer == BLACK && yPos == 1 && boardForRendering[yPos + 1][xPos] == 0 && boardForRendering[yPos + 2][xPos] == 0) {
					buttons[yPos + 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + 2][xPos].setDisable(false);
				}
				if (currentPlayer == WHITE && yPos == 6 && boardForRendering[yPos - 1][xPos] == 0 && boardForRendering[yPos - 2][xPos] == 0) {
					buttons[yPos - 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos - 2][xPos].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos) && boardForRendering[yPos + offset][xPos] == 0) {
					buttons[yPos + offset][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos + 1) &&
						boardForRendering[yPos + offset][xPos + 1] != 0 &&
						boardForRendering[yPos + offset][xPos + 1] / 10 != currentPlayer) {
					buttons[yPos + offset][xPos + 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos + 1].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos - 1) && // Проверка на возможность побить вражескую фигуру
						boardForRendering[yPos + offset][xPos - 1] != 0 &&
						boardForRendering[yPos + offset][xPos - 1] / 10 != currentPlayer) {
					buttons[yPos + offset][xPos - 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos - 1].setDisable(false);
				}
			}
		}
	}

	private void diagonalMovesGraphic(int yPos, int xPos, boolean isOneStep) {
		int j = xPos + 1;
		for(int i = yPos-1; i >= 0; i--) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) {
				break;
			}

			if (j < 7) {
				j++;
			} else break;

			if (isOneStep) {
				break;
			}
		}

		j = xPos - 1;
		for (int i = yPos - 1; i >= 0; i--) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) {
				break;
			}

			if (j > 0) {
				j--;
			} else break;

			if (isOneStep) {
				break;
			}
		}

		j = xPos - 1;
		for (int i = yPos + 1; i < 8; i++) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) {
				break;
			}

			if (j > 0) {
				j--;
			} else break;

			if (isOneStep) {
				break;
			}
		}

		j = xPos + 1;
		for (int i = yPos + 1; i < 8; i++) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) {
				break;
			}

			if (j <7) {
				j++;
			} else break;

			if (isOneStep) {
				break;
			}
		}
	}

	private void verticalAndHorizontalMovesGraphic(int yPos, int xPos, boolean isOneStep) {
		for (int i = yPos + 1; i < 8; i++) {
			if (InsideBorder(i, xPos) && !freeCellCheck(i, xPos)) break;
			if (isOneStep) break;
		}

		for (int i = yPos - 1; i >= 0; i--){
			if (InsideBorder(i, xPos) && !freeCellCheck(i, xPos)) break;
			if (isOneStep) break;
		}

		for (int j = xPos + 1; j < 8; j++) {
			if (InsideBorder(yPos, j) && !freeCellCheck(yPos, j)) break;
			if (isOneStep) break;
		}

		for (int j = xPos - 1; j >= 0; j--) {
			if (InsideBorder(yPos, j) && !freeCellCheck(yPos, j)) break;
			if (isOneStep) break;
		}
	}

	private void horseMovesGraphic(int yPos, int xPos) {
		if (InsideBorder(yPos - 2, xPos + 1)) freeCellCheck(yPos - 2, xPos + 1);
		if (InsideBorder(yPos - 2, xPos - 1)) freeCellCheck(yPos - 2, xPos - 1);
		if (InsideBorder(yPos + 2, xPos + 1)) freeCellCheck(yPos + 2, xPos + 1);
		if (InsideBorder(yPos + 2, xPos - 1)) freeCellCheck(yPos + 2, xPos - 1);
		if (InsideBorder(yPos - 1, xPos + 2)) freeCellCheck(yPos - 1, xPos + 2);
		if (InsideBorder(yPos - 1, xPos - 2)) freeCellCheck(yPos - 1, xPos - 2);
		if (InsideBorder(yPos + 1, xPos + 2)) freeCellCheck(yPos + 1, xPos + 2);
		if (InsideBorder(yPos + 1, xPos - 2)) freeCellCheck(yPos + 1, xPos - 2);
	}

	private boolean freeCellCheck(int yPos, int xPos) {
		if (boardForRendering[yPos][xPos] == 0) {
			buttons[yPos][xPos].setStyle("-fx-background-color: yellow");
			buttons[yPos][xPos].setDisable(false);
		} else {
			if (boardForRendering[yPos][xPos] / 10 != currentPlayer) {
				buttons[yPos][xPos].setStyle("-fx-background-color: yellow");
				buttons[yPos][xPos].setDisable(false);
			}
			return false;
		}
		return true;
	}

	private void enableButtons() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setDisable(true);
			}
		}
	}

	private void cleanBoard(){
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					buttons[j][i].setStyle("-fx-background-color: white");
				} else {
					buttons[j][i].setStyle("-fx-background-color: grey");
				}
				buttons[j][i].setDisable(false);
			}
		}
	}

	private boolean InsideBorder(int y,int x) {
		return y < 8 && x < 8 && y >= 0 && x >= 0;
	}

	private void pawnToQueenCheck(int clckdY, int clckdX) {
		if ((clckdY == 0 && currentPlayer == WHITE) || (clckdY == 7 && currentPlayer == BLACK)){
			if (currentPlayer == WHITE) {
				boardForRendering[clckdY][clckdX] = 12;
				buttons[clckdY][clckdX].setGraphic(new ImageView("resources/Queen1.png"));
				addQueen(WHITE, clckdY, clckdX);
			} else {
				boardForRendering[clckdY][clckdX] = 2;
				buttons[clckdY][clckdX].setGraphic(new ImageView("resources/Queen0.png"));
				chessBoard.figureInCell(clckdY, clckdX).removeFigure();
				addQueen(BLACK, clckdY, clckdX);
			}
		}
	}

	private boolean noMovesLeft(int color) {
		int prevY, prevX;
		Figure figure;
		LinkedList<Figure> enemyFigures;

		if (color == BLACK) {
			enemyFigures = blackFiguresList;
		} else {
			enemyFigures = whiteFiguresList;
		}
		for (Figure currentPiece : enemyFigures) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {

					if (currentPiece.canMoveTo(j, i)) {
						figure = chessBoard.figureInCell(j, i);

						prevY = currentPiece.getY();
						prevX = currentPiece.getX();

						currentPiece.movingFigure(j, i);

						if (!isKingInCheck(color)) {
							currentPiece.movingFigure(prevY, prevX);
							if (figure != null) {
								figure.movingFigure(j, i);
							}
							return false;
						} else {
							currentPiece.movingFigure(prevY, prevX);
							if (figure != null) {
								figure.movingFigure(j, i);
							}
						}
					}
				}
			}
		}
		return true;
	}

	private boolean isKingInCheck(int color) {
		LinkedList<Figure> enemyFigures;
		King whichKingCheck;

		if (color == BLACK) {
			enemyFigures = whiteFiguresList;
			whichKingCheck = blackKing;
		} else {
			enemyFigures = blackFiguresList;
			whichKingCheck = whiteKing;
		}
		int yKing = whichKingCheck.getY();
		int xKing = whichKingCheck.getX();

		for (Figure enemyFigure : enemyFigures) {
			if (enemyFigure.canMoveTo(yKing, xKing)) {
				return true;
			}
		}
		return false;
	}

	private void changePlayer() {
		if (currentPlayer == WHITE) {
			currentPlayer = BLACK;
		} else currentPlayer = WHITE;
	}

	private void addNewFigure(int yPos, int xPos, String figure, int color) {
		switch (figure) {
			case "Pawn" -> addPawn(color, yPos, xPos);
			case "Knight" -> addKnight(color, yPos, xPos);
			case "Bishop" -> addBishop(color, yPos, xPos);
			case "Queen" -> addQueen(color, yPos, xPos);
			case "Rook" -> {
				addRook(color, yPos, xPos);
				chessBoard.figureInCell(yPos, xPos).setIsMoved(true);
			}
			case "King" -> {
				if (color == 0) {
					blackKing = new King(chessBoard, color, yPos, xPos);
					figuresListAdd(blackKing, color);
				} else {
					whiteKing = new King(chessBoard, color, yPos, xPos);
					figuresListAdd(whiteKing, color);
				}
			}
		}
	}

	private void addQueen(int color, int y, int x){
		Queen queen = new Queen(chessBoard, color, y, x);
		figuresListAdd(queen, color);
	}

	private void addKnight(int color, int y, int x){
		Knight knight = new Knight(chessBoard, color, y, x);
		figuresListAdd(knight, color);
	}

	private void addRook(int color, int y, int x){
		Rook rook = new Rook(chessBoard, color, y, x);
		figuresListAdd(rook, color);
	}

	private void addBishop(int color, int y, int x){
		Bishop bishop = new Bishop(chessBoard, color, y, x);
		figuresListAdd(bishop, color);
	}

	private void addPawn(int color, int y, int x){
		Pawn pawn = new Pawn(chessBoard, color, y, x);
		figuresListAdd(pawn, color);
	}

	private void figuresListAdd(Figure piece, int color){
		if (color == BLACK) {
			blackFiguresList.add(piece);
		} else whiteFiguresList.add(piece);
	}

	public static void main(String[] args) { launch(args); }

	private void showWinner() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Шахматы");

		alert.setHeaderText(null);
		if (currentPlayer == WHITE) {
			alert.setContentText("Шах и мат, белый!\nЧерный тебя обыграл!");
		} else alert.setContentText("Шах и мат, черный!\nБелый тебя обыграл!");
		alert.showAndWait();
	}
}