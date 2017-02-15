package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * Pause2 class creates the pause screen for PlayClassic that allows
 * the user to return to main menu or travel to the help state
 * 
 * @author Matt W
 * @version 1.0
 */


public class Pause extends BasicGameState{
	//Deceleration of class variables
	private Image background;
	private Image Resume;
	private Image BackToMain;
	private Image Help;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public Pause(int state){
	}

	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		background = new Image("res/space background.png");
		Resume = new Image("res/ResumeButton.png");
		BackToMain = new Image("res/BackToMainMenu.png");
		Help = new Image("res/HelpButton.png");
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		background.draw(0, 0);
		Resume.draw(860,300);
		Help.draw(925,500);
		BackToMain.draw(650,700);
		
	}
	
	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		//quit button
				if((xpos>650&&xpos<1360) && (ypos>340&&ypos<380)){
					if(input.isMouseButtonDown(0)){
						sbg.enterState(0);
					}
				}
				//help menu
				if ((xpos>925&&xpos<1075)&&(ypos>540&&ypos<580)){
					if(input.isMouseButtonDown(0)){
						sbg.enterState(2);
					}
				}	
				//resume click
				if((xpos>860&&xpos<1140)&&(ypos>740&&ypos<770)){
					if(input.isMouseButtonDown(0)){
						sbg.enterState(1);
					}
				}
	}
	
	//returns the id of the current state
	public int getID(){
		return 3;
	}
}