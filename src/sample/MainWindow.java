package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Arrays;

public class MainWindow extends Application {
	private final Pane root = new Pane();
	private Button[][] buttons = new Button[8][8];
	private Button previousButton;
	private boolean isMoving = false;
	private int currPlayer = 1;
	private boolean checkMate = false;
	private Pair<Integer, Integer> whiteKingPosition = new Pair<>(0, 4);
	private Pair<Integer, Integer>  blackKingPosition = new Pair<>(7, 4);


	private int[][] board = {
			{15,14,13,12,11,13,14,15 },
			{16,16,16,16,16,16,16,16 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{0,0,0,0,0,0,0,0 },
			{26,26,26,26,26,26,26,26 },
			{25,24,23,22,21,23,24,25 }
	};

	@Override
	public void start(Stage boardStage) {
		buttonOnBoard();
		boardStage.setScene(new Scene(root, 800, 800));
		boardStage.show();
	}

	public void buttonOnBoard(){
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Button cell = new Button();
				cell.setMinHeight(100);
				cell.setMinWidth(100);
				cell.setLayoutX(i * 100);
				cell.setLayoutY(j * 100);
				buttons[j][i] = cell;
				if (board[j][i] != 0) cell.setGraphic(new ImageView("resources/" + board[j][i] + ".png"));
				if ((i + j) % 2 == 0) cell.setStyle("-fx-background-color: white");
				else cell.setStyle("-fx-background-color: grey");
				cell.setOnMouseClicked(event -> press(cell));
				root.getChildren().add(cell);
			}
		}
	}

	public void press(Button bt){
		if (previousButton != null) previousButton.setStyle(previousButton.getStyle());

		int clckdX = (int) (bt.getLayoutX()/100);
		int clckdY = (int) (bt.getLayoutY()/100);
		int clickedButton = board[clckdY][clckdX];

		if (clickedButton != 0 && clickedButton / 10 == currPlayer) { //Проверка на наличие пешки на кнопке и очередность хода
			cleanBoard();
			bt.setStyle("-fx-background-color: red"); //Подсвечиваем выбранную фигуру
			enableButtons(); //Активируем все кнопки для того, чтобы можно было походить
			bt.setDisable(false);
			freeMove(clckdY, clckdX, clickedButton); // Отображаем все доступные ходы для выбранной фигуры
			checkMateDetector();
			if (isMoving){ // Если ход осуществился
				cleanBoard(); // Возращаем расцветку доски
				if ((clckdX + clckdY) % 2 == 0) bt.setStyle("-fx-background-color: white");
				else bt.setStyle("-fx-background-color: grey");
				disableButtons();
				isMoving = false;
			} else isMoving = true; // Если фигура была взята, то происходит прослушивание доски на наличие нажатия на другую кнопку
		} else if (isMoving){ // Если на свободный ход было произведено нажатие
			int prevY = (int) (previousButton.getLayoutY()/100);
			int prevX = (int) (previousButton.getLayoutX()/100);

			board[clckdY][clckdX] = board[prevY][prevX];//Меняем место фигуры внутри массива
			board[prevY][prevX] = 0;

			bt.setGraphic(previousButton.getGraphic());//Меняем местами изображения фигур
			previousButton.setGraphic(null);

			isMoving = false;
			if (board[clckdY][clckdX] == 26 || board[clckdY][clckdX] == 16)	pawnToQueenCheck(clckdX, clckdY); // Проверяем не стала ли пешка ферзем
			cleanBoard(); // Возращаем расцветку доски
			disableButtons();
			changePlayer(); // Меняем очередность хода
		}
		previousButton = bt;
	}

	public void checkMateDetector(){
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (buttons[i][j].getStyle() == "-fx-background-color: yellow" && (board[i][j] == 11 || board[i][j] == 21)) checkMate = true;
			}
		}
		System.out.println(checkMate);
	}

	public void freeMove(int yPos, int xPos, int figure){
		int offset;
		if (currPlayer == 1) offset = 1;
		else offset = -1;

		switch (figure % 10) {
			case 1 -> {
				verticalAndHorizontalMoves(yPos, xPos, true);
				diagonalMoves(yPos, xPos, true);
			}
			case 2 -> {
				verticalAndHorizontalMoves(yPos, xPos, false);
				diagonalMoves(yPos, xPos, false);
			}
			case 3 -> diagonalMoves(yPos, xPos,false);
			case 4 -> horseMoves(yPos, xPos);
			case 5 -> verticalAndHorizontalMoves(yPos, xPos, false);
			case 6 -> {
				if (currPlayer == 1 && yPos == 1) {
					buttons[yPos + 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + 2][xPos].setDisable(false);
				}
				if (currPlayer == 2 && yPos == 6) {
					buttons[yPos - 2][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos - 2][xPos].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos) && board[yPos + offset][xPos] == 0) {
					buttons[yPos + offset][xPos].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos + 1) && // Проверка на возможность побить вражескую фигуру
						board[yPos + offset][xPos + 1] != 0 &&
						board[yPos + offset][xPos + 1] / 10 != currPlayer) {
					buttons[yPos + offset][xPos + 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos + 1].setDisable(false);
				}
				if (InsideBorder(yPos + offset, xPos - 1) && // Проверка на возможность побить вражескую фигуру
						board[yPos + offset][xPos - 1] != 0 &&
						board[yPos + offset][xPos - 1] / 10 != currPlayer) {
					buttons[yPos + offset][xPos - 1].setStyle("-fx-background-color: yellow");
					buttons[yPos + offset][xPos - 1].setDisable(false);
				}
			}
		}
	}

	public void diagonalMoves(int yPos, int xPos, boolean isOneStep) {
		int j = xPos + 1;
		for(int i = yPos-1; i >= 0; i--) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) break;
			if (j < 7) j++;
			else break;
			if (isOneStep) break;
		}

		j = xPos - 1;
		for (int i = yPos - 1; i >= 0; i--) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) break;
			if (j > 0) j--;
			else break;
			if (isOneStep) break;
		}

		j = xPos - 1;
		for (int i = yPos + 1; i < 8; i++) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) break;
			if (j > 0) j--;
			else break;
			if (isOneStep) break;
		}

		j = xPos + 1;
		for (int i = yPos + 1; i < 8; i++) {
			if (InsideBorder(i, j) && !freeCellCheck(i, j)) break;
			if (j <7) j++;
			else break;
			if (isOneStep) break;
		}
	}

	public void verticalAndHorizontalMoves(int yPos, int xPos, boolean isOneStep) {
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

	public void horseMoves(int yPos, int xPos) {
		if (InsideBorder(yPos - 2, xPos + 1)) freeCellCheck(yPos - 2, xPos + 1);
		if (InsideBorder(yPos - 2, xPos - 1)) freeCellCheck(yPos - 2, xPos - 1);
		if (InsideBorder(yPos + 2, xPos + 1)) freeCellCheck(yPos + 2, xPos + 1);
		if (InsideBorder(yPos + 2, xPos - 1)) freeCellCheck(yPos + 2, xPos - 1);
		if (InsideBorder(yPos - 1, xPos + 2)) freeCellCheck(yPos - 1, xPos + 2);
		if (InsideBorder(yPos - 1, xPos - 2)) freeCellCheck(yPos - 1, xPos - 2);
		if (InsideBorder(yPos + 1, xPos + 2)) freeCellCheck(yPos + 1, xPos + 2);
		if (InsideBorder(yPos + 1, xPos - 2)) freeCellCheck(yPos + 1, xPos - 2);
	}

	public void enableButtons() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setDisable(true);
			}
		}
	}

	public void disableButtons() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setDisable(false);
			}
		}
	}

	public void cleanBoard(){
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) buttons[j][i].setStyle("-fx-background-color: white");
				else buttons[j][i].setStyle("-fx-background-color: grey");
			}
		}
	}

	public boolean freeCellCheck(int yPos, int xPos) {
		if (board[yPos][xPos] == 0) {
			buttons[yPos][xPos].setStyle("-fx-background-color: yellow");
			buttons[yPos][xPos].setDisable(false);
		} else {
			if (board[yPos][xPos] / 10 != currPlayer) {
				buttons[yPos][xPos].setStyle("-fx-background-color: yellow");
				buttons[yPos][xPos].setDisable(false);
			}
			return false;
		}
		return true;
	}

	public boolean InsideBorder(int y,int x) { return y < 8 && x < 8 && y >= 0 && x >= 0; }

	public void pawnToQueenCheck(int clckdX, int clckdY){
		if ((clckdY == 0 && currPlayer == 2) || (clckdY == 7 && currPlayer == 1)){
			if (currPlayer == 2) {
				board[clckdY][clckdX] = 22;
				buttons[clckdY][clckdX].setGraphic(new ImageView("resources/" + 22 + ".png"));
			} else {
				board[clckdY][clckdX] = 12;
				buttons[clckdY][clckdX].setGraphic(new ImageView("resources/" + 12 + ".png"));
			}
		}
	}

	public void changePlayer() {
		if (currPlayer == 1) currPlayer = 2;
		else currPlayer = 1;
	}

	public static void main(String[] args) { launch(args); }
}