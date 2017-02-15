package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * Menu class creates the main menu state that allows players to
 * navigate from state to state with mouse clicks
 * 
 * @author Matt W
 * @version 1.0
 */

public class Menu extends BasicGameState{
	
	private Image background;
	private Image exitGame;
	private Image ClassicMode;
	private Image Title;
	private Image Multiplayer;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public Menu(int state){
	}
	
	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		//sets image variables to file directory
		exitGame = new Image("res/ExitButton.png");
		background = new Image("res/space background.png");
		ClassicMode = new Image("res/ClassicMode.png");
		Multiplayer = new Image("res/Multiplayer.png");
		Title = new Image("res/Title.png");
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		background.draw(0, 0);
		Title.draw(570,150);
		ClassicMode.draw(660,350);
		Multiplayer.draw(740, 600);
		exitGame.draw(880,850);
		
	}
	
	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		//Variables to hold user input
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		//if statement to check if user clicks on image and then sends to another state
		if ((xpos>660&&xpos<1295) && (ypos>675&&ypos<730)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(1);
			}
		}
		//if statement to check if user clicks on image and then sends to another state
		if ((xpos>730&&xpos<1230) && (ypos>430&&ypos<470)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(4);
			}
		}
		//if statement to check if user clicks on image and then sends to another state
		if((xpos>880&&xpos<1070) && (ypos>180&&ypos<230)){
			if(input.isMouseButtonDown(0)){
				System.exit(0);
			}
				
		}
	}
	
	//returns the id of the current state
	public int getID(){
		return 0;
	}
}
