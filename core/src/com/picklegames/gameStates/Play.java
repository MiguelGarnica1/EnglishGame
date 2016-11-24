package com.picklegames.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.picklegames.entities.Player;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.handlers.GameStateManager;

// Miguel Garnica
// Nov 23, 2016
public class Play extends GameState {

	private Player player;

	public Play(GameStateManager gsm) {
		super(gsm);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		player = new Player();
		createPlayerBox2D();

		player.setVelocity(0, 0);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
		if(Gdx.input.isKeyPressed(Keys.A)){
			player.setVelocityX(-8);
		}else if(Gdx.input.isKeyPressed(Keys.D)){
			player.setVelocityX(8);
		}else{
			player.setVelocityX(0);
		}

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		handleInput();
		
		player.update(dt);
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		player.render(batch);
	}

	public void createPlayerBox2D() {
		player.setBody(CreateBox2D.createBox(EnglishGame.getWorld(), 100, 100, player.getWidth(), player.getHeight(),
				new Vector2(player.getWidth() / 2, player.getHeight() / 2), BodyType.DynamicBody, B2DVars.BIT_PLAYER,
				B2DVars.BIT_WALL, "player"));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		player.dispose();

	}

}
