package com.picklegames.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.picklegames.entities.BadGuy;
import com.picklegames.entities.Collectible;
import com.picklegames.entities.Player;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.handlers.GameStateManager;
import com.picklegames.handlers.HUD;
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
	private Array<BadGuy> baddies;

	private HUD hud;
	private BitmapFont font;
	
	
	private World world;
	private Box2DDebugRenderer bdr;
	public static final boolean DEBUG = false;
	public static int level;
	public static int deaths;
	public static int burgersEat;
	public static int score;
	public static float timePlayed;

	public Play(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		//create Box2D world
		world = new World(new Vector2(0, -9.81f), true);
		

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
		player.setTotalItems(coll.size);

		// create bad guys
		baddies = new Array<BadGuy>();
		createBadguys();

		// create and set world contact listener
		cl = new MyContactListener();
		world.setContactListener(cl);

		// set box2d boundaries at edge of tile map
		addBound(new Vector2(0, 0), new Vector2(0, cam.viewportHeight), B2DVars.BIT_WALL, B2DVars.BIT_PLAYER);
		addBound(new Vector2(tileMapWidth * tileSize, 0), new Vector2(tileMapWidth * tileSize, cam.viewportHeight),
				B2DVars.BIT_WALL, B2DVars.BIT_PLAYER);

		// load font
		font = new BitmapFont();

		EnglishGame.res.getMusic("opus").setVolume(.15f);
		if(!EnglishGame.res.getMusic("opus").isPlaying()){
			EnglishGame.res.getMusic("opus").play();
		}

		hud = new HUD(player.getTotalItems());
		
		if (DEBUG) {
			bdr = new Box2DDebugRenderer();
		}

	}

	@Override
	public void handleInput() {

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			player.getBody().setLinearVelocity(-2, player.getBody().getLinearVelocity().y);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.getBody().setLinearVelocity(2, player.getBody().getLinearVelocity().y);
		} else {
			player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
		}

		if (Gdx.input.isKeyJustPressed(Keys.UP) && cl.isPlayerOnGround()) {
			player.getBody().applyForceToCenter(0, 230, true);
			EnglishGame.res.getSound("jump").setVolume(0, .5f);
			EnglishGame.res.getSound("jump").play();

		}

	}

	@Override
	public void update(float dt) {
		if (!hud.isWin() && !hud.isPlayerDead()) {
			handleInput();
		}
		
		timePlayed+=dt;
		
		//update world
		world.step(dt, 4, 4);

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

		// update bad guys
		for (BadGuy b : baddies) {
			b.update(dt);
		}

		// remove collectible
		Array<Body> bodies = cl.getBodiesToRemove();
		for (int i = 0; i < bodies.size; i++) {
			Body b = bodies.get(i);
			coll.removeValue((Collectible) b.getUserData(), true);
			world.destroyBody(b);
			player.collectItem();
			player.setScale(player.getWidth() * 1.05f, player.getHeight() * 1.05f);
			EnglishGame.res.getSound("item").play();
			burgersEat++;
			score++;

		}
		bodies.clear();

		Array<Body> badguys = cl.getBaddiesToRemove();
		for (int i = 0; i < badguys.size; i++) {
			Body b = badguys.get(i);
			baddies.removeValue((BadGuy) b.getUserData(), true);
			player.getBody().applyLinearImpulse(0, 2, 0, 0, true);
			world.destroyBody(b);
			EnglishGame.res.getSound("hit").play();
			score++;

		}
		badguys.clear();

		// check player win
		if (player.getWorldPosition().x > tileMapWidth * tileSize - player.getWidth() * 2) {
			System.out.println("YOU WIN!!");
			hud.setWin(true);
		}
		
		hud.update(player.getNumItems(), dt);
		if(hud.getContinueButton().isClicked()){
			if(level<3){
				level++;
				//dispose();
				gsm.setState(GameStateManager.PLAY);
			}else{
				//dispose();
				gsm.setState(GameStateManager.END);
			}
		}
		if(hud.getRetryButton().isClicked()){
			score--;
			deaths++;
			gsm.setState(GameStateManager.PLAY);
		}
		
		//check player hit
		if(cl.isPlayerHit()){
			player.getBody().applyLinearImpulse((float) Math.random() * -10,(float) Math.random() * 5, 0, 0, true);
			hud.setPlayerDead(true);
			
		}

		// check player fall into void
		if (player.getWorldPosition().y < 0) {
			System.out.println("You lose!!");
			hud.setPlayerDead(true);
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

		// render bad guys
		for (BadGuy b : baddies) {
			b.render(batch);
		}

		// render player
		batch.setProjectionMatrix(cam.combined);
		player.render(batch);

		batch.setProjectionMatrix(hudCam.combined);
		hud.render(batch);
	
		batch.setProjectionMatrix(cam.combined);
		if (DEBUG) {
			bdr.render(world, cam.combined.scl(B2DVars.PPM));
		}
		
	}

	public void createPlayerBox2D() {
		player.setBody(CreateBox2D.createPlayerBox(world, 100, 100, player.getWidth(), player.getHeight(),
				new Vector2(player.getWidth() / 2, player.getHeight() / 2)));

	}//

	/*
	 * reads int tile map layers
	 */

	public void createTileLayers() {

		// load tile map and renderer
		try {
			tileMap = new TmxMapLoader().load("maps/level" + level + ".tmx");
		} catch (Exception e) {
			System.out.println("Cannot find file: maps/level" + level + ".tmx");
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
				fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BAD;
				world.createBody(bdef).createFixture(fdef);
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
			Body body = world.createBody(bdef);

			// create shape
			CircleShape cs = new CircleShape();
			cs.setRadius(8 / B2DVars.PPM);

			// create fixture
			FixtureDef fdef = new FixtureDef();
			fdef.shape = cs;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_ITEM;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("coll");
			cs.dispose();

			// create new collectible and add to collectible list
			Collectible c = new Collectible(body);
			body.setUserData(c);
			coll.add(c);

		}
	}

	public void createBadguys() {

		MapLayer layer = tileMap.getLayers().get("bad");
		if (layer == null)
			return;

		for (MapObject mo : layer.getObjects()) {

			BadGuy b = new BadGuy();
			

			// create body definition
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;

			// get item position from object layer
			float x = (float) mo.getProperties().get("x", Float.class) / B2DVars.PPM;
			float y = (float) mo.getProperties().get("y", Float.class) / B2DVars.PPM;
			bdef.position.set(x, y);
			Body body = world.createBody(bdef);

			// create shape
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(b.getWidth() / 4 / B2DVars.PPM, b.getHeight() * .45f / B2DVars.PPM);

			// create fixture
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.restitution = .75f;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_WALL;
			body.createFixture(fdef).setUserData("bad");

			// middle head sensor
			shape = new PolygonShape();
			shape.setAsBox(2 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, b.getHeight() * .45f / B2DVars.PPM), 0);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(fdef).setUserData("head");

			// left head sensor
			shape.setAsBox(2 / B2DVars.PPM, 2 / B2DVars.PPM,
					new Vector2(-b.getWidth() / 6 / B2DVars.PPM, b.getHeight() * .45f / B2DVars.PPM), 0);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(fdef).setUserData("head");

			// right head sensor
			shape.setAsBox(2 / B2DVars.PPM, 2 / B2DVars.PPM,
					new Vector2(b.getWidth() / 6 / B2DVars.PPM, b.getHeight() * .45f / B2DVars.PPM), 0);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(fdef).setUserData("head");

			// left side
			shape.setAsBox(2 / B2DVars.PPM, 10 / B2DVars.PPM, new Vector2(-b.getWidth() / 4 / B2DVars.PPM, 0), 0);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(fdef).setUserData("side");

			// right side
			shape.setAsBox(2 / B2DVars.PPM, 10 / B2DVars.PPM, new Vector2(b.getWidth() / 4 / B2DVars.PPM, 0), 0);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_BAD;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(fdef).setUserData("side");

			shape.dispose();

			// create new collectible and add to collectible list
			b.setBody(body);
			body.setUserData(b);
			baddies.add(b);

		}
	}

	public void addBound(Vector2 x1y1, Vector2 x2y2, short categoryBits, short maskBits) {

		Body body;

		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		ChainShape cs = new ChainShape();
		Vector2[] vertices = new Vector2[2];
		vertices[0] = new Vector2(x1y1.x / B2DVars.PPM, x1y1.y / B2DVars.PPM);
		vertices[1] = new Vector2(x2y2.x / B2DVars.PPM, x2y2.y / B2DVars.PPM);
		cs.createChain(vertices);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BAD;
		body.createFixture(fdef);

		cs.dispose();

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
//		player.dispose();
//		world.clearForces();
//		for(BadGuy b : baddies){
//			b.dispose();
//			world.destroyBody(b.getBody());
//		}
//		baddies.clear();
//		for(Collectible c : coll){
//			c.dispose();
//			world.destroyBody(c.getBody());
//		}
//		world.destroyBody(player.getBody());
//		coll.clear();
//		bdr.dispose();
//		world.dispose();
//		tex.dispose();
//		tileMap.dispose();
//		tmr.dispose();
//		font.dispose();
			

	}

}
