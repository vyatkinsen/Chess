package main.java.sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainWindow extends Scene {
	public static final int BLACK = 0;
	public static final int WHITE = 1;

	private int currentPlayer = WHITE;
	private Board chessBoard;
	private King blackKing;
	private King whiteKing;

	private final Pane root;

	private Button previousButton;
	private boolean isMoving = false;

	private final Button[][] buttons = new Button[8][8];

	public MainWindow(Parent parent, double width, double height) {
		super(parent, width, height);
		root = (Pane) parent;
	}

	public void start(Stage boardStage) {
		chessBoard = new Board();
		chessBoard.initBoard();
		blackKing = chessBoard.getBlackKing();
		whiteKing = chessBoard.getWhiteKing();

		buttonOnBoard();
		boardStage.setScene(this);
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

		if (chessBoard.figureInCell(clckdY, clckdX) != null && chessBoard.figureInCell(clckdY, clckdX).getFigureColor() == currentPlayer) {
			cleanBoard();
			bt.setStyle("-fx-background-color: red");
			enableButtons();
			bt.setDisable(false);
			freeMoveGraphic(clckdY, clckdX, chessBoard.figureInCell(clckdY, clckdX).getType());

			if (isMoving) {
				cleanBoard();
				isMoving = false;
			} else isMoving = true;
		} else if (isMoving) {
			int prevY = (int) (previousButton.getLayoutY()/100);
			int prevX = (int) (previousButton.getLayoutX()/100);

			if (chessBoard.figureInCell(prevY, prevX).canMoveTo(clckdY, clckdX)) chessBoard.figureInCell(prevY, prevX).movingFigure(clckdY, clckdX);

			if (chessBoard.getCheck()) { //Проверка на шах
				cleanBoard();
				isMoving = false;
				return;
			} else { //Если не было шаха
				if (chessBoard.figureInCell(clckdY, clckdX).getType() == FigureType.PAWN &&
						Math.abs(clckdX - prevX) == 1 && Math.abs(clckdY - prevY) == 1 &&
						chessBoard.figureInCell(prevY, clckdX) == null){
					 buttons[prevY][clckdX].setGraphic(null);
				}
				bt.setGraphic(previousButton.getGraphic());
				pawnToQueenCheck(clckdY, clckdX);
				previousButton.setGraphic(null);

				if (blackKing.getIsCastling() && currentPlayer == BLACK || whiteKing.getIsCastling() && currentPlayer == WHITE) {
					if (currentPlayer == BLACK) blackKing.setIsCastling(false);
					else whiteKing.setIsCastling(false);
					isCastling(prevY, clckdX);
				}

				isMoving = false;
				cleanBoard();
				changePlayer();
			}
//			if (chessBoard.noMovesLeft(currentPlayer)){
//			cleanBoard();
//			enableButtons();
//			showWinner(); }

		}
		chessBoard.printBoard();
		previousButton = bt;
	}

	private void freeMoveGraphic(int yPos, int xPos, FigureType type) {
		int offset;
		if (currentPlayer == WHITE) offset = -1;
		else offset = 1;

		switch (type) {
			case KING -> {
				verticalAndHorizontalMovesGraphic(yPos, xPos, true);
				diagonalMovesGraphic(yPos, xPos, true);
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 5) == null &&
						!blackKing.getIsCastling() && chessBoard.figureInCell(0, 7) != null &&
						chessBoard.figureInCell(0, 7).getType() == FigureType.ROOK &&
						!chessBoard.figureInCell(0, 7).getIsMoved() &&
						chessBoard.isCellBroken(BLACK, 0, 5)){
					freeCellCheck(0, 6);
				}
				if (currentPlayer == BLACK && yPos == 0 && chessBoard.figureInCell(0, 1) == null &&
						chessBoard.figureInCell(0, 3) == null && !blackKing.getIsCastling() &&
						chessBoard.figureInCell(0, 0) != null && chessBoard.figureInCell(0, 0).getType() == FigureType.ROOK &&
						!chessBoard.figureInCell(0, 0).getIsMoved() &&
						chessBoard.isCellBroken(BLACK, 0, 3)){
					freeCellCheck(0, 2);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 5) == null &&
						!whiteKing.getIsCastling() && chessBoard.figureInCell(7, 7) != null &&
						chessBoard.figureInCell(7, 7).getType() == FigureType.ROOK &&
						!chessBoard.figureInCell(7, 7).getIsMoved() &&
						chessBoard.isCellBroken(WHITE, 7, 5)){
					freeCellCheck(7, 6);
				}
				if (currentPlayer == WHITE && yPos == 7 && chessBoard.figureInCell(7, 1) == null &&
						chessBoard.figureInCell(7, 3) == null && !whiteKing.getIsCastling() &&
						chessBoard.figureInCell(7, 0) != null &&
						chessBoard.figureInCell(7, 0).getType() == FigureType.ROOK &&
						!chessBoard.figureInCell(7, 0).getIsMoved() &&
						chessBoard.isCellBroken(WHITE, 7, 3)){
					freeCellCheck(7, 2);
				}
			}
			case QUEEN -> {
				verticalAndHorizontalMovesGraphic(yPos, xPos, false);
				diagonalMovesGraphic(yPos, xPos, false);
			}
			case BISHOP -> diagonalMovesGraphic(yPos, xPos,false);
			case KNIGHT -> horseMovesGraphic(yPos, xPos);
			case ROOK -> verticalAndHorizontalMovesGraphic(yPos, xPos, false);
			case PAWN -> {
				if (currentPlayer == BLACK && yPos == 1 && chessBoard.figureInCell(yPos + 1, xPos) == null && chessBoard.figureInCell(yPos + 2, xPos) == null) {
					buttons[yPos + 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + 2][xPos].setDisable(false);
				}
				if (currentPlayer == WHITE && yPos == 6 && chessBoard.figureInCell(yPos - 1, xPos) == null && chessBoard.figureInCell(yPos - 2, xPos) == null) {
					buttons[yPos - 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos - 2][xPos].setDisable(false);
				}
				if (chessBoard.insideBoard(yPos + offset, xPos) && chessBoard.figureInCell(yPos + offset, xPos) == null) {
					buttons[yPos + offset][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos].setDisable(false);
				}
				if ((chessBoard.insideBoard(yPos + offset, xPos + 1) && chessBoard.figureInCell(yPos + offset, xPos + 1) != null &&
						chessBoard.figureInCell(yPos + offset, xPos + 1).getFigureColor() != currentPlayer) ||
						(yPos + offset == chessBoard.getyPawnBrokenCell() && xPos + 1 == chessBoard.getxPawnBrokenCell() && currentPlayer != chessBoard.getColorOfPawnBrokenCell())) {
					buttons[yPos + offset][xPos + 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos + 1].setDisable(false);
				}
				if ((chessBoard.insideBoard(yPos + offset, xPos - 1) && chessBoard.figureInCell(yPos + offset, xPos - 1) != null &&
						chessBoard.figureInCell(yPos + offset, xPos - 1).getFigureColor() != currentPlayer) ||
						(yPos + offset == chessBoard.getyPawnBrokenCell() && xPos - 1 == chessBoard.getxPawnBrokenCell() && currentPlayer != chessBoard.getColorOfPawnBrokenCell())) {
					buttons[yPos + offset][xPos - 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos - 1].setDisable(false);
				}
			}
		}
	}

	private void diagonalMovesGraphic(int yPos, int xPos, boolean isOneStep) {
		int j = xPos + 1;
		for(int i = yPos-1; i >= 0; i--) {
			if (chessBoard.insideBoard(i, j) && !freeCellCheck(i, j)) {
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
			if (chessBoard.insideBoard(i, j) && !freeCellCheck(i, j)) {
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
			if (chessBoard.insideBoard(i, j) && !freeCellCheck(i, j)) {
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
			if (chessBoard.insideBoard(i, j) && !freeCellCheck(i, j)) {
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
			if (chessBoard.insideBoard(i, xPos) && !freeCellCheck(i, xPos)) break;
			if (isOneStep) break;
		}

		for (int i = yPos - 1; i >= 0; i--){
			if (chessBoard.insideBoard(i, xPos) && !freeCellCheck(i, xPos)) break;
			if (isOneStep) break;
		}

		for (int j = xPos + 1; j < 8; j++) {
			if (chessBoard.insideBoard(yPos, j) && !freeCellCheck(yPos, j)) break;
			if (isOneStep) break;
		}

		for (int j = xPos - 1; j >= 0; j--) {
			if (chessBoard.insideBoard(yPos, j) && !freeCellCheck(yPos, j)) break;
			if (isOneStep) break;
		}
	}

	private void horseMovesGraphic(int yPos, int xPos) {
		if (chessBoard.insideBoard(yPos - 2, xPos + 1)) freeCellCheck(yPos - 2, xPos + 1);
		if (chessBoard.insideBoard(yPos - 2, xPos - 1)) freeCellCheck(yPos - 2, xPos - 1);
		if (chessBoard.insideBoard(yPos + 2, xPos + 1)) freeCellCheck(yPos + 2, xPos + 1);
		if (chessBoard.insideBoard(yPos + 2, xPos - 1)) freeCellCheck(yPos + 2, xPos - 1);
		if (chessBoard.insideBoard(yPos - 1, xPos + 2)) freeCellCheck(yPos - 1, xPos + 2);
		if (chessBoard.insideBoard(yPos - 1, xPos - 2)) freeCellCheck(yPos - 1, xPos - 2);
		if (chessBoard.insideBoard(yPos + 1, xPos + 2)) freeCellCheck(yPos + 1, xPos + 2);
		if (chessBoard.insideBoard(yPos + 1, xPos - 2)) freeCellCheck(yPos + 1, xPos - 2);
	}

	private boolean freeCellCheck(int yPos, int xPos) {
		if (chessBoard.figureInCell(yPos, xPos) == null) {
			buttons[yPos][xPos].setStyle("-fx-background-color: yellow");
			buttons[yPos][xPos].setDisable(false);
		} else {
			if (chessBoard.figureInCell(yPos, xPos).getFigureColor() != currentPlayer) {
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

	private void pawnToQueenCheck(int clckdY, int clckdX) {
		if (chessBoard.figureInCell(clckdY, clckdX).getType() == FigureType.QUEEN && (clckdY == 0 && currentPlayer == WHITE || clckdY == 7 && currentPlayer == BLACK)){
			if (currentPlayer == WHITE) buttons[clckdY][clckdX].setGraphic(new ImageView("Queen1.png"));
			else buttons[clckdY][clckdX].setGraphic(new ImageView("Queen0.png"));
		}
	}

	private void isCastling(int yKing, int xMove){
		switch (yKing) {
			case 0 -> {
				if (xMove == 6){
					buttons[0][5].setGraphic(new ImageView("Rook0.png"));
					buttons[0][7].setGraphic(null);
				} else if (xMove == 2){
					buttons[0][3].setGraphic(new ImageView("Rook0.png"));
					buttons[0][0].setGraphic(null);
				}
			}
			case 7 -> {
				if (xMove == 6) {
					buttons[7][5].setGraphic(new ImageView("Rook1.png"));
					buttons[7][7].setGraphic(null);
				} else if (xMove == 2) {
					buttons[7][3].setGraphic(new ImageView("Rook1.png"));
					buttons[7][0].setGraphic(null);
				}
			}
		}
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

}