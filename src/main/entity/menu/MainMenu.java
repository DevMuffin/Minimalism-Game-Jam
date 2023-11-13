package main.entity.menu;

import java.awt.Color;
import java.awt.Font;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.Background.SolidColorBackground;
import com.anthonybhasin.nohp.level.Level;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.texture.Sprite;
import com.anthonybhasin.nohp.ui.Button;
import com.anthonybhasin.nohp.ui.UIElementBackground.BasicUIBackground;
import com.anthonybhasin.nohp.util.UpDownInt;

import main.Main;
import main.sounds.Sounds;

public class MainMenu extends Level {

	public static final Font // MENU_TITLE_FONT = new Font("Verdana", 0, 50),
	MENU_OPTIONS_FONT = new Font("Verdana", 0, 25);

	private static final Sprite TITLE_SPRITE = new Sprite("title.png");

	private UpDownInt playerCloneUDI;

	public MainMenu(Camera camera) {

		super(camera, new SolidColorBackground(Color.DARK_GRAY));

		super.addEntity(new Button(GameSettings.width / 2, GameSettings.height / 2 + 40, 250, 50,
				new BasicUIBackground(Color.DARK_GRAY).withText("Click to Play", Color.CYAN,
						MainMenu.MENU_OPTIONS_FONT)) {
			@Override
			public void onLeftClick() {

				Main.startGameInstance();
				Sounds.play(Sounds.MENU_SOUND);
			}
		});
		super.addEntity(new Button(GameSettings.width / 2, GameSettings.height / 2 + 100, 250, 50,
				new BasicUIBackground(Color.DARK_GRAY).withText("Instructions", Color.PINK,
						MainMenu.MENU_OPTIONS_FONT)) {
			@Override
			public void onLeftClick() {

				Game.instance.level = new InstructionsMenu(camera);
				Sounds.play(Sounds.MENU_SOUND);
			}
		});

		this.playerCloneUDI = new UpDownInt(1, -100, 100);
	}

	@Override
	public void tick() {

		super.tick();

		this.playerCloneUDI.tick();
	}

	@Override
	public void render() {

		super.render();
//
//		float textWidth = Screen.getStringWidth(Main.title, MainMenu.MENU_TITLE_FONT);
//		Screen.text(Main.title).start(GameSettings.width / 2 - textWidth / 2, GameSettings.height / 2 - 50)
//				.font(MainMenu.MENU_TITLE_FONT).color(Color.WHITE).draw();

		Screen.sprite(MainMenu.TITLE_SPRITE)
				.start(GameSettings.width / 2 - MainMenu.TITLE_SPRITE.getWidth() / 2, GameSettings.height / 2 - 110)
				.draw();

		int value = this.playerCloneUDI.get();
		Screen.rect().start(50, GameSettings.height / 2 + value).dimensions(50, 50).color(Color.CYAN).fill().draw();
		Screen.rect().start(GameSettings.width - 100, GameSettings.height / 2 + value).dimensions(50, 50)
				.color(Color.PINK).fill().draw();
	}
}
