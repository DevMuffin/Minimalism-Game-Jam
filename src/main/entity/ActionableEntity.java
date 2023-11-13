package main.entity;

import java.awt.Color;
import java.awt.Rectangle;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.level.entity.Entity;
import com.anthonybhasin.nohp.level.entity.PostRenderable;
import com.anthonybhasin.nohp.math.Vector2D;
import com.anthonybhasin.nohp.util.Timer;
import com.anthonybhasin.nohp.util.UpDownInt;

import main.Main;
import main.entity.bullet.Bullet;
import main.entity.menu.OverMenu;
import main.entity.particles.BeamParticle;
import main.sounds.Sounds;

public abstract class ActionableEntity extends Entity implements PostRenderable {

	protected static final int JUMP_TIME = 28, MAX_HEALTH = 200;

	protected int size;

	protected boolean grounded, performedJumpDamageAlready;
	protected int health, lastXDir, jumpTimer, beamChargeSize, beamSquareRotation, deathTimer;

	protected Timer bulletTimer, beamTimer, damageTimer;

//	TODO - have the beam wiggle up and down as well?
	protected Rectangle beamRectangle;

	protected UpDownInt beamHeightUDI;

	public ActionableEntity(int size) {

		this.size = size;

		this.grounded = false;
		this.performedJumpDamageAlready = false;

		this.health = ActionableEntity.MAX_HEALTH;
		this.lastXDir = 1;
		this.jumpTimer = 0;
		this.beamChargeSize = 0;
		this.beamSquareRotation = 0;
		this.deathTimer = 0;

		this.bulletTimer = new Timer();
		this.beamTimer = new Timer();
		this.damageTimer = new Timer();

		this.beamRectangle = new Rectangle();

		this.beamHeightUDI = new UpDownInt(2, 0, 15);
	}

