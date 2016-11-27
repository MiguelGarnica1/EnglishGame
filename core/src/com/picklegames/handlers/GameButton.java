package com.picklegames.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
	private GlyphLayout layout;

	Vector3 vec;
	private OrthographicCamera cam;

	private boolean clicked;

	public GameButton(TextureRegion reg, float x, float y, float width, float height, OrthographicCamera cam) {
		this.reg = reg;
		this.x = x;
		this.y = y;
		this.cam = cam;
		this.width = width;
		this.height = height;
		vec = new Vector3();

		font = new BitmapFont();
		layout = new GlyphLayout();
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setText(String s) {
		text = s;
		layout.setText(font, text);
	}

	public void update(float dt) {
		vec.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(vec);

		System.out.println("BUTTON XY: " + x +" " +y);
		System.out.println("MOUSE XY: " + vec.toString());
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
			font.getData().setScale(2);
			drawString(batch, text, x - layout.width, y + layout.height/2);
		}
	}

	public void drawString(SpriteBatch batch, String s, float x, float y) {

		font.draw(batch, s, x, y);
	}
}
