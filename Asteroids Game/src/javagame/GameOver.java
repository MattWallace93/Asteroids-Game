package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * GameOver class creates the end menu and is the final state of the game
 * 
 * @author Matt W
 * @version 1.0
 */

public class GameOver extends BasicGameState{
	//Deceleration of class variables
	private Image background;
	private Image ReturnGame;
	private Image GameOver;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public GameOver(int state){
	}

	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		//sets image variables to file directory
		ReturnGame = new Image("res/BackToMainMenu.png");
		background = new Image("res/space background.png");
		GameOver = new Image("res/GameOver.png");
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		//draws the images on screen
		background.draw(0, 0);
		GameOver.draw(570,300);
		ReturnGame.draw(600,750);
		
	}
	
	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		//variable to hold user input
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		//if statement to check if user clicks on image and then sends to another state
		if ((xpos>600&&xpos<1360) && (ypos>290&&ypos<330)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(0);
			}
		}
	}
	
	//returns the id of the current state
	public int getID(){
		return 7;
	}
}