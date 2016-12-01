package com.picklegames.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.picklegames.game.EnglishGame;

// Miguel Garnica
// Nov 28, 2016
public class BadGuy extends Entity{
	
	private TextureRegion[] texR;
	private Texture tex;
	private boolean isDead = false;
	private int id;
	Random r;
	float speed = -1;
	
	public BadGuy(){
		super();
		init();
	}
	
	public BadGuy(Body body){
		super(body);
		init();
	}
	
	public void init(){
		
		r = new Random();
		id = r.nextInt(3)+1;
		EnglishGame.res.loadTexture("images/veg" + id + ".png", "veg" + id);
		tex = EnglishGame.res.getTexture("veg" + id);
		
		texR = TextureRegion.split(tex, 64, 64)[0];
		animation.setFrames(texR);
		
		width = animation.getFrame().getRegionWidth();
		height = animation.getFrame().getRegionHeight();
		
		
	}
	
	float timeElapsed =0;
	@Override
	public void update(float dt){
		super.update(dt);
		System.out.println("BADGUY ID : "+id);
		timeElapsed += dt;
		if(timeElapsed> 1f){
			move();
			timeElapsed = 0;
		}
		
		body.setLinearVelocity(speed , body.getLinearVelocity().y);
	}
	
	public void move(){
		speed *= -1;
	}
	
	@Override
	public void render(SpriteBatch batch){
		
		batch.draw(animation.getFrame(), getWorldPosition().x - width/2, getWorldPosition().y - height/2, width, height);
	}

}
