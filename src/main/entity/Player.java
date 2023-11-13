package main.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.io.Keyboard;
import com.anthonybhasin.nohp.util.UpDownInt;

import main.entity.action.Action;
import main.sounds.Sounds;

public class Player extends ActionableEntity {

	public static final Font COOLDOWN_FONT = new Font("Verdana", 0, 20);

	public static final List<Action> REPLAYS = new ArrayList<Action>();

//	public static int score = 0;

	private UpDownInt displaySizeUDI;

	public Player() {

		super(50);

		super.persistent = true;
		super.position.set(-GameSettings.width / 2 + 50, 0);

		this.displaySizeUDI = new UpDownInt(10, 0, 10);
	}

	@Override
	public void tick() {

		this.displaySizeUDI.tick();

		int moveXDir = 0;

		if (Keyboard.getPressed(KeyEvent.VK_A)) {

			moveXDir--;
		}
		if (Keyboard.getPressed(KeyEvent.VK_D)) {

			moveXDir++;
		}

		super.tick();

		boolean jump = super.grounded && (Keyboard.getPressed(KeyEvent.VK_SPACE) || Keyboard.getPressed(KeyEvent.VK_W));

		boolean bullet = Keyboard.getPressed(KeyEvent.VK_J);

		boolean beam = Keyboard.getPressed(KeyEvent.VK_K);

		Action action = new Action(this, moveXDir, jump, bullet, beam);

		action.run(this);

		Player.REPLAYS.add(action);

		if (super.beamTimer.getTime() == 9 * GameSettings.maxTPS) {

			Sounds.play(Sounds.LASER);
		}
	}

	@Override
	public void render() {

		super.render_(Color.CYAN);
	}

	private void displayCooldown(int timer, float x, char key) {

		int seconds = (int) Math.ceil(timer / (float) GameSettings.maxTPS);

		Screen.rect().start(x, GameSettings.height - 35).dimensions(35, 35)
				.color(seconds == 0 ? Color.CYAN : Color.WHITE).fill().draw();

		String text = seconds > 0 ? seconds + "s" : "" + key;
		float textWidth = Screen.getStringWidth(text, Player.COOLDOWN_FONT);
		Screen.text(text).start(x + 35 / 2 - textWidth / 2, GameSettings.height - 10).font(Player.COOLDOWN_FONT)
				.color(Color.BLACK).draw();
	}

	@Override
	public void postRender() {

		super.postRender();

		this.displayCooldown(super.bulletTimer.getTime(), GameSettings.width / 2 - 35 - 15, 'J');
		this.displayCooldown(super.beamTimer.getTime(), GameSettings.width / 2 + 15, 'K');
	}

//	@Override
//	public void jump() {
//
//		super.jump();
//
//		Sounds.PLAYER_JUMP.play();
//	}

	@Override
	public void shootBullet() {

		super.shootBullet();

		Sounds.play(Sounds.PLAYER_BULLETS);
	}
}
