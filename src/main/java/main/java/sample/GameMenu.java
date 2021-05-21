package main.java.sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import 	javafx.scene.control.Button;

public class GameMenu extends Application {

	public static Button newGame = new Button("НОВАЯ ИГРА");
	public static Button exitGame = new Button("ВЫХОД");

	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();
		primaryStage.setTitle("Шахматы");
		primaryStage.setScene(new Scene(root, 800, 800));
		primaryStage.setResizable(false);
		ImageView img = new ImageView("menuImage.jpg");
		img.setFitHeight(800);
		img.setFitWidth(800);

		newGame.setMinHeight(20);
		newGame.setMinWidth(200);
		exitGame.setMinHeight(20);
		exitGame.setMinWidth(200);
		newGame.setTranslateX(50);
		newGame.setTranslateY(100);
		exitGame.setTranslateX(50);
		exitGame.setTranslateY(135);
		newGame.setOpacity(0.8);
		exitGame.setOpacity(0.8);
		newGame.setStyle("-fx-background-color: white");
		exitGame.setStyle("-fx-background-color: white");

		newGame.setOnMouseClicked(event -> new MainWindow( new Pane(), 800, 800).start(primaryStage));
		exitGame.setOnMouseClicked(event -> System.exit(0));

		root.getChildren().addAll(img, newGame, exitGame);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}