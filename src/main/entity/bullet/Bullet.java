package main.entity.bullet;

import java.awt.Color;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.level.entity.Entity;
import com.anthonybhasin.nohp.math.Vector2D;

import main.Main;
import main.entity.ActionableEntity;
import main.entity.Player;
import main.sounds.Sounds;

public class Bullet extends Entity {

	private static final int SIZE = 8, SPEED = 5;

	private ActionableEntity sender;

	private int dirX, bounces;

	private float dirY;

	public Bullet(ActionableEntity sender, int dirX, float dirY) {

		super.persistent = true;

		this.sender = sender;

		this.dirX = dirX;

		this.dirY = dirY;

		this.bounces = 0;
	}

	@Override
	public void tick() {

		super.position.move(new Vector2D(this.dirX * Bullet.SPEED, this.dirY * Bullet.SPEED));

		for (ActionableEntity entity : Main.HITTABLES) {

			if (entity.equals(this.sender)) {

				continue;
			}

			if (this.getMaxX() > entity.getMinX() && this.getMinX() < entity.getMaxX()) {
				if (this.getMaxY() > entity.getMinY() && this.getMinY() < entity.getMaxY()) {

//					hit
					entity.damage(20);
					
					Game.instance.level.removeEntity(this);
					return;
				}
			}
		}

		float entityMinX = bounds.getMinX(), entityMinY = bounds.getMinY(), entityMaxX = bounds.getMaxX(),
				entityMaxY = bounds.getMaxY();

		if (entityMinX < 0 && this.dirX < 0) {

			this.dirX = Math.abs(this.dirX);
			this.bounces++;
			Sounds.play(Sounds.BOUNCE, 0.05);
		}
		if (entityMaxX > GameSettings.width && this.dirX > 0) {

			this.dirX = -Math.abs(this.dirX);
			this.bounces++;
			Sounds.play(Sounds.BOUNCE, 0.05);
		}
		if (entityMinY < 0 && this.dirY < 0) {

			this.dirY = Math.abs(this.dirY);
			this.bounces++;
			Sounds.play(Sounds.BOUNCE, 0.05);
		}
		if (entityMaxY > GameSettings.height && this.dirY > 0) {

			this.dirY = -Math.abs(this.dirY);
			this.bounces++;
			Sounds.play(Sounds.BOUNCE, 0.05);
		}

		if (this.bounces >= 3) {

			Game.instance.level.removeEntity(this);
		}

//		if (!Game.instance.level.camera.canSee(this)) {
//
//			Game.instance.level.removeEntity(this);
//		}
	}

	@Override
	public void render() {

		Camera camera = Game.instance.level.camera;

		Screen.rect().start(this.getMinX() - camera.getMinX(), this.getMinY() - camera.getMinY())
				.dimensions(Bullet.SIZE, Bullet.SIZE).fill()
				.color(this.sender instanceof Player ? Color.CYAN : Color.PINK).draw();
	}

	@Override
	public float getMinX() {

		return super.position.x - Bullet.SIZE / 2;
	}

	@Override
	public float getMaxX() {

		return super.position.x + Bullet.SIZE / 2;
	}

	@Override
	public float getMinY() {

		return super.position.y - Bullet.SIZE / 2;
	}

	@Override
	public float getMaxY() {

		return super.position.y + Bullet.SIZE / 2;
	}
}
