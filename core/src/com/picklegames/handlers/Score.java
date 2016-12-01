package com.picklegames.handlers;

import com.badlogic.gdx.Gdx;

// Miguel Garnica
// Nov 30, 2016
public class Score {
	
	private String[]score;
	private String name;
	private int points;
	private int burgers;
	private int deaths;	
	
	
	public Score(String s){
		score = s.split(":");
		name = score[0];
		points = Integer.parseInt(score[1]);
		burgers = Integer.parseInt(score[2]);
		deaths = Integer.parseInt(score[3]);
		
	}

	public String[] getScore() {
		return score;
	}

	public void setScore(String[] score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getBurgers() {
		return burgers;
	}

	public void setBurgers(int burgers) {
		this.burgers = burgers;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	

}
