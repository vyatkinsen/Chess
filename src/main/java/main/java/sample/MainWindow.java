package main.java.sample;

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

	public MainWindow(Pane pane, double width, double height) {
		super(pane, width, height);
		root = pane;
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
				cell.setId(j + "" + i);
				if (chessBoard.figureInCell(j, i) != null) cell.setGraphic(new ImageView("" + chessBoard.figureInCell(j, i) + chessBoard.figureInCell(j, i).getFigureColor() + ".png"));

				if ((i + j) % 2 == 0) cell.setStyle("-fx-background-color: white");
				else cell.setStyle("-fx-background-color: grey");
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
			bt.setStyle("-fx-background-color: lightcoral");
			enableButtons();
			bt.setDisable(false);
			freeMoveGraphic(chessBoard.figureInCell(clckdY, clckdX));

			if (isMoving) {
				cleanBoard();
				isMoving = false;
			} else isMoving = true;
		} else if (isMoving) {
			int prevY = (int) (previousButton.getLayoutY()/100);
			int prevX = (int) (previousButton.getLayoutX()/100);

			if (chessBoard.figureInCell(prevY, prevX).canMoveTo(clckdY, clckdX)) chessBoard.figureInCell(prevY, prevX).movingFigure(clckdY, clckdX);

			if (chessBoard.getCheck()) {
				cleanBoard();
				isMoving = false;
				return;
			} else {
				if (chessBoard.figureInCell(clckdY, clckdX).getType() == FigureType.PAWN &&
						Math.abs(clckdX - prevX) == 1 && Math.abs(clckdY - prevY) == 1 &&
						chessBoard.figureInCell(prevY, clckdX) == null) buttons[prevY][clckdX].setGraphic(null);
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
			if (chessBoard.noMovesLeft(currentPlayer)){
			cleanBoard();
			enableButtons();
			showWinner(); }
		}
		chessBoard.printBoard();
		previousButton = bt;
	}

	private void freeMoveGraphic(Figure fig) {
		int offset;
		if (currentPlayer == WHITE) offset = -1;
		else offset = 1;
		if (fig.getType() == FigureType.PAWN){
				if (currentPlayer == BLACK && fig.getY() == 1 && chessBoard.figureInCell(fig.getY() + 1, fig.getX()) == null &&
						chessBoard.figureInCell(fig.getY() + 2, fig.getX()) == null) freeCellCheck(fig.getY() + 2, fig.getX());
				if (currentPlayer == WHITE && fig.getY() == 6 && chessBoard.figureInCell(fig.getY() - 1, fig.getX()) == null &&
						chessBoard.figureInCell(fig.getY() - 2, fig.getX()) == null) freeCellCheck(fig.getY() - 2, fig.getX());
				if (chessBoard.insideBoard(fig.getY() + offset, fig.getX()) && chessBoard.figureInCell(fig.getY() + offset, fig.getX()) == null)
					freeCellCheck(fig.getY() + offset, fig.getX());
				if ((chessBoard.insideBoard(fig.getY() + offset, fig.getX() + 1) &&
						chessBoard.figureInCell(fig.getY() + offset, fig.getX() + 1) != null &&
						chessBoard.figureInCell(fig.getY() + offset, fig.getX() + 1).getFigureColor() != currentPlayer) ||
						(fig.getY() + offset == chessBoard.getyPawnBrokenCell() &&
								fig.getX() + 1 == chessBoard.getxPawnBrokenCell() && currentPlayer != chessBoard.getColorOfPawnBrokenCell()))
					freeCellCheck(fig.getY() + offset, fig.getX() + 1);
				if ((chessBoard.insideBoard(fig.getY() + offset, fig.getX() - 1) &&
						chessBoard.figureInCell(fig.getY() + offset, fig.getX() - 1) != null &&
						chessBoard.figureInCell(fig.getY() + offset, fig.getX() - 1).getFigureColor() != currentPlayer) ||
						(fig.getY() + offset == chessBoard.getyPawnBrokenCell() &&
								fig.getX() - 1 == chessBoard.getxPawnBrokenCell() && currentPlayer != chessBoard.getColorOfPawnBrokenCell()))
					freeCellCheck(fig.getY() + offset, fig.getX() - 1);
			} else {
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						if (fig.canMoveTo(y, x)) freeCellCheck(y, x);
					}
				}
			}
		}

	private void freeCellCheck(int yPos, int xPos) {
		if (chessBoard.figureInCell(yPos, xPos) == null) {
			buttons[yPos][xPos].setStyle("-fx-background-color: lightgreen");
			buttons[yPos][xPos].setDisable(false);
		} else {
			if (chessBoard.figureInCell(yPos, xPos).getFigureColor() != currentPlayer) {
				buttons[yPos][xPos].setStyle("-fx-background-color: lightgreen");
				buttons[yPos][xPos].setDisable(false);
			}
		}
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
		if (currentPlayer == WHITE) currentPlayer = BLACK;
		else currentPlayer = WHITE;
	}
}