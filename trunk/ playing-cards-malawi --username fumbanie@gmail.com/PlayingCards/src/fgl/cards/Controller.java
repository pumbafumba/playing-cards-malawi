/**
 * 
 * Copyright (c) 2011 FUMBA GAME LAB. All rights reserved
 * 
 * Malawi Playing Cards: SetUpGameBoard.java
 * Collects all necessary elements needed for gameplay. This class controls and manages the elements.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class Controller extends View {

	/** interface to global information about an application environment */
	private Context context; 

	/** collection of all the cards in the deck */
	private List<Card> cardDeck = new ArrayList<Card>();

	/** collection of cards served to player */
	private List<Card> servedCards = new ArrayList<Card>();

	/** card letters */
	private final String [] CARD_LETTERS = {"A","2","3","4","5","6","7","8","9","10", "J", "K", "Q"};

	/** card suites: Spade, Heart, Diamond, Club */
	private final String [] CARD_SUITES = {"S","H","D","C"}; 

	/** currently selected card */
	private String activeCard;

	/** offset position relative to the touch point */
	private Point offset = new Point();

	/** Collection of buttons used in the game */
	private List<CustomButton> buttons = new ArrayList<CustomButton>();

	
	private Point touchPoint;


	/**
	 * Sets up the gameboard and manages it
	 * @param context interface to global information about an application environment 
	 */
	public Controller(Context context) 
	{
		super(context);
		this.touchPoint = new Point();
		this.context = context;
		this.setFocusable(true);
		this.makeCards();
		
		this.showMainMenu();
		//this.serveCards();
	}

	/**
	 * Shows the main menu for the application
	 */
	private void showMainMenu() {
		
		
		CustomButton button = new CustomButton(this.context, new Point(), "main_btn");
		
		Point center = button.getCenter();
		Point midpoint = new Point ( PlayingCardsActivity.width / 2 - center.x , PlayingCardsActivity.height / 2 - center.y);
		button.setPosition(midpoint);
		
		buttons.add(button);
	}

	/**
	 * Picks a random card from the card deck. 
	 * @return	A random card from the card deck 
	 */
	private Card pickRandomCard() {
		return this.cardDeck.remove( new Random().nextInt( this.cardDeck.size() ));
	}


	/**
	 * Makes 54 card objects that are to be reused throughout gameplay. 
	 */
	private void makeCards() {
		//Step 1: Add normal cards
		for (int s = 0; s < this.CARD_SUITES.length; s++)
			for (int l = 0; l < this.CARD_LETTERS.length; l++)
				this.cardDeck.add(new Card(this.context, this.CARD_LETTERS[l] + this.CARD_SUITES[s]) );

		//Step 2: Add two jokers to the collection
		this.cardDeck.add( new Card(this.context,"*B")); //B = Black and White
		this.cardDeck.add( new Card(this.context,"*C")); //C = Color
	}


	/**
	 * Takes cards from the deck and puts them in player hands
	 */
	private void serveCards() {

		Point point1 = new Point(100,100); 
		Point point2 = new Point(200,300); 
		Point point3 = new Point(100,400);

		Card card1 = this.pickRandomCard();
		card1.setDefaultPosition(point1);
		card1.setCurrentPosition(point1);
		servedCards.add(card1);

		Card card2 = this.pickRandomCard();
		card2.setDefaultPosition(point2);
		card2.setCurrentPosition(point2);
		servedCards.add(card2);

		Card card3 = this.pickRandomCard();
		card3.setDefaultPosition(point3);
		card3.setCurrentPosition(point3);
		servedCards.add(card3);

		//XXX: Print Serving Summary to Eclipse log
		Tools.catLog( "Random Card 1  : " + card1.getName());
		Tools.catLog( "Random Card 2  : " + card2.getName());
		Tools.catLog( "Random Card 3  : " + card3.getName());
		Tools.catLog( "# Served Cards : " + this.servedCards.size());
		Tools.catLog( "# Cards in Deck: " + this.cardDeck.size());
	}

	/**
	 * Overrides View.ondraw method. Draws images on the canvas
	 * @param canvas The canvas onto which the bitmaps will be drawn            
	 * @see	View
	 */
	@Override protected void onDraw(Canvas canvas) {
		for (CustomButton button: this.buttons)
			canvas.drawBitmap( button.getBitmap(), button.getX(), button.getY(), null);
		for (Card card : this.servedCards)
			canvas.drawBitmap(card.getBitmap(), card.getX(), card.getY(), null);
	}

	/**
	 * Detects screen touch gestures and moves the selected card accordingly
	 * @param	event The detected motion event
	 * @return	A boolean?? //TODO:                
	 * @see	MotionEvent
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction(); 
		
		this.touchPoint = new Point( (int)event.getX(),(int)event.getY());
		//TODO: Find out how switch case works in this case...

		switch (eventAction ) {
		//ACTION 1: TOUCH DOWN
		case MotionEvent.ACTION_DOWN:
			this.buttonDown();
			
			this.activeCard = "";//reset active card

			//Check to see if a card being touched
			for (Card card : this.servedCards) 
			{
				Point check = card.isTouched(touchPoint);
				if ( check != null ){
					this.offset = check;
					this.activeCard = card.getName();
					this.bringToFront(card); //Important: bring the active card to front
					card.setToCenter( this.offset );
					break;
				}
			}
			
			
			break; 

			//ACTION 2: DRAG (MOVE)
		case MotionEvent.ACTION_MOVE:
			if (this.activeCard != "") 
			{
				Card card = this.getServedCard(this.activeCard);
				card.setX(touchPoint.x - card.getCenter().x);
				card.setY(touchPoint.y - card.getCenter().y);
				PlayingCardsActivity.printDebug( "Card : " + card.getName() + "  Position : " + card.getX() + "," + card.getY() );
			}
			break; 

			//ACTION 3: DROP ( TOUCH UP)
		case MotionEvent.ACTION_UP: 
			if (activeCard != "")  
				this.getServedCard(activeCard).resetPosition();
			break; 
		} 

		this.invalidate(); //Redraw the canvas 
		return true; 
	}

	/**
	 * Performs necessary action when a button is touched
	 */
	private void buttonDown() {
		//Check to see if a button was pressed
		for (CustomButton button : this.buttons) 
		{
			Point check = button.isTouched(touchPoint);
			if ( check != null){
				buttons.remove(button);
				this.invalidate();
				
				//Serve cards at this point
				this.serveCards();
				break;
			}
		}
	}

	/**
	 * Returns a card object from the collection of served cards
	 * @param	cardName The name of the card to be extracted
	 * @return	The specified card. Returns null if no cards could be identified by the specified name
	 */	
	private Card getServedCard(String cardName) {
		for( Card card: this.servedCards){
			if (card.getName().equalsIgnoreCase(cardName))
				return card;
		}
		return null;
	}


	/**
	 * Brings the selected card to the top of the list.
	 * @param	touchedCard The card to be brought to the front
	 */
	private void bringToFront(Card touchedCard) {

		for(int i = 0; i < this.servedCards.size(); i++ )
		{
			Card card = this.servedCards.get(i);
			if ( card.getName().equalsIgnoreCase( touchedCard.getName()))
			{
				this.servedCards.remove(i);
				this.servedCards.add(card);
				this.invalidate(); //Redraw canvas
			}
		}
	}


}