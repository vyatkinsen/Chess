package main.java.sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.List;

public class MainWindow extends Application {
	public static final int BLACK = 0;
	public static final int WHITE = 1;

	private int currentPlayer = WHITE;
	private Board chessBoard;
	private King blackKing;
	private King whiteKing;
	private int yPawnBrokenCell = -1;
	private int xPawnBrokenCell = -1;

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
		chessBoard.initBoard();
		blackKing = chessBoard.getBlackKing();
		whiteKing = chessBoard.getWhiteKing();

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
					cell.setGraphic(new ImageView("" + chessBoard.figureInCell(j, i) + chessBoard.figureInCell(j, i).getFigureColor() + ".png"));
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
			int col = -1;
			int col2 = -1;
			int prevY = (int) (previousButton.getLayoutY()/100);
			int prevX = (int) (previousButton.getLayoutX()/100);
			Figure tempFig = chessBoard.figureInCell(prevY, prevX);
			Figure secTempFig = chessBoard.figureInCell(clckdY, clckdX);

			if (chessBoard.figureInCell(prevY, prevX) != null) col = chessBoard.figureInCell(prevY, prevX).getFigureColor();

			if (chessBoard.figureInCell(clckdY, clckdX) != null) col2 = chessBoard.figureInCell(clckdY, clckdX).getFigureColor();

			if (chessBoard.figureInCell(prevY, prevX).canMoveTo(clckdY, clckdX)) chessBoard.figureInCell(prevY, prevX).movingFigure(clckdY, clckdX);

