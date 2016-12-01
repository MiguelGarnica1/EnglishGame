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
public class MyContactListener implements ContactListener {

	private int groundContact = 0;
	private int playerHit = 0;
	
	private Array<Body> bodiesToRemove;
	private Array<Body> baddiesToRemove;
	private boolean playerIsdDead = false;

	public MyContactListener() {
		super();
		bodiesToRemove = new Array<Body>();
		baddiesToRemove = new Array<Body>();
	}

	@Override
	// called when two fixtures start to coll
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		// contact with foot and ground
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			groundContact++;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			groundContact++;
		}

		// contact with player and collectible
		if (fa.getUserData() != null && fa.getUserData().equals("coll") && fb.getUserData().equals("player")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("coll") && fa.getUserData().equals("player")) {
			bodiesToRemove.add(fb.getBody());
		}

		// contact with player foot and bad guy head
		if (fa.getUserData() != null && fb.getUserData() != null) {
			if (fa.getUserData().equals("head") && fb.getUserData().equals("foot")) {
				baddiesToRemove.add(fa.getBody());
			}
			if (fb.getUserData().equals("head") && fa.getUserData().equals("foot")) {
				baddiesToRemove.add(fb.getBody());
			}
		}
		

		// contact with player and bad guy sides
		if (fa.getUserData() != null && fb.getUserData() != null) {
			if (fa.getUserData().equals("side") && fb.getUserData().equals("player")) {
				playerHit++;
				System.out.println("CONTACT BITCH");
				
			}
			if (fb.getUserData().equals("side") && fa.getUserData().equals("player")) {
				playerHit++;
				System.out.println("CONTACT BITCH");
			}
		}

	}

	@Override
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			groundContact--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			groundContact--;
		}

	}

	public Array<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}

	public Array<Body> getBaddiesToRemove() {
		return baddiesToRemove;
	}

	public boolean isPlayerOnGround() {
		return groundContact > 0;
	}
	
	public boolean isPlayerHit(){
		return playerHit > 0;
	}

	// COLLISON
	/*
	 * collision detection presolve collision handling postsolve
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
