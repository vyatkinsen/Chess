package main.java.sample;

import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;

class GameMenuTest extends TestFXBase {
	final String PLAY_BUTTON = "НОВАЯ ИГРА";
	final String EXIT_BUTTON = "ВЫХОД";


	@Test
	public void playAndExitButtonsTest() {
		Assertions.assertThat(GameMenu.newGame).hasText(PLAY_BUTTON);
		Assertions.assertThat(GameMenu.exitGame).hasText(EXIT_BUTTON);
		clickOn(PLAY_BUTTON);
	}
}