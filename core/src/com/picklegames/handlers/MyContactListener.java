package com.picklegames.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

// Miguel Garnica
// Nov 27, 2016
public class MyContactListener implements ContactListener{
	
	private int groundContact =0;
	private int numFootContacts;
	private Array<Body> bodiesToRemove;
	
	public MyContactListener(){
		super();
		bodiesToRemove = new Array<Body>();
	}
	
	@Override
	// called when two fixtures start to colle
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			groundContact++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			groundContact++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("coll")){
			bodiesToRemove.add(fa.getBody());
		}
		if(fb.getUserData() != null && fb.getUserData().equals("coll")){
			bodiesToRemove.add(fb.getBody());
		}
	}

	@Override
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			groundContact--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			groundContact--;
		}
		
	}
	
	public Array<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}
	
	public boolean isPlayerOnGround(){
		return groundContact > 0;
	}
	
	//COLLISON
		/* collision detection
		 * presolve
		 * collision handling
		 * postsolve
		 */

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
