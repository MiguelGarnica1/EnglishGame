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

	public static Body createPlayerBox(World world, float x, float y, float width, float height, Vector2 center) {

		// create body definition
		BodyDef bdef = new BodyDef();
		bdef.position.set(x / PPM, y / PPM);
		bdef.type = BodyType.DynamicBody;

		// create body from the body definition
		Body body = world.createBody(bdef);
		body.setUserData("player");
		// create box shape for player collision
		PolygonShape shape = new PolygonShape();
		// shape.set(new float[]{0,,1,1});
		shape.setAsBox(width / 4 / PPM, height * .85f / PPM, new Vector2(center.x / PPM, center.y / PPM), 0);

		// create fixture definition
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits =  B2DVars.BIT_WALL | B2DVars.BIT_ITEM | B2DVars.BIT_BAD;

		// create player collision box fixture
		body.createFixture(fdef).setUserData("player");
		shape.dispose();

		// create foot sensor

		// create middle "foot" sensor
		shape = new PolygonShape();
		shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(width / 2 / PPM, -height * .35f / PPM), 0);

		// create fixture definition
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_WALL | B2DVars.BIT_BAD;

		body.createFixture(fdef).setUserData("foot");

		// create bottom left
		shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(width / 3 / PPM, -height * .35f / PPM), 0);

		// create fixture definition
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_WALL | B2DVars.BIT_BAD;

		body.createFixture(fdef).setUserData("foot");

		// create bottom left
		shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(width/2 / PPM + width / 6 / PPM, -height * .35f / PPM), 0);

		// create fixture definition
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_WALL | B2DVars.BIT_BAD;

		body.createFixture(fdef).setUserData("foot");
		shape.dispose();

		// final tweaks, manually set the player body mass to 1 kg
		MassData md = body.getMassData();
		md.mass = 1f;
		body.setMassData(md);

		return body;

	}

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
		md.mass = 1f;
		body.setMassData(md);

		return body;

	}
}
