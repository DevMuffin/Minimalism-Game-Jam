package main.entity.action;

import com.anthonybhasin.nohp.GameSettings;

import main.entity.ActionableEntity;

public class Action {

	private static final int SPEED = 5;

	public int moveXDir;

	public boolean jumped, bullet, beam, beamWasActiveDuringCreation;

	public Action(int moveXDir) {
		this.moveXDir = moveXDir;

		this.jumped = false;
		this.bullet = false;
		this.beam = false;

		this.beamWasActiveDuringCreation = false;
	}

	public Action(ActionableEntity entity, int moveXDir, boolean jumped, boolean bullet, boolean beam) {

		this.moveXDir = moveXDir;

		this.jumped = jumped;

		this.bullet = bullet;

		this.beam = beam;

		this.beamWasActiveDuringCreation = entity.isBeamActive();
	}

	public void reverse() {

		this.moveXDir = -this.moveXDir;
	}

	public void injectBeam() {

		this.bullet = false;
		this.beam = true;
	}

	public void run(ActionableEntity entity, boolean updateLastXDir) {

		if (entity.isBeamActive()) {

			this.moveXDir = 0;

			this.jumped = false;

			this.bullet = false;
		}

		if (updateLastXDir) {

			entity.setLastXDir(this.moveXDir);
		}

		entity.position.x += this.moveXDir * Action.SPEED;
		if (entity.position.x < -GameSettings.width / 2) {
			entity.position.x = -GameSettings.width / 2;
		}
		if (entity.position.x >= GameSettings.width / 2) {
			entity.position.x = GameSettings.width / 2;
		}

		if (this.jumped) {

			if (entity.canJump()) {
				entity.jump();
			}
		}

		if (this.bullet) {

			if (entity.canShootBullet()) {

				entity.shootBullet();
			}
		}

		if (this.beam) {

			if (entity.canStartBeam()) {

				entity.startBeam();
			}
		}
	}

	public void run(ActionableEntity entity) {
		this.run(entity, true);
	}
}
