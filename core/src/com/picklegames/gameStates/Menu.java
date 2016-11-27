package com.picklegames.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.Animation;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.GameButton;
import com.picklegames.handlers.GameStateManager;

// Miguel Garnica
// Nov 23, 2016
public class Menu extends GameState {

	private Background bg;
	private Animation animation;
	private GameButton playButton;

	private Texture tex;

	public Menu(GameStateManager gsm) {
		super(gsm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
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

		EnglishGame.res.loadTexture("images/button.png", "button");
		tex = EnglishGame.res.getTexture("button");
		playButton = new GameButton(new TextureRegion(tex, 64, 32), 600, 300, 200, 100, cam);
		playButton.setText("PLAY");

		cam.setToOrtho(false, EnglishGame.V_WIDTH, EnglishGame.V_HEIGHT);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

		if (playButton.isClicked()) {
			gsm.setState(GameStateManager.PLAY);
		}

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub

		handleInput();

		bg.update(dt);
		animation.update(dt);
		playButton.update(dt);

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		
		bg.render(batch);
		playButton.render(batch);
		batch.draw(animation.getFrame(), 175, 75, 150,150);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
