package com.picklegames.gameStates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.picklegames.entities.Collectible;
import com.picklegames.entities.Player;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.handlers.GameStateManager;
import com.picklegames.handlers.MyContactListener;

// Miguel Garnica
// Nov 23, 2016
public class Play extends GameState {

	private TiledMap tileMap;
	private float tileMapWidth;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	private MyContactListener cl;
	
	private Texture tex;
	private Background[] bg;

	private Player player;
	private Array<Collectible> coll;

	public static int level;

	public Play(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		// create player
		player = new Player();
		player.setVelocity(0, 0);
		createPlayerBox2D();

		// create background
		EnglishGame.res.loadTexture("images/background.png", "bg");
		tex = EnglishGame.res.getTexture("bg");

		TextureRegion[] texR;
		texR = new TextureRegion[3];
		texR[0] = new TextureRegion(tex, 0, 0, 800, 600);
		texR[2] = new TextureRegion(tex, 0, 600, 800, 600);
		texR[1] = new TextureRegion(tex, 0, 1200, 800, 600);
		bg = new Background[texR.length];
		for (int i = 0; i < texR.length; i++) {
			bg[i] = new Background(texR[i], cam, 1 + i * .5f);
			if (i == 2) {
				bg[i].setVector(-8, 0);
			}
		}

		// create tiles
		createTileLayers();

		// create collectible
		coll = new Array<Collectible>();
		createCollectibles();
		
		// create and set world contact listener
		cl = new MyContactListener();
		EnglishGame.world.setContactListener(cl);
		
		// load sfx
		EnglishGame.res.loadSound("sfx/jump.wav", "jump");
		EnglishGame.res.loadSound("sfx/item.wav", "item");
		
		// load music
		EnglishGame.res.loadMusic("music/Black Violin - Opus.mp3", "opus");
		EnglishGame.res.getMusic("opus").setVolume(.15f);
		EnglishGame.res.getMusic("opus").play();

	}

	@Override
	public void handleInput() {

		if (Gdx.input.isKeyPressed(Keys.A)) {
			player.getBody().setLinearVelocity(-2, player.getBody().getLinearVelocity().y);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			player.getBody().setLinearVelocity(2, player.getBody().getLinearVelocity().y);
		} else {
			player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
		}

		if (Gdx.input.isKeyJustPressed(Keys.W) && cl.isPlayerOnGround()) {
			player.getBody().applyForceToCenter(0, 230, true);
			EnglishGame.res.getSound("jump").setVolume(0, .5f);
			EnglishGame.res.getSound("jump").play();

		}

	}

	@Override
	public void update(float dt) {

		handleInput();
		
		// update player
		player.update(dt);

		// update bg
		for (Background b : bg) {
			b.update(dt);
		}

		// update collectible
		for (Collectible c : coll) {
			c.update(dt);
		}
		
		// remove collectible
		Array<Body> bodies = cl.getBodiesToRemove();
		for(int i = 0; i < bodies.size; i++){
			Body b = bodies.get(i);
			coll.removeValue((Collectible) b.getUserData(), true );
			EnglishGame.world.destroyBody(b);
			player.collectItem();
			EnglishGame.res.getSound("item").play();
			
		}
		bodies.clear();

		// check player win
		if (player.getWorldPosition().x > tileMapWidth * tileSize) {
			System.out.println("YOU WIN!!");
		}

		// check player fall into void
		if (player.getWorldPosition().y < 0) {
			System.out.println("You lose!!");
		}

	}

	@Override
	public void render() {

		// claear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		setCameraBounds();

		// render background
		for (Background b : bg) {
			b.render(batch);
		}

		// render tile map
		batch.end();
		tmr.setView(cam);
		tmr.render();
		batch.begin();

		// render collectible
		for (Collectible c : coll) {
			c.render(batch);
		}

		// render player
		batch.setProjectionMatrix(cam.combined);
		player.render(batch);
	}

	public void createPlayerBox2D() {
		player.setBody(CreateBox2D.createPlayerBox(EnglishGame.world, 100, 100, player.getWidth(), player.getHeight(),
				new Vector2(player.getWidth() / 2, player.getHeight() / 2)));

	}//

	/*
	 * reads int tile map layers
	 */

	public void createTileLayers() {

		// load tile map and renderer
		try {
			tileMap = new TmxMapLoader().load("maps/test.tmx");
		} catch (Exception e) {
			System.out.println("Cannot find file: maps/test.tmx");
			Gdx.app.exit();
		}
		tmr = new OrthogonalTiledMapRenderer(tileMap);

		// get map properties
		MapProperties props = tileMap.getProperties();
		tileMapWidth = (int) props.get("width", Integer.class);
		tileSize = (int) props.get("tilewidth", Integer.class);

		// read each tile map layer and create box2d collision boxes
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blocked");
		createBox2DTiles(layer);
	}

	public void createBox2DTiles(TiledMapTileLayer layer) {

		// tileSize
		float ts = layer.getTileWidth();

		// go through all the cells in the layer
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {

				// get cell
				Cell cell = layer.getCell(col, row);

				// check if cell exists
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				// create body definition
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * ts / B2DVars.PPM, (row + 0.5f) * ts / B2DVars.PPM);

				// create shape
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				v[1] = new Vector2(-ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
				v[2] = new Vector2(ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
				v[3] = new Vector2(ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				v[4] = new Vector2(-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				cs.createChain(v);

				// create fixture definition
				FixtureDef fdef = new FixtureDef();
				fdef.shape = cs;
				fdef.friction = .15f;
				fdef.filter.categoryBits = B2DVars.BIT_WALL;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				EnglishGame.getWorld().createBody(bdef).createFixture(fdef);
				cs.dispose();

			}
		}

	}

	public void createCollectibles() {

		MapLayer layer = tileMap.getLayers().get("items");
		if (layer == null)
			return;

		for (MapObject mo : layer.getObjects()) {

			// create body definition
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;

			// get item position from object layer
			float x = (float) mo.getProperties().get("x", Float.class) / B2DVars.PPM;
			float y = (float) mo.getProperties().get("y", Float.class) / B2DVars.PPM;
			bdef.position.set(x, y);
			Body body = EnglishGame.world.createBody(bdef);

			// create shape
			CircleShape cs = new CircleShape();
			cs.setRadius(8 / B2DVars.PPM);

			// create fixture
			FixtureDef fdef = new FixtureDef();
			fdef.shape = cs;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_WALL;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("coll");
			cs.dispose();

			// create new collectible and add to collectible list
			Collectible c = new Collectible(body);
			body.setUserData(c);
			coll.add(c);

		}
	}

	public void setCameraBounds() {

		if (player.getWorldPosition().x < cam.viewportWidth / 2) {
			cam.position.set(cam.viewportWidth / 2, cam.position.y, 0);
			cam.update();
		} else if (player.getWorldPosition().x + cam.viewportWidth / 2 > tileMapWidth * tileSize) {
			cam.position.set((tileMapWidth * tileSize) - cam.viewportWidth / 2, cam.position.y, 0);
		} else {
			cam.position.set(player.getWorldPosition().x, cam.position.y, 0);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		player.dispose();

	}

}
