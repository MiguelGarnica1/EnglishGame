package com.picklegames.gameStates;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
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
import com.picklegames.handlers.GameButton;
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

	private Texture tex;
	private Texture burger;

	public END(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {

		// file = Gdx.files.local("data/highscores.txt");
		//
		// listener = new MyTextInputListener();
		// Gdx.input.getTextInput(listener, "HighScore", "Name", "Name");
		// listener.input("Jeff");
		// file.writeString(listener.getName() + ":" + Play.score + ":" +
		// Play.burgersEat + ":" + Play.deaths, false);
		//
		//
		//
		// kgb = new Scanner(file.readString());
		font = new BitmapFont(Gdx.files.internal("fonts/comicsan.fnt"));
		font.setColor(Color.GOLD);
		// scores = new ArrayList<Score>();
		layout = new GlyphLayout();
		//
		// while (kgb.hasNextLine()) {
		// String s = kgb.nextLine();
		// scores.add(new Score(s));
		// }
		
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
		
	//	burger = EnglishGame.res.getTexture("burger");

	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
//		bg.update(dt);
//		animation.update(dt);
	}

	@Override
	public void render() {
		bg.render(batch);
		batch.draw(animation.getFrame(), 175, 75, 150,150);
		batch.setProjectionMatrix(hudCam.combined);
		
		
		layout.setText(font, "SCORE   BURGERS   DEATHS");
		font.draw(batch, "SCORE   BURGERS   DEATHS", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight / 2 + 75);
		
		
		layout.setText(font, Play.score + "             " + Play.burgersEat + "            " + Play.deaths);
		font.draw(batch, Play.score + "             " + Play.burgersEat + "            " + Play.deaths,
				hudCam.viewportWidth / 2 - layout.width / 2, hudCam.viewportHeight / 2);
		
		layout.setText(font,"TOTAL TIME PLAYED");
		font.draw(batch, "TOTAL TIME PLAYED", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight / 2 - 80);
		
		layout.setText(font,Play.timePlayed / 60 + "minutes");
		font.draw(batch, Play.timePlayed / 60 + "minutes", hudCam.viewportWidth / 2 - layout.width / 2,
				hudCam.viewportHeight / 2 - 150);

		// if(listener.getName() !=null){
		// font.draw(batch, file.readString(), hudCam.viewportWidth / 2 -
		// layout.width/2, hudCam.viewportHeight - 50);
		// }

		// for (int i = 0; i < scores.size(); i++) {
		// currentScore = scores.get(i).getName() + " " +
		// scores.get(i).getPoints() + " "
		// + scores.get(i).getBurgers() + " " + scores.get(i).getDeaths();
		// layout.setText(font, currentScore);
		// font.draw(batch, currentScore, hudCam.viewportWidth/2 -
		// layout.width/2, hudCam.viewportHeight - (layout.height + 10));
		// }

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
