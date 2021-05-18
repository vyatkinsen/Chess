package sample;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;



public class GameMenu extends Application {

	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();
		ImageView img = new ImageView("resources/menuImage.jpg");
		img.setFitHeight(800);
		img.setFitWidth(800);
		menuButton newGame = new menuButton("НОВАЯ ИГРА");
		menuButton exitGame = new menuButton("ВЫХОД");
		menuGroup mainMenu = new menuGroup(newGame,exitGame);
		newGame.setOnMouseClicked(event -> new sample.MainWindow().start(primaryStage));
		exitGame.setOnMouseClicked(event -> System.exit(0));
		root.getChildren().add(img);
		root.getChildren().addAll(new MenuBox(mainMenu));
		primaryStage.setTitle("Шахматы");
		primaryStage.setScene(new Scene(root, 800, 800));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private static class menuButton extends StackPane {
		public menuButton(String name){
			Rectangle bg = new Rectangle(200,20, Color.WHITE);
			bg.setOpacity(0.5);

			Text text = new Text(name);
			text.setFill(Color.WHITE);
			text.setFont(Font.font("Arial", FontWeight.BOLD,14));

			setAlignment(Pos.CENTER);
			getChildren().addAll(bg,text);
			FillTransition st = new FillTransition(Duration.seconds(0.5),bg);
			setOnMouseEntered(event -> {
				st.setFromValue(Color.DARKGRAY);
				st.setToValue(Color.YELLOWGREEN);
				st.setCycleCount(Animation.INDEFINITE);
				st.setAutoReverse(true);
				st.play();
			});
			setOnMouseExited(event -> {
				st.stop();
				bg.setFill(Color.WHITE);
			});
		}
	}

	private static class menuGroup extends VBox {
		public menuGroup(menuButton @NotNull ...items){
			setSpacing(15);
			setTranslateY(100);
			setTranslateX(50);
			for(menuButton item : items){
				getChildren().addAll(item);
			}
		}
	}

	private static class MenuBox extends Pane{
		static menuGroup menuGroup;
		public MenuBox(menuGroup menuGroup){
			MenuBox.menuGroup = menuGroup;
			getChildren().addAll(menuGroup);
		}
	}

	public static void main(String[] args) {
//		Game game = new Game();
//		game.gameLoop();
		launch(args);
	}
}

