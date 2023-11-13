package main.entity.menu;

import java.awt.Color;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.Background.SolidColorBackground;
import com.anthonybhasin.nohp.level.Level;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.ui.Button;
import com.anthonybhasin.nohp.ui.UIElementBackground.BasicUIBackground;

import main.sounds.Sounds;

public class OverMenu extends Level {

	private boolean playerWon;

	public OverMenu(Camera camera, boolean playerWon) {

		super(camera, new SolidColorBackground(Color.DARK_GRAY));

		super.addEntity(new Button(GameSettings.width / 2, GameSettings.height - 40, 250, 50,
				new BasicUIBackground(Color.DARK_GRAY).withText("Return to Main Menu",
						playerWon ? Color.PINK : Color.CYAN, MainMenu.MENU_OPTIONS_FONT)) {
			@Override
			public void onLeftClick() {

				Game.instance.level = new MainMenu(Game.instance.level.camera);
				Sounds.play(Sounds.MENU_SOUND);
			}
		});

		this.playerWon = playerWon;

		Sounds.BATTLE_MUSIC.stop();
	}

	@Override
	public void render() {

		super.render();

		String text = this.playerWon ? "You won!" : "Game Over";
		float textWidth = Screen.getStringWidth(text, MainMenu.MENU_OPTIONS_FONT);
		Screen.text(text).start(GameSettings.width / 2 - textWidth / 2, 100).font(MainMenu.MENU_OPTIONS_FONT)
				.color(this.playerWon ? Color.CYAN : Color.PINK).draw();

		int size = 125, halfSize = size / 2;

		Screen.rect().start(GameSettings.width / 2 - halfSize, GameSettings.height / 2 - halfSize)
				.dimensions(size, size).color(playerWon ? Color.CYAN : Color.PINK).fill().draw();
	}
}
