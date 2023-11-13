package main.entity;

import java.awt.Color;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.Screen;
import com.anthonybhasin.nohp.level.entity.Camera;
import com.anthonybhasin.nohp.level.entity.Entity;

public class Enemy {//extends Entity {

//	private int direction = 0;
//
//	public Enemy() {
//
//		super.persistent = true;
//		super.position.set(0, (float) (Math.random() * GameSettings.height - GameSettings.height / 2));
//
//		if (Math.random() < 0.5) {
//			this.direction = -1;
//		} else
//			this.direction = 1;
//	}
//
//	@Override
//	public void tick() {
//
//		super.position.x += this.direction * 2;
//
//		if (this.direction < 0) {
//
//			if (super.position.x < -GameSettings.width / 2) {
//				this.remove();
//			}
//		} else {
//			if (super.position.x > GameSettings.width / 2) {
//				this.remove();
//			}
//		}
//	}
//
//	@Override
//	public void render() {
//
//		Camera camera = Game.instance.level.camera;
//
//		Screen.rect().start(super.position.x - 2 - camera.getMinX(), super.position.y - 2 - camera.getMinY())
//				.dimensions(4, 4).fill().color(Color.WHITE).draw();
//	}
//
//	public void remove() {
//
//		Game.instance.level.removeEntity(this);
//
//		Player.score++;
//	}
}