	@Override
	public void tick() {

		if (this.health < 0) {

			this.deathTimer++;

			if (this.deathTimer % 2 == 0) {

				this.size--;
			}

			if (this.size <= 0) {

				Game.instance.level = new OverMenu(Game.instance.level.camera, !this.equals(Main.player));
				return;
			}
		}

		this.bulletTimer.tick();
		this.beamTimer.tick();
		this.damageTimer.tick();

		if (!this.beamTimer.ready()) {

			int beamVal = this.beamTimer.getTime();

			if (beamVal >= 9 * GameSettings.maxTPS) {

//				Charge up
				this.beamChargeSize++;

				this.beamSquareRotation += 2;
			} else if (beamVal >= 8 * GameSettings.maxTPS) {

//				Execute beam for two seconds
				this.beamSquareRotation = 0;
				this.beamChargeSize = 0;
				this.beamHeightUDI.tick();

				for (ActionableEntity entity : Main.HITTABLES) {

					if (entity.equals(this)) {

						continue;
					}

					if ((float) this.beamRectangle.getMaxX() > entity.getMinX()
							&& (float) this.beamRectangle.getMinX() < entity.getMaxX()) {
						if ((float) this.beamRectangle.getMaxY() > entity.getMinY()
								&& (float) this.beamRectangle.getMinY() < entity.getMaxY()) {

//							Beam hit

//							Particles
							if (beamVal % 3 == 0) {

								float particleXDir = (float) (Math.random() * 2 - 1),
										particleYDir = (float) (Math.random() * 2 - 1);

								if (Math.abs(particleXDir) < 0.1f && Math.abs(particleYDir) < 0.1f) {

									particleXDir = 0.5f;
								}

								Game.instance.level.addEntity(new BeamParticle(
										entity.getMidX() + (float) (Math.random() * entity.size - entity.size / 2),
										entity.getMidY() + (float) (Math.random() * entity.size - entity.size / 2),
										(float) (Math.random() * 5 + 5),
										new Vector2D(particleXDir, particleYDir).normalize(),
										entity.equals(Main.player) ? Color.CYAN : Color.PINK));
							}

//							Damage
							if (entity.equals(Main.player)) {

								entity.damage(1);
							} else if (beamVal % 3 == 0) {

								entity.damage(1);
							}
						}
					}
				}
			}
		}

		if (!this.isBeamActive()) {

			this.grounded = false;

			for (Floor floor : Main.FLOORS) {

				if (this.getMaxX() >= floor.getMinX() && this.getMinX() <= floor.getMaxX()) {

					if (this.getMaxYi() == (int) floor.position.y) {

						this.grounded = true;
						break;
					}
				}
			}

			if (this.jumpTimer > 0) {

				int jumpSpeed = 0;
				if (this.jumpTimer > ActionableEntity.JUMP_TIME - 3) {
					jumpSpeed = 4;
				} else if (this.jumpTimer > ActionableEntity.JUMP_TIME - 6) {
					jumpSpeed = 5;
				} else if (this.jumpTimer > ActionableEntity.JUMP_TIME - 12) {
					jumpSpeed = 6;
				} else if (this.jumpTimer > ActionableEntity.JUMP_TIME - 18) {
					jumpSpeed = 5;
				} else if (this.jumpTimer > ActionableEntity.JUMP_TIME - 24) {
					jumpSpeed = 4;
				} else {
					jumpSpeed = 3;
				}

				// } else {

				// jumpSpeed = 0;
				// }
				// float multiplier = ((ActionableEntity.JUMP_TIME / 2f) -
				// Math.abs(ActionableEntity.JUMP_TIME / 2 - this.jumpTimer))
				// / (ActionableEntity.JUMP_TIME / 2f);

				float oldY = this.getMinY();

				super.position.y -= 5 + jumpSpeed;

				float newY = this.getMinY();

				if (!this.performedJumpDamageAlready) {

					for (ActionableEntity entity : Main.HITTABLES) {

						if (entity.equals(this)) {

							continue;
						}

//						Jumped through the other entity

						if (this.getMaxX() >= entity.getMinX() && this.getMinX() <= entity.getMaxX()
								&& oldY >= entity.getMaxY() && newY < entity.getMaxY()) {

							entity.damage(30);
							this.performedJumpDamageAlready = true;
						}
					}
				}

				this.jumpTimer--;
			}

			if (!this.grounded) {

				float newY = super.position.y + 5;

				for (Floor floor : Main.FLOORS) {

					if (super.position.x >= floor.getMinX() && super.position.x <= floor.getMaxX()) {

//						If old y was above the floor, and new y below the floor, cap off new y.
						if (this.getMaxY() < floor.position.y && (newY + this.size / 2) >= floor.position.y) {

							newY = floor.position.y - this.size / 2;
							break;
						}
					}
				}

				super.position.y = newY;
			}
		}
	}

	public void renderHealth(Color color) {

		Camera camera = Game.instance.level.camera;

		int healthDisplayWidth = 100;

		Screen.rect()
				.start(this.getMidX() - healthDisplayWidth / 2 - camera.getMinX(),
						this.getMinY() - 15 - camera.getMinY())
				.dimensions(healthDisplayWidth, 5).color(Color.GRAY).fill().draw();

		if (this.health > 0) {

			Screen.rect()
					.start(this.getMidX() - healthDisplayWidth / 2 - camera.getMinX(),
							this.getMinY() - 15 - camera.getMinY())
					.dimensions((int) ((this.health / (float) ActionableEntity.MAX_HEALTH) * healthDisplayWidth), 5)
					.color(color).fill().draw();
		}
	}

	public void render_(Color color) {

		if (!this.damageTimer.ready()) {

			if (this.damageTimer.getTime() % 20 > 10) {

				this.renderHealth(Color.RED);
				return;
			}
		}

		this.renderHealth(color);

		Camera camera = Game.instance.level.camera;

		if (this.beamSquareRotation > 0) {

			Screen.rect()
					.start(this.getMinX() - camera.getMinX() + this.beamChargeSize / 2,
							this.getMinY() - camera.getMinY() + this.beamChargeSize / 2)
					.dimensions(this.size - this.beamChargeSize, this.size - this.beamChargeSize)
					.selfRotation(this.beamSquareRotation).fill().color(color).draw();
		} else {

			Screen.rect().start(this.getMinX() - camera.getMinX(), this.getMinY() - camera.getMinY())
					.dimensions(this.size, this.size).fill().color(color).draw();
		}
	}

