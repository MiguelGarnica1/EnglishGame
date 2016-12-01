package com.picklegames.game;

import java.awt.Color;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.Content;
import com.picklegames.handlers.GameStateManager;

public class EnglishGame extends ApplicationAdapter {
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;
	public static final float SCALE = 1f;
	public static Content res;

	private SpriteBatch batch;
	private OrthographicCamera cam;
	public static OrthographicCamera hudCam;
	private GameStateManager gsm;


	
	@Override
	public void create() {

		// load content
		res = new Content();

		// set up cameras
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH / SCALE, V_HEIGHT / SCALE);

		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH / SCALE, V_HEIGHT / SCALE);

		// load up game
		gsm = new GameStateManager(this);


	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		gsm.render();
		batch.end();

	}
	
	public void update(float dt) {
		cam.update();
		gsm.update(dt);
		
	}


	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();;
		res.removeAll();
	}

	public static OrthographicCamera getHudCam() {
		return hudCam;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

}
