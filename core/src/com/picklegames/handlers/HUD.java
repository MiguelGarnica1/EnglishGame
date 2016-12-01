package com.picklegames.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.picklegames.game.EnglishGame;

// Miguel Garnica
// Nov 28, 2016
public class HUD {

	private TextureRegion texR;
	private int total;
	private int cNum;
	private BitmapFont font;
	private GlyphLayout layout;

	private GameButton continueButton;
	private boolean win = false;
	private boolean isPlayerDead = false;
	private GameButton retryButton;

	public HUD(float total) {
		this.total = (int) total;
		init();
	}

	public void init() {

		texR = new TextureRegion(EnglishGame.res.getTexture("burger"));
		font = new BitmapFont(Gdx.files.internal("fonts/comicsan.fnt"));
		font.getData().setScale(1);
		font.setColor(Color.GOLD);

		layout = new GlyphLayout();
		
		EnglishGame.res.loadTexture("images/button.png", "button");
		Texture tex = EnglishGame.res.getTexture("button");
		continueButton = new GameButton(new TextureRegion(tex), EnglishGame.getHudCam().viewportWidth / 2,
				EnglishGame.getHudCam().viewportHeight / 2 - 100, 200, 100, EnglishGame.getHudCam());
		continueButton.setText("Continue", 1.25f, Color.GOLDENROD);
		
		retryButton = new GameButton(new TextureRegion(tex), EnglishGame.getHudCam().viewportWidth / 2,
				EnglishGame.getHudCam().viewportHeight / 2 - 100, 200, 100, EnglishGame.getHudCam());
		retryButton.setText("Retry", 1.25f, Color.GOLDENROD);
		
	}

	public void update(float cNum, float dt) {
		this.cNum = (int) cNum;
		if (win) {
			continueButton.update(dt);
		}
		if(isPlayerDead){
			retryButton.update(dt);
		}

	}

	public void render(SpriteBatch batch) {

		batch.draw(texR, EnglishGame.hudCam.viewportWidth / 2,
				EnglishGame.hudCam.viewportHeight - texR.getRegionHeight() * 1.75f);
		font.draw(batch, cNum + " / " + total, EnglishGame.hudCam.viewportWidth / 2 + texR.getRegionWidth() * 2,
				EnglishGame.hudCam.viewportHeight - texR.getRegionHeight());
		

		if (win) {
			font.getData().setScale(2);
			layout.setText(font, "YOU ARE WINNER!");
			font.draw(batch, "YOU ARE WINNER!", EnglishGame.hudCam.viewportWidth / 2 - layout.width / 2,
					EnglishGame.hudCam.viewportHeight / 2 + layout.height / 2);
			continueButton.render(batch);

		}
		
		if(isPlayerDead){
			font.getData().setScale(2);
			layout.setText(font, "YOU ARE DEAD!");
			font.draw(batch, "YOU ARE DEAD!", EnglishGame.hudCam.viewportWidth / 2 - layout.width / 2,
					EnglishGame.hudCam.viewportHeight / 2 + layout.height / 2);
			retryButton.render(batch);
		}

	}

	public void setWin(Boolean b) {
		win = b;
	}

	public boolean isWin() {
		return win;
	}

	public GameButton getContinueButton() {
		return continueButton;
	}

	public boolean isPlayerDead() {
		return isPlayerDead;
	}

	public void setPlayerDead(boolean isPlayerDead) {
		this.isPlayerDead = isPlayerDead;
	}

	public GameButton getRetryButton() {
		return retryButton;
	}
	public void dispose(){
		font.dispose();
		
	}
}