			if (currentPlayer == BLACK && isKingInCheck(BLACK) || currentPlayer == WHITE && isKingInCheck(WHITE)) { //Проверка на шах
				chessBoard.figureInCell(clckdY, clckdX).removeFigure();
				chessBoard.addNewFigure(prevY, prevX, tempFig.toString(), col);
				if (secTempFig != null) chessBoard.addNewFigure(clckdY, clckdX, secTempFig.toString(), col2);

				cleanBoard();
				isMoving = false;
				return;
			} else { //Если не было шаха
				boardForRendering[clckdY][clckdX] = boardForRendering[prevY][prevX];
				bt.setGraphic(previousButton.getGraphic());
				previousButton.setGraphic(null);

				if (boardForRendering[clckdY][clckdX] % 10 == 6) {
					pawnToQueenCheck(clckdY, clckdX);
					LinkedList<Figure> enemyFigures;
					int offset;
					if (currentPlayer == BLACK) {
						enemyFigures = chessBoard.getWhiteFiguresList();
						offset = -1;
					} else {
						offset = 1;
						enemyFigures = chessBoard.getBlackFiguresList();
					}

					if (Math.abs(clckdY - prevY) == 2 && chessBoard.isCellBrokenByPawn(enemyFigures, clckdY + offset, clckdX, -offset)) { //Сохранение ячейки битого поля для взятия на проходе
						yPawnBrokenCell = clckdY + offset;
						xPawnBrokenCell = clckdX;
					}

					if (boardForRendering[prevY][prevX] % 10 == 6 && Math.abs(clckdX - prevX) == 1){
						buttons[prevY][clckdX].setGraphic(null);
						if (chessBoard.figureInCell(prevY, clckdX) != null){
							chessBoard.figureInCell(prevY, clckdX).removeFigure();
						}
					}
				}
				boardForRendering[prevY][prevX] = 0;

				if (boardForRendering[clckdY][clckdX] == 1 || boardForRendering[clckdY][clckdX] == 11) isCastling(prevY, clckdX); //Реализация рокировки

				isMoving = false;
				cleanBoard();
				changePlayer();
			}
		}
		if (noMovesLeft(currentPlayer)) {
			cleanBoard();
			enableButtons();
			showWinner();
		}
		previousButton = bt;
	}

	private void freeMoveGraphic(int yPos, int xPos, int figure) {
		int offset;
		if (currentPlayer == WHITE) offset = -1;
		else offset = 1;

		switch (figure % 10) {
			case 1 -> {
				verticalAndHorizontalMovesGraphic(yPos, xPos, true);
				diagonalMovesGraphic(yPos, xPos, true);
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 5) == null &&
						!blackKing.getIsMoved() && !chessBoard.figureInCell(0, 7).getIsMoved() &&
						isCellBroken(BLACK, 0, 5)){
					freeCellCheck(0, 6);
				}
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 1) == null &&
						chessBoard.figureInCell(0, 3) == null && !blackKing.getIsMoved() &&
						!chessBoard.figureInCell(0, 0).getIsMoved() &&
						isCellBroken(BLACK, 0, 3)){
					freeCellCheck(0, 2);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 5) == null &&
						!whiteKing.getIsMoved() && !chessBoard.figureInCell(7, 7).getIsMoved() &&
						isCellBroken(WHITE, 7, 5)){
					freeCellCheck(7, 6);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 1) == null &&
						chessBoard.figureInCell(7, 3) == null && !whiteKing.getIsMoved() &&
						!chessBoard.figureInCell(7, 0).getIsMoved() && isCellBroken(WHITE, 7, 3)){
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
				if (boardForRendering[yPos + offset][xPos] == 0) {
					buttons[yPos + offset][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos].setDisable(false);
				}
				if ((InsideBorder(yPos + offset, xPos + 1) && boardForRendering[yPos + offset][xPos + 1] != 0 &&
						boardForRendering[yPos + offset][xPos + 1] / 10 != currentPlayer) ||
						(yPos + offset == yPawnBrokenCell && xPos + 1 == xPawnBrokenCell)) {
					if (yPos + offset == yPawnBrokenCell && xPos + 1 == xPawnBrokenCell) {
						chessBoard.figureInCell(yPos, xPos).setBrokeCell(true);
					}
					buttons[yPos + offset][xPos + 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos + 1].setDisable(false);
				}
				if ((InsideBorder(yPos + offset, xPos - 1) && boardForRendering[yPos + offset][xPos - 1] != 0 &&
						boardForRendering[yPos + offset][xPos - 1] / 10 != currentPlayer) ||
						(yPos + offset == yPawnBrokenCell && xPos - 1 == xPawnBrokenCell)) {
					if (yPos + offset == yPawnBrokenCell && xPos - 1 == xPawnBrokenCell) {
						 chessBoard.figureInCell(yPos, xPos).setBrokeCell(true);
					}
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
				buttons[clckdY][clckdX].setGraphic(new ImageView("Queen1.png"));
			} else {
				boardForRendering[clckdY][clckdX] = 2;
				buttons[clckdY][clckdX].setGraphic(new ImageView("Queen0.png"));
			}
		}
	}

	private void isCastling(int yKing, int xMove){
		switch (yKing) {
			case 0 -> {
				if (xMove == 6){
					boardForRendering[0][5] = boardForRendering[0][7];
					boardForRendering[0][7] = 0;
					buttons[0][5].setGraphic(new ImageView("Rook0.png"));
					buttons[0][7].setGraphic(null);
				} else if (xMove == 2){
					boardForRendering[0][3] = boardForRendering[0][0];
					boardForRendering[0][0] = 0;
					buttons[0][3].setGraphic(new ImageView("Rook0.png"));
					buttons[0][0].setGraphic(null);
				}
			}
			case 7 -> {
				if (xMove == 6) {
					boardForRendering[7][5] = boardForRendering[7][7];
					boardForRendering[7][7] = 0;
					buttons[7][5].setGraphic(new ImageView("Rook1.png"));
					buttons[7][7].setGraphic(null);
				} else if (xMove == 2) {
					boardForRendering[7][3] = boardForRendering[7][0];
					boardForRendering[7][0] = 0;
					buttons[7][3].setGraphic(new ImageView("Rook1.png"));
					buttons[7][0].setGraphic(null);
				}
			}
		}
	}

	private boolean noMovesLeft(int color) {
		int prevY, prevX;
		Figure tempFigure;
		List<Figure> correctFiguresList;

		if (color == BLACK) {
			correctFiguresList = chessBoard.getBlackFiguresList();
		} else {
			correctFiguresList = chessBoard.getWhiteFiguresList();
		}
		for (Figure currFigure: correctFiguresList) {
			boolean move = currFigure.getIsMoved();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {

					if (currFigure.canMoveTo(j, i)) {
						tempFigure = chessBoard.figureInCell(j, i);
						prevY = currFigure.getY();
						prevX = currFigure.getX();

						currFigure.movingFigure(j, i);

						if (!isKingInCheck(color)) {
							currFigure.movingFigure(prevY, prevX);
							if (tempFigure != null) {
								chessBoard.addNewFigure(j, i, tempFigure.toString(), tempFigure.getFigureColor());
							}
							currFigure.setIsMoved(move);
							return false;
						} else {
							currFigure.movingFigure(prevY, prevX);
							if (tempFigure != null) {
								chessBoard.addNewFigure(j, i, tempFigure.toString(), tempFigure.getFigureColor());
							}
						}
					}
				}
			}
			currFigure.setIsMoved(move);
		}
		return true;
	}

	private boolean isKingInCheck(int color) {
		if (color == BLACK) {
			return blackKing.isKingInCheck(chessBoard.getWhiteFiguresList());
		} else {
			return whiteKing.isKingInCheck(chessBoard.getBlackFiguresList());
		}
	}

	private boolean isCellBroken(int color, int yPos, int xPos) {
		LinkedList<Figure> enemyFigures;
		if (color == BLACK) {
			enemyFigures = chessBoard.getWhiteFiguresList();
		} else {
			enemyFigures = chessBoard.getBlackFiguresList();
		}
		for (Figure enemyFigure : enemyFigures) {
			if (enemyFigure.canMoveTo(yPos, xPos)) {
				return false;
			}
		}
		return true;
	}



	private void showWinner() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Шахматы");

		alert.setHeaderText(null);
		if (currentPlayer == WHITE) {
			alert.setContentText("Шах и мат, белый!\nЧерный тебя обыграл!");
		} else alert.setContentText("Шах и мат, черный!\nБелый тебя обыграл!");
		alert.showAndWait();
	}

	private void changePlayer() {
		if (currentPlayer == WHITE) {
			currentPlayer = BLACK;
		} else currentPlayer = WHITE;
	}

	public static void main(String[] args) {
		launch(args);
	}
}