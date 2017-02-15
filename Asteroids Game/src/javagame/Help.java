package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * Help class creates the help menu for the PlayClassic state
 * displaying the controls on how to play
 * 
 * @author Matt W
 * @version 1.0
 */

public class Help extends BasicGameState{
	
	//Deceleration of class variables
	private Image background;
	private Image ReturnGame;
	private Image HelpContent;
	private Image Title;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public Help(int state){
	}

	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		//sets image variables to file directory
		ReturnGame = new Image("res/HelpRTG.png");
		background = new Image("res/space background.png");
		HelpContent = new Image("res/HelpContent.png");
		Title = new Image("res/HelpMenu.png");
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		//draws the images on screen
		background.draw(0, 0);
		Title.draw(570,100);
		HelpContent.draw(550,325);
		ReturnGame.draw(600,850);
		
	}
	
	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		//variables to hold user input
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		//if statement to check if user clicks on image and then sends to another state
		if ((xpos>600&&xpos<1360) && (ypos>185&&ypos<225)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(1);
			}
		}
	}
	
	//return the id of the current state
	public int getID(){
		return 2;
	}
}