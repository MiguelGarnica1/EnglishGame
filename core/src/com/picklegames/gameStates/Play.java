package com.picklegames.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.picklegames.entities.Player;
import com.picklegames.game.EnglishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.handlers.GameStateManager;

// Miguel Garnica
// Nov 23, 2016
public class Play extends GameState {

	private TiledMap tileMap;
	private float tileMapWidth;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;

	private Player player;
	

	public static int level;

	public Play(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		// create tiles
		player = new Player();
		player.setVelocity(0, 0);
		createPlayerBox2D();

		// create tiles
		createTileLayers();

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
		
		if(Gdx.input.isKeyJustPressed(Keys.W)){
			player.getBody().applyForceToCenter(0,230, true);
			
		}

	}

	@Override
	public void update(float dt) {

		handleInput();

		// update player
		player.update(dt);

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

		// set camera to follow player
		batch.setProjectionMatrix(cam.combined);
		game.getCam().position.set(player.getWorldPosition().x + EnglishGame.V_WIDTH / 6, EnglishGame.V_HEIGHT / 2, 0);
		cam.update();

		// render tile map
		batch.end();
		tmr.setView(cam);
		tmr.render();
		batch.begin();

		// render player
		batch.setProjectionMatrix(cam.combined);
		player.render(batch);
	}

	public void createPlayerBox2D() {
		player.setBody(CreateBox2D.createBox(EnglishGame.getWorld(), 100, 100, player.getWidth()/4,player.getHeight() * .75f,
				new Vector2(player.getWidth() / 2, player.getHeight() / 2), BodyType.DynamicBody, B2DVars.BIT_PLAYER,
				B2DVars.BIT_WALL, "player"));
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
				bdef.position.set((col + 0.5f)* ts / B2DVars.PPM, (row + 0.5f)* ts / B2DVars.PPM);

				// create shape
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(
						-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				v[1] = new Vector2(
						-ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
				v[2] = new Vector2(
						ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		player.dispose();

	}

}