	@Override
	public void postRender() {

		Camera camera = Game.instance.level.camera;

		int beamVal = this.beamTimer.getTime();
		if (8 * GameSettings.maxTPS <= beamVal && beamVal < 9 * GameSettings.maxTPS) {

			Screen.rect()
					.start(this.beamRectangle.x - camera.getMinX(),
							this.beamRectangle.y - camera.getMinY() + this.beamHeightUDI.get() / 2)
					.dimensions(this.beamRectangle.width, this.beamRectangle.height - this.beamHeightUDI.get())
					.color(Color.RED).fill().draw();
		}
	}

	public void damage(int amount) {

		this.health -= amount;

		if (this.damageTimer.ready()) {

			if (this.equals(Main.player)) {

				Sounds.play(Sounds.ME_HURT, 0.5);
			} else {

				Sounds.play(Sounds.CLONE_ME_HURT, 0.5);
			}
		}

		this.damageTimer.set((int) (0.5 * GameSettings.maxTPS));
	}

	public void jump() {

		this.jumpTimer = ActionableEntity.JUMP_TIME;
		this.performedJumpDamageAlready = false;
	}

	public void shootBullet() {

		Bullet bulletU = new Bullet(this, this.lastXDir, -0.4f);
		bulletU.position.set(super.position.x, super.position.y - 50);
		Bullet bulletC = new Bullet(this, this.lastXDir, 0f);
		bulletC.position.set(super.position);
		Bullet bulletD = new Bullet(this, this.lastXDir, 0.4f);
		bulletD.position.set(super.position.x, super.position.y + 50);

		Game.instance.level.addEntity(bulletU);
		Game.instance.level.addEntity(bulletC);
		Game.instance.level.addEntity(bulletD);

		this.bulletTimer.set(3 * GameSettings.maxTPS);
	}

	public void startBeam() {

//		Use this.lastXDir
		this.beamTimer.set((int) (9.5 * GameSettings.maxTPS));
		this.beamHeightUDI.setValue(0, true);

		this.beamChargeSize = 0;
		this.beamSquareRotation = 0;

		Camera camera = Game.instance.level.camera;

		if (this.lastXDir < 0) {

//			this.beamRectangle = new Rectangle(super.getMidXi(), super.getMidYi(), GameSettings.width - super.getMidXi(), 10);
			this.beamRectangle = new Rectangle(camera.getMinXi(), super.getMinYi(),
					super.getMinXi() - camera.getMinXi(), super.getHeight());
		} else {

			this.beamRectangle = new Rectangle(super.getMaxXi(), super.getMinYi(),
					GameSettings.width - super.getMaxXi(), super.getHeight());
		}
	}

	@Override
	public float getMinX() {

		return super.position.x - this.size / 2;
	}

	@Override
	public float getMaxX() {

		return super.position.x + this.size / 2;
	}

	@Override
	public float getMinY() {

		return super.position.y - this.size / 2;
	}

	@Override
	public float getMaxY() {

		return super.position.y + this.size / 2;
	}

	public void setLastXDir(int xDir) {

//		Ignore value of 0, so it will be last direction that was moved.
		if (xDir == 0) {

			return;
		}

		this.lastXDir = xDir;
	}

	public boolean canJump() {

		return this.grounded && this.jumpTimer == 0;
	}

	public boolean canShootBullet() {

		return this.bulletTimer.ready();
	}

	public boolean canStartBeam() {

		return this.beamTimer.ready();
	}

	public boolean isBeamActive() {

		return this.beamTimer.getTime() >= 8 * GameSettings.maxTPS;
	}
}
