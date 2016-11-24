package com.picklegames.gameStates;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.GameStateManager;

// Miguel Garnica
// Nov 23, 2016
public abstract class GameState {
	
	protected GameStateManager gsm;
	protected EnglishGame game;
	
	protected SpriteBatch batch;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudCam;
	
	public GameState(GameStateManager gsm){
		this.gsm = gsm; 
		game = gsm.game();
		batch = game.getBatch();
		cam = game.getCam();
		hudCam = game.getHudCam();
		init();
		
	}
	
	public abstract void init();
	
	public abstract void handleInput();
	
	public abstract void update(float dt);
	
	public abstract void render();
	
	public abstract void dispose();

}
