package com.picklegames.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

// Miguel Garnica
// Nov 24, 2016
public class GameButton {

	// center at x, y
	private float x;
	private float y;
	private float width;
	private float height;

	private TextureRegion reg;
	private BitmapFont font;
	private String text;

	Vector3 vec;
	private OrthographicCamera cam;

	private boolean clicked;

	public GameButton(TextureRegion reg, float x, float y, OrthographicCamera cam) {
		this.reg = reg;
		this.x = x;
		this.y = y;
		this.cam = cam;

		width = reg.getRegionWidth();
		height = reg.getRegionHeight();
		vec = new Vector3();

		font = new BitmapFont();
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setText(String s) {
		text = s;
	}

	public void update(float dt) {
		vec.set(Gdx.input.getX(), cam.viewportHeight - Gdx.input.getY(), 0);
		cam.unproject(vec);

		if (Gdx.input.isButtonPressed(Buttons.LEFT) && vec.x > x - width / 2 && vec.x < x + width / 2
				&& vec.y > y - height / 2 && vec.y < y + height / 2) {
			clicked = true;

		} else {
			clicked = false;
		}
	}

	public void render(SpriteBatch batch) {

		batch.draw(reg, x - width / 2, y - height / 2, width, height);

		if (text != null) {
			drawString(batch, text, x, y);
		}
	}

	public void drawString(SpriteBatch batch, String s, float x, float y) {

		font.draw(batch, s, x, y);
	}
}
