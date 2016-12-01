package com.picklegames.handlers;

import com.badlogic.gdx.Input.TextInputListener;

// Miguel Garnica
// Nov 30, 2016
public class MyTextInputListener implements TextInputListener{

	private String name;
	
	@Override
	public void input(String text) {
		// TODO Auto-generated method stub
		
		name = text;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String s){
		name = s;
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub
		
	}

}
