package main.entity.particles;

import java.awt.Color;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.level.entity.Entity;
import com.anthonybhasin.nohp.level.entity.PostRenderable;
import com.anthonybhasin.nohp.math.Vector2D;
import com.anthonybhasin.nohp.util.Timer;

public class BeamParticle extends Entity implements PostRenderable {

	private float size;

	private Vector2D dir;

	private Color color;

	private int rotation;

	private Timer life;

	public BeamParticle(float x, float y, float size, Vector2D dir, Color color) {

		super();

		super.position.set(x, y);

		this.size = size;

		this.dir = dir;

		this.color = color;

		this.rotation = 0;

		this.life = new Timer();
		this.life.set((int) (0.5 * GameSettings.maxTPS));
	}

	@Override
	public void tick() {

		this.rotation += 5;

		this.life.tick();

		if (this.life.ready()) {

			Game.instance.level.removeEntity(this);
			return;
		}

		super.position.move(this.dir);
	}

	@Override
	public void postRender() {

		Camera camera = Game.instance.level.camera;

		Screen.rect()
				.start(super.position.x - this.size / 2 - camera.getMinX(),
						super.position.y - this.size / 2 - camera.getMinY())
				.dimensions(Math.round(this.size), Math.round(this.size)).color(this.color).fill()
				.selfRotation(this.rotation % 359).draw();

		Screen.rect()
				.start(super.position.x - this.size / 2 - camera.getMinX(),
						super.position.y - this.size / 2 - camera.getMinY())
				.dimensions(Math.round(this.size), Math.round(this.size)).color(Color.BLACK)
				.selfRotation(this.rotation % 359).draw();
	}
}
