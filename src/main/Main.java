package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.anthonybhasin.nohp.Game;
import com.anthonybhasin.nohp.GameSettings;
import com.anthonybhasin.nohp.level.Background.SolidColorBackground;
import com.anthonybhasin.nohp.level.Level;
import com.anthonybhasin.nohp.level.entity.Camera;

import main.entity.ActionableEntity;
import main.entity.Clone;
import main.entity.Floor;
import main.entity.Player;
import main.entity.menu.MainMenu;
import main.sounds.Sounds;

public class Main {

	public static final String title = "REFLECTION";

	public static final List<Floor> FLOORS = new ArrayList<Floor>();

	public static final List<ActionableEntity> HITTABLES = new ArrayList<ActionableEntity>();

	public static Player player;

	public static void main(String[] args) {

		GameSettings.windowTitle = Main.title + " - r/SoloDevelopment Game Jam #1";

		GameSettings.width = 900;

		GameSettings.maxTPS = 100;

		Game.create();

		Game.instance.level = new MainMenu(new Camera());

		Game.instance.start();
	}

	public static void startGameInstance() {

		Sounds.play(Sounds.START_GAME);

		Game.instance.level = new Level(new Camera(), new SolidColorBackground(Color.DARK_GRAY));

		Main.FLOORS.clear();
		Main.HITTABLES.clear();

		Main.player = new Player();
		Main.HITTABLES.add(Main.player);
		Game.instance.level.addEntity(Main.player);
		Clone clone = new Clone();
		Main.HITTABLES.add(clone);
		Game.instance.level.addEntity(clone);
		Floor mainFloor = new Floor(0, GameSettings.height / 2 - 50, 900);
		Main.FLOORS.add(mainFloor);
		Game.instance.level.addEntity(mainFloor);

		Floor leftFloor = new Floor(-GameSettings.width / 2 + 150, GameSettings.height / 2 - 150, 200);
		Main.FLOORS.add(leftFloor);
		Game.instance.level.addEntity(leftFloor);

		Floor rightFloor = new Floor(GameSettings.width / 2 - 150, GameSettings.height / 2 - 150, 200);
		Main.FLOORS.add(rightFloor);
		Game.instance.level.addEntity(rightFloor);

		Floor centerFloor = new Floor(0, -50, 400);
		Main.FLOORS.add(centerFloor);
		Game.instance.level.addEntity(centerFloor);

		Floor centerFloorLower = new Floor(0, GameSettings.height / 2 - 150, 200);
		Main.FLOORS.add(centerFloorLower);
		Game.instance.level.addEntity(centerFloorLower);

		Sounds.BATTLE_MUSIC.setLoopPositionBySeconds(0);
		Sounds.BATTLE_MUSIC.rewindToLoopPosition();
		Sounds.BATTLE_MUSIC.play(true, 0.3);
	}
}
