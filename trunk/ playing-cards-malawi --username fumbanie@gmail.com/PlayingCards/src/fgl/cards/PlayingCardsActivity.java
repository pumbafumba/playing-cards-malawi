/**
 * 
 * Copyright (c) 2011 FUMBA GAME LAB. All rights reserved
 * 
 * Malawi Playing Cards: PlayingCardsActivity.java
 * This class is called once the application is initiated on the android device
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
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class PlayingCardsActivity extends Activity {

	/** Organises all the game elements */
	private static GameBoardLayout elements; 
	
	/** The screen width of the device */
	public static int width;
	
	/** The screen height of the device */
	public static int height;

	/** Called when the activity is first created. */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Display display = this.getWindowManager().getDefaultDisplay(); 
		width = display.getWidth();
		height = display.getHeight();
		
		Tools.catLog("width : "+ width + " Height: "+ height);
		
		elements = new GameBoardLayout(this);
		this.setContentView(elements);
	}


	/**
	 * Used to print debugging text on the android device screen
	 * @param  text The text to be printed on the android device screen
	 */
	public static void printDebug(String text)
	{
		//note: only updates text if input text is different from the one currently displayed
		if (elements.getDebugTextView().getText() != text)
			elements.getDebugTextView().setText(text);
	}
}