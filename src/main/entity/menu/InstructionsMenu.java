package main.entity.menu;

import java.awt.Color;
import java.awt.Font;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.Background.SolidColorBackground;
import com.anthonybhasin.nohp.level.Level;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.ui.Button;
import com.anthonybhasin.nohp.ui.UIElementBackground.BasicUIBackground;

import main.sounds.Sounds;

public class InstructionsMenu extends Level {

	private static final Font INSTRUCTIONS_FONT = new Font("Verdana", 0, 20);

	public InstructionsMenu(Camera camera) {

		super(camera, new SolidColorBackground(Color.DARK_GRAY));

		super.addEntity(new Button(GameSettings.width / 2, GameSettings.height - 40, 250, 50,
				new BasicUIBackground(Color.DARK_GRAY).withText("Return to Main Menu", Color.PINK,
						MainMenu.MENU_OPTIONS_FONT)) {
			@Override
			public void onLeftClick() {

				Game.instance.level = new MainMenu(Game.instance.level.camera);
				Sounds.play(Sounds.MENU_SOUND);
			}
		});
	}

	private void write(String text, int line, Color color) {

		float textWidth = Screen.getStringWidth(text, InstructionsMenu.INSTRUCTIONS_FONT);

		Screen.text(text).start(GameSettings.width / 2 - textWidth / 2, (line + 1) * 20)
				.font(InstructionsMenu.INSTRUCTIONS_FONT).color(color).draw();

	}

	private void write(String text, int line) {

		this.write(text, line, Color.WHITE);
	}

	@Override
	public void render() {

		super.render();

		this.write("Instructions:", 2, Color.CYAN);
		this.write("You will be fighting a clone of yourself that is stuck in the past", 3);
		this.write("Use this knowledge to your advantage, and you will rise victorious", 4);
		this.write("Movement Controls:", 6, Color.PINK);
		this.write("Move Left/Right: A/D", 7);
		this.write("Jump: W or Spacebar", 8);
		this.write("Attack Controls:", 10, Color.CYAN);
		this.write("Jumping is also an attack", 11);
		this.write("Simply jump through the enemy", 12);
		this.write("(from underneath them, to up above them)", 13);
		this.write("Shoot Bullets: J", 14);
		this.write("Fire Laser: K", 15);
		this.write("Made in 72h for r/SoloDevelopment Game Jam #1", 17);
	}
}
