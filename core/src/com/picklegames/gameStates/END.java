package com.picklegames.gameStates;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.Animation;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.GameStateManager;
import com.picklegames.handlers.MyTextInputListener;
import com.picklegames.handlers.Score;

// Miguel Garnica
// Nov 30, 2016
public class END extends GameState {

	private BitmapFont font;
	private Scanner kgb;
	private ArrayList<Score> scores;
	private GlyphLayout layout;
	private String currentScore;
	FileHandle file;
	MyTextInputListener listener;

	private Background bg;
	private Animation animation;
	private Animation ani1;
	private Animation ani2;
	private Animation ani3;
	private Animation ani4;

	private Texture tex;
	private TextureRegion[] texR;

	String name;

	public END(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {

		// file = Gdx.files.local("data/highscores.txt");
		//
		// listener = new MyTextInputListener();
		// Gdx.input.getTextInput(listener, "HighScore", "Name", "Name");
		// //listener.input("Jeff");
		// name = listener.getName();
		// file.writeString(listener.getName() + ":" + Play.score + ":" +
		// Play.burgersEat + ":" + Play.deaths, false);

		// kgb = new Scanner(file.readString());
		// scores = new ArrayList<Score>();
		// while (kgb.hasNextLine()) {
		// String s = kgb.nextLine();
		// scores.add(new Score(s));
		// }
		font = new BitmapFont(Gdx.files.internal("fonts/comicsan.fnt"));
		font.setColor(Color.GOLD);

		layout = new GlyphLayout();

		EnglishGame.res.loadTexture("images/menu.png", "menu");
		tex = EnglishGame.res.getTexture("menu");
		bg = new Background(new TextureRegion(tex), cam, 1f);
		bg.setVector(-450, 0);

		EnglishGame.res.loadTexture("images/player.png", "player");
		tex = EnglishGame.res.getTexture("player");
		TextureRegion[] reg = new TextureRegion[4];
		for (int i = 0; i < reg.length; i++) {
			reg[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
		}
		animation = new Animation(reg, 1 / 12f);

		EnglishGame.res.loadTexture("images/veg1.png", "veg1");
		tex = EnglishGame.res.getTexture("veg1");
		texR = TextureRegion.split(tex, 64, 64)[0];
		ani1 = new Animation(texR, 1 / 12f);

		EnglishGame.res.loadTexture("images/veg2.png", "veg2");
		tex = EnglishGame.res.getTexture("veg2");
		texR = TextureRegion.split(tex, 64, 64)[0];
		ani2 = new Animation(texR, 1 / 12f);

		EnglishGame.res.loadTexture("images/veg3.png", "veg3");
		tex = EnglishGame.res.getTexture("veg3");
		texR = TextureRegion.split(tex, 64, 64)[0];
		ani3 = new Animation(texR, 1 / 12f);

		EnglishGame.res.loadTexture("images/burger.png", "burger");
		tex = EnglishGame.res.getTexture("burger");
		texR = TextureRegion.split(tex, 32, 32)[0];
		ani4 = new Animation(texR, 1 / 12f);

	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
		bg.update(dt);
		animation.update(dt);
		ani1.update(dt);
		ani2.update(dt);
		ani3.update(dt);
		ani4.update(dt);
	}

	@Override
	public void render() {
		bg.render(batch);
		batch.setProjectionMatrix(hudCam.combined);
		batch.draw(animation.getFrame(), hudCam.viewportWidth / 2, 75, 150, 150);
		batch.draw(ani1.getFrame(), 25, 85, 100, 100);
		batch.draw(ani2.getFrame(), 150, 80, 100, 100);
		batch.draw(ani3.getFrame(), 250, 85, 100, 100);
		batch.draw(ani4.getFrame(), hudCam.viewportWidth - 150, 100, 50, 50);
		batch.setProjectionMatrix(hudCam.combined);

		layout.setText(font, "SCORE   BURGERS   DEATHS");
		font.draw(batch, "SCORE   BURGERS   DEATHS", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight - 75);

		layout.setText(font, (Play.score) + "             " + Play.burgersEat + "            " + Play.deaths);
		font.draw(batch, (Play.score) + "             " + Play.burgersEat + "            " + Play.deaths,
				hudCam.viewportWidth / 2 - layout.width / 2, hudCam.viewportHeight - 150);

		layout.setText(font, "TOTAL TIME PLAYED");
		font.draw(batch, "TOTAL TIME PLAYED", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight / 2 + 80);

		layout.setText(font, Play.timePlayed / 60 + "minutes");
		font.draw(batch, Play.timePlayed / 60 + "minutes", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight / 2);

		// if (listener.getName() != null) {
		// font.draw(batch, file.readString(), hudCam.viewportWidth / 2 -
		// layout.width / 2,
		// hudCam.viewportHeight - 50);
		// }

		// for (int i = 0; i < scores.size(); i++) {
		// currentScore = name + " " + scores.get(i).getPoints() + " " +
		// scores.get(i).getBurgers()
		// + " " + scores.get(i).getDeaths();
		// layout.setText(font, currentScore);
		// font.draw(batch, currentScore, hudCam.viewportWidth / 2 -
		// layout.width / 2,
		// hudCam.viewportHeight - (layout.height + 10));
		// }

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
