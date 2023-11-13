package main.sounds;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class Sounds {

	static {

		TinySound.init();
	}

	public static final Sound MENU_SOUND = TinySound.loadSound("sounds/menu_sound.wav"),
			START_GAME = TinySound.loadSound("sounds/start_game2.wav"),
//			PLAYER_JUMP = TinySound.loadSound("sounds/jump4.wav"), CLONE_JUMP = TinySound.loadSound("sounds/Jump.wav"),
			PLAYER_BULLETS = TinySound.loadSound("sounds/bullets_player.wav"),
			CLONE_BULLETS = TinySound.loadSound("sounds/bullets_clone.wav"),
			BOUNCE = TinySound.loadSound("sounds/ball_bounce2.wav"), LASER = TinySound.loadSound("sounds/laser2.wav"),
			DAMAGE = TinySound.loadSound("sounds/damage2.wav"), ME_HURT = TinySound.loadSound("sounds/me_hurt.wav"),
			CLONE_ME_HURT = TinySound.loadSound("sounds/clone_me_hurt.wav");

	public static final Music BATTLE_MUSIC = TinySound.loadMusic("music/battle.wav");

	public static void play(Sound sound, double vol) {

		sound.play(vol);
	}

	public static void play(Sound sound) {

		Sounds.play(sound, 0.2);
	}
}
