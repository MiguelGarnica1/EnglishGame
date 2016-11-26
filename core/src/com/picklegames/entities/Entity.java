package com.picklegames.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.picklegames.handlers.Animation;
import com.picklegames.handlers.B2DVars;

// Miguel Garnica
// Nov 23, 2016
public class Entity {
	protected Body body;
	protected Vector2 velocity;
	protected Animation animation;
	protected float width;
	protected float height;

	public Entity(){
		animation = new Animation();
		velocity = new Vector2();
	}
	
	public Entity(Body body) {
		this.body = body;
		animation = new Animation();
		velocity = new Vector2();
	}

	public void setBody(Body body){
		this.body = body;
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}

	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}

	public void update(float dt) {
		animation.update(dt);
		//body.setLinearVelocity(velocity);
	}

	public void render(SpriteBatch batch) {
		try {
			if (animation.getFrame() != null) {
				batch.begin();
				batch.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - width / 2,
						body.getPosition().y * B2DVars.PPM - height / 2);
				batch.end();
			}
		} catch (NullPointerException e) {
			//System.out.println(e.getMessage());
		}
	}
	

	public void dispose(){
		body.getFixtureList().clear();
		animation.dispose();
	}
	
	public Body getBody() {
		return body;
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public Vector2 getWorldPosition() {
		return body.getPosition().scl(B2DVars.PPM);
	}
	
	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
		
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public void setWidth(float w){
		width = w;
	}
	public void setHeight(float h){
		height = h;
	}
	
	public void setSize(float w, float h){
		width = w;
		height = h;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(float x, float y) {
		this.velocity = new Vector2(x, y);
	}

	public void setVelocityX(float x) {
		this.velocity.x = x;
	}

	public void setVelocityY(float y) {
		this.velocity.y = y;
	}
	public Animation getAnimation() {
		return animation;
	}
	
	public void scl(float scl){
		width *= scl;
		height *= scl;
	}
	
	public void sclX(float scl){
		width = animation.getFrame().getRegionWidth() * scl;
	}
	
	public void sclY(float scl){
		height = animation.getFrame().getRegionWidth() * scl;
	}
}
