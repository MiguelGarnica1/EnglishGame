package com.picklegames.handlers;

import java.util.Stack;

import com.picklegames.game.EnglishGame;
import com.picklegames.gameStates.GameState;
import com.picklegames.gameStates.Menu;
import com.picklegames.gameStates.Play;

// Miguel Garnica
// Nov 23, 2016
public class GameStateManager {

	private EnglishGame game;
	private Stack<GameState> gameStates;

	public static final int MENU = 1111;
	public static final int PLAY = 1112;

	public GameStateManager(EnglishGame game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);

	}
	
	public EnglishGame game(){
		return game;
	}
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}

	public void render() {
		gameStates.peek().render();
	}

	public GameState getState(int state) {

		if (state == MENU) {
			return new Menu(this);
		} else if (state == PLAY) {
			return new Play(this);
		}
		
		return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);
	}
	
	public void pushState(int state){
		gameStates.push(getState(state));
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		g.dispose();
	}

}
