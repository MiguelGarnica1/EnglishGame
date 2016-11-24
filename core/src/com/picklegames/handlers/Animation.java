package com.picklegames.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Miguel Garnica
// Nov 23, 2016
public class Animation {
	public final static int INFINITE = -1;

	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;
	private boolean isCompleted;

	public Animation() {
	}

	public Animation(TextureRegion[] frames) {
		this(frames, 1 / 12f);
	}

	public Animation(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
	}

	public void setDelay(float f) {
		delay = f;
	}

	public void setCurrentFrame(int i) {
		if (i < frames.length)
			currentFrame = i;
	}

	public void setFrames(TextureRegion[] frames) {
		setFrames(frames, 1 / 12f);
	}

	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;

	}

	public void play(float dt, int times) {
		if (times == INFINITE) {
			update(dt);
		} else {
			if (delay <= 0)
				return;
			time += dt;
			if (timesPlayed < times) {
				while (time >= delay) {
					next();
				}
			} else if (timesPlayed >= times) {
				timesPlayed = 0;
				isCompleted = true;
			}
		}

	}

	public void update(float dt) {
		if (delay <= 0)
			return;
		time += dt;
		while (time >= delay) {
			next();
		}
	}

	public void next() {
		time -= delay;
		currentFrame++;
		if (currentFrame == frames.length) {
			currentFrame = 0;
			timesPlayed++;
		}
	}

	public void reset() {
		isCompleted = false;
		timesPlayed = 0;
		currentFrame = 0;

	}

	public TextureRegion getFrame() {
		return (frames[currentFrame]);
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public boolean hasPlayedOnce() {
		return timesPlayed > 0;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void dispose() {
		if (frames == null)
			return;
		for (TextureRegion t : frames) {
			t.getTexture().dispose();
		}
	}
}
