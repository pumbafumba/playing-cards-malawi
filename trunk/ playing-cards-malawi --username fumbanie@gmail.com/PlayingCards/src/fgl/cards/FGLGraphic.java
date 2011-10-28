/**
 * Representative of graphic objects printed on the device screen
 * Copyright (c) 2011 FUMBA GAME LAB. All rights reserved
 * 
 * Malawi Playing Cards: FGLView.java
 * Fumba Game Lab View Object
 * 
 * @author Fumbani Chibaka
 * @version 1.0
 * @since 0.0
 * 
 * 
 * /*******************************************************************************
 * Copyright (c) 1998, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 ******************************************************************************
 *
 */
package fgl.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

public abstract class FGLGraphic {

	/** the graphical bitmap image of the custom view */
	protected Bitmap img;

	/** the unique id of the custom view */
	private String name; 

	/** the x- coordinate of the view current position. default = 0 */
	private int currentX = 0; 

	/** the y- coordinate of the view current position. default = 0 */
	private int currentY = 0;

	/** the x- coordinate of the cards original position after it was served */
	private int originalX; 

	/** the y- coordinate of the cards original position after it was served */
	private int originalY;

	/** the width of the view */
	protected int width; 

	/** the height of the view */
	protected int height;

	/** the context */
	protected Context context;


	public FGLGraphic(Context context, String name) {
		this.name = name;
		this.context = context;
		this.extractBitmap();
	}

	FGLGraphic(Context context, Point point, String name)
	{
		this(context, name);
		this.currentX = point.x;
		this.currentY = point.y;
	}


	/**
	 * 
	 */
	protected abstract void extractBitmap();


	/**
	 * Gets the cards' unique identifier
	 * @return the unique name of the card
	 */
	public String getName()
	{
		return this.name;
	}


	public Bitmap getBitmap() {
		return this.img;
	}

	public int getX() {
		return this.currentX;
	}

	public int getY() {
		return this.currentY;
	}

	/**
	 * Gets the center for the button
	 * @return a point representing the x and y coordinates for the buttons
	 */
	public Point getCenter() {
		int x = this.img.getWidth()/2;
		int y = this.img.getHeight()/2;
		return new Point(x,y);
	}

	public void setPosition(Point point) {
		this.currentX = point.x;
		this.currentY = point.y;
	}

	/**
	 * Resets the current position for the card to its original position.
	 * This method is used when the player drops the cards on screen areas which do not
	 * recognise the move.
	 */
	public void resetPosition() 
	{
		this.currentX = this.originalX;
		this.currentY = this.originalY;
	}


	/**
	 * Sets the y-coordinate for the card's current position.
	 * This method is used to move the card on the device screen.
	 * @param newY the new y-coordinate.
	 */
	void setY(int newY) {
		currentY = newY;
	}

	/**
	 * Sets the x-coordinate for the cards' position.
	 * this method is used to move the card on the device screen.
	 * @param newX the new x-coordinate
	 */
	void setX(int newX) {
		currentX = newX;
	}

	/**
	 * Sets the default position of the card
	 * @param point contain x and y coordinates for the default position
	 */
	public void setDefaultPosition(Point point) {
		this.originalX = point.x;
		this.originalY = point.y;   
	}

	/**
	 * Sets the current position of the card
	 * @param point contain x and y coordinates of the new position
	 */
	public void setCurrentPosition(Point point) {
		this.currentX = point.x;
		this.currentY = point.y;  
	}

	/**
	 * Calculates the offset position of the card once it is touched. Offset is used to center the card with the touch position.
	 * @param offset the offset position
	 * @see fgl.Controller.SetUpGameBoard.onTouchEvent
	 */
	public void setToCenter(Point offset) {
		this.currentX = this.currentX + offset.x;
		this.currentY = this.currentY + offset.y;
	}

	/**
	 * Gets the width of the card's bitmap image
	 * @return an integer value representing the width of the card
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Gets the height of the card
	 * @return the height of the card
	 */
	public int getHeight() {
		return this.height;
	}


	/**
	 * Checks to see if the graphic object was touched
	 * @return the offset position
	 */
	public  Point isTouched(Point touchPoint)
	{		
		Point cardCenter = this.getCenter();
		if( touchPoint.x >= this.getX() && touchPoint.x <= this.getX() + this.getWidth() )
		{
			if ( touchPoint.y >= this.getY() && touchPoint.y <= this.getY() + this.getHeight())
			{
				int x =  touchPoint.x - ( cardCenter.x + this.getX() ); 
				int y =  touchPoint.y - ( cardCenter.y + this.getY()) ; 				
				return new Point(x, y);
			}
		}
		return null;
	}
	

}