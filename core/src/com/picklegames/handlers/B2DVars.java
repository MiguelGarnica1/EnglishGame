package com.picklegames.handlers;

// Miguel Garnica
// Nov 23, 2016
public class B2DVars {
	// Box2D Variables
	/*
	 * Box2D uses the MKS system (Meter, Kilograms, Seconds) 1 pixel is equal to
	 * one meter, so a 1:1 ratio is not good Whenever dealing with Box2D MUST
	 * USE THE FOLLOWING CONVERSION RATIO (ex. 200px / PPM)
	 */

	// Ratio of 100:1 PPM (Pixel Per Meter)
	public static final float PPM = 100;

	// ////////////////////////////////////////////////////////////////////////

	// category bits
	/*
	 * 2 bytes = 16 bits (box2d is 16bit)
	 * 
	 * default category for every fixture is 1st bit 0000 0000 0000 0001 but we
	 * can specify differently ex. Ground is 2nd bit 0000 0000 0000 0010 or 4th
	 * bit 0000 0000 0000 0100 MUST USE STANDARD POWER OF 2 BITS ex. if try to
	 * use 7th bit 0000 0000 0000 0111 (this means object is 3 categories, and
	 * makes no sense)
	 */

	public static final short BIT_PLAYER = 2;
	public static final short BIT_WALL = 4;
	public static final short BIT_ITEM = 8;
	public static final short BIT_BAD = 16;
}
