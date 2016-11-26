package com.picklegames.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.B2DVars;

// Miguel Garnica
// Nov 23, 2016
public class Player extends Entity {

	private TextureRegion[] texR;
	private Texture tex;

	public Player() {
		init();
	}

	public Player(Body body) {
		super(body);
		init();
	}

	public void init() {

		EnglishGame.res.loadTexture("images/player.png", "player");
		tex = EnglishGame.res.getTexture("player");

		texR = TextureRegion.split(tex, 32, 32)[0];
		animation.setFrames(texR, 1/8f);

		width = animation.getFrame().getRegionWidth();
		height = animation.getFrame().getRegionHeight();

	}

	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(body.getLinearVelocity().x == 0){
			animation.setCurrentFrame(4);
		}
		
		//body.setLinearVelocity(velocity);
	}

	@Override
	public void render(SpriteBatch batch) {
		
		batch.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - width / 2,
				body.getPosition().y * B2DVars.PPM - height / 2, width *2, height *2);
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
