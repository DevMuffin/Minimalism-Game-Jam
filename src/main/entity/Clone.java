package main.entity;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.util.Timer;

import main.entity.action.Action;
import main.sounds.Sounds;

public class Clone extends ActionableEntity {
//TODO - mirror the actions?
	// OR MAYBE one clone that follows you directly, and one that mirrors you
	// completely.

	private Timer savedBeamTimer, changeDelayTimer, changeDelayDisplayTimer;

	private int randomMoveDir, delay;

	private boolean startGameNoRandomMove, randomMoving;

	public Clone() {

		super(50);

		super.persistent = true;
		super.position.set(GameSettings.width / 2 - 50, 0);

//		This way even if the player doesn't move, the clone's last xDir will be a reflected version of the player's default xDir.
		super.lastXDir = 1;

		this.savedBeamTimer = new Timer();
		this.changeDelayTimer = new Timer();
		this.changeDelayDisplayTimer = new Timer();

		this.changeDelayTimer.set(10 * GameSettings.maxTPS);

		this.randomMoveDir = -1;

		this.setDelay(1 * GameSettings.maxTPS);

		this.startGameNoRandomMove = true;
		this.randomMoving = false;
	}

	private void setDelay(int delay) {

		if (delay < this.delay) {
			for (int i = 0; i < this.delay - delay; i++) {

				Player.REPLAYS.remove(0);
			}
		}

		this.delay = delay;
	}

	@Override
	public void tick() {

		super.tick();

		this.changeDelayTimer.tick();

		this.changeDelayDisplayTimer.tick();

		if (this.changeDelayTimer.ready()) {

//			Between [0.5, 5.5) seconds of delay
			this.setDelay((int) ((Math.random() * 5 + 0.5) * GameSettings.maxTPS));

			this.changeDelayTimer.set(30 * GameSettings.maxTPS);

			this.changeDelayDisplayTimer.set(GameSettings.maxTPS);

			this.startGameNoRandomMove = false;
		}

		this.savedBeamTimer.tick();

		this.randomMoving = false;

		if (Player.REPLAYS.size() >= this.delay) {

			Action action = Player.REPLAYS.remove(0);

//			TODO perhaps instead of this random action style of idea, make it so the clone's beam rotations/moves which the player's doesnt.
			/**
			 * if (action.beamWasActiveDuringCreation) {
			 * 
			 * if ((int) (Math.random() * 75) == 25) {
			 * 
			 * this.randomMoveDirDuringPlayerBeam *= -1; }
			 * 
			 * if ((int) (Math.random() * 150) == 50) {
			 * 
			 * action.jumped = true; }
			 * 
			 * if ((int) (Math.random() * 150) == 75) {
			 * 
			 * action.bullet = true; }
			 * 
			 * action.moveXDir = this.randomMoveDirDuringPlayerBeam; }
			 * 
			 * if (action.beam) {
			 * 
			 * // If the Clone beams exactly the delay after the player beams, it's very
			 * predictable since the player was still for the entire beam time and knows
			 * it's coming. // Instead, save the beam action and inject it later.
			 * 
			 * this.savedBeamTimer.set((int) ((Math.random() * 3 + 2) *
			 * GameSettings.maxTPS)); action.beam = false; }
			 * 
			 * boolean updateLastXDir = true;
			 * 
			 * if (this.savedBeamTimer.getTime() == 1) {
			 * 
			 * if (!super.beamTimer.ready()) {
			 * 
			 * this.savedBeamTimer.set(2); } else {
			 * 
			 * action.injectBeam(); super.setLastXDir((int)
			 * Math.signum(super.position.signedDistanceX(Main.player.position)));
			 * updateLastXDir = false; } }
			 * 
			 */
			boolean updateLastXDir = true;
//			==================================

			action.reverse();

			action.run(this, updateLastXDir);
		} else if (!this.startGameNoRandomMove) {

//			Here we move randomly until there's enough replays in the buffer for the amount of time that we want selected.
			Action fakeAction = new Action(this.randomMoveDir);

			if ((int) (Math.random() * 75) == 25) {

				this.randomMoveDir *= -1;
			}

			if ((int) (Math.random() * 150) == 50) {

				fakeAction.jumped = true;
			}

			if ((int) (Math.random() * 150) == 75) {

				fakeAction.bullet = true;
			}

			fakeAction.run(this);
			this.randomMoving = true;
		}

		if (super.beamTimer.getTime() == 9 * GameSettings.maxTPS) {

			Sounds.play(Sounds.LASER);
		}
	}

	@Override
	public void render() {

		super.render_(Color.PINK);
	}

	private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

	private static final Font DELAY_FONT = new Font("Verdana", 0, 30), DELAY_FONT_SMALL = new Font("Verdana", 0, 25);

	@Override
	public void postRender() {

		super.postRender();

		Camera camera = Game.instance.level.camera;

		if (!this.changeDelayDisplayTimer.ready()) {

			String delaySeconds = Clone.FORMAT.format(this.delay / (float) GameSettings.maxTPS) + "s";

			String textStr = "Delay Update: " + delaySeconds;
			float textWidth = Screen.getStringWidth(textStr, Clone.DELAY_FONT);
			Screen.text(textStr).start(0 - camera.getMinX() - textWidth / 2, -150 - camera.getMinY())
					.font(Clone.DELAY_FONT).color(Color.WHITE).draw();

			textStr = delaySeconds;
			textWidth = Screen.getStringWidth(textStr, Clone.DELAY_FONT_SMALL);

			Screen.text(textStr)
					.start(super.getMidX() - camera.getMinX() - textWidth / 2, super.getMinY() - 25 - camera.getMinY())
					.font(Clone.DELAY_FONT_SMALL).color(Color.WHITE).draw();
		}

		if (this.randomMoving) {

			String textStr = "Moving Randomly";

			float textWidth = Screen.getStringWidth(textStr, Clone.DELAY_FONT_SMALL);

			Screen.text(textStr)
					.start(super.getMidX() - camera.getMinX() - textWidth / 2, super.getMinY() - 50 - camera.getMinY())
					.font(Clone.DELAY_FONT_SMALL).color(Color.WHITE).draw();
		}
	}

//	@Override
//	public void jump() {
//
//		super.jump();
//
//		Sounds.CLONE_JUMP.play();
//	}

	@Override
	public void shootBullet() {

		super.shootBullet();

		Sounds.play(Sounds.CLONE_BULLETS);
	}
}
