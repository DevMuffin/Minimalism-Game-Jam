package main.entity;

import java.awt.Color;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.level.entity.Entity;

public class Floor extends Entity {

	private int width;

	public Floor(float xc, float yc, int width) {

		super.persistent = true;
		super.position.set(xc, yc);

		this.width = width;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render() {

		Camera camera = Game.instance.level.camera;

		Screen.rect().start(super.position.x - this.width / 2 - camera.getMinX(), super.position.y - camera.getMinY())
				.dimensions(this.width, 1).color(Color.WHITE).draw();
	}

	@Override
	public float getMinX() {

		return super.position.x - this.width / 2;
	}

	@Override
	public float getMaxX() {

		return super.position.x + this.width / 2;
	}
}
