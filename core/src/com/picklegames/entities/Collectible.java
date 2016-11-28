package com.picklegames.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.picklegames.game.EnglishGame;

// Miguel Garnica
// Nov 26, 2016
public class Collectible extends Entity {

	private TextureRegion[] texR;
	private Texture tex;

	public Collectible() {
		super();
		init();
	}

	public Collectible(Body body) {
		super(body);
		init();
	}

	public void init() {

		EnglishGame.res.loadTexture("images/coins.png", "coins");
		tex = EnglishGame.res.getTexture("coins");

		texR = TextureRegion.split(tex, 32, 32)[0];
		animation.setFrames(texR);

		width = animation.getFrame().getRegionWidth();
		height = animation.getFrame().getRegionWidth();
	}

	@Override
	public void update(float dt) {
		super.update(dt);
	}

	@Override
	public void render(SpriteBatch batch) {

		batch.draw(animation.getFrame(), getWorldPosition().x - width / 2, getWorldPosition().y - height / 2, width,
				height);
	}

}
