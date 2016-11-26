package com.picklegames.handlers;

import static com.picklegames.handlers.B2DVars.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// Miguel Garnica
// Nov 23, 2016
public final class CreateBox2D {
	
	public static Body createBox(World world, float x, float y, float width, float height, Vector2 center,
			BodyType bodyType, short categoryBits, short maskBits, String userData) {

		// create body definition
		BodyDef bdef = new BodyDef();
		bdef.position.set(x / PPM, y / PPM);
		bdef.type = bodyType;

		// create body from the body definition
		Body body = world.createBody(bdef);
		body.setUserData(userData);
		// create box shape for player collision
		PolygonShape shape = new PolygonShape();
		// shape.set(new float[]{0,,1,1});
		shape.setAsBox(width / PPM, height / PPM, new Vector2(center.x / PPM, center.y / PPM), 0);

		// create fixture definition
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;

		// create player collision box fixture
		body.createFixture(fdef).setUserData(userData);
		shape.dispose();

		// final tweaks, manually set the player body mass to 1 kg
		MassData md = body.getMassData();
		md.mass  = 1f;
		body.setMassData(md);

		return body;

	}
}
