package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * Game class creates holds the deceleration of all other states and their 
 * id's, it also adds all of the states into one game
 * 
 * @author Matt W
 * @version 1.0
 */

public class Game extends StateBasedGame{
	
	//Deceleration of class variables
	public static final String gamename = "Asteroids";
	public static final int menu = 0;
	public static final int playclassic = 1;
	public static final int helpscreen = 2;
	public static final int pause = 3;
	public static final int multi = 4;
	public static final int pause2 = 5;
	public static final int help2 = 6;
	public static final int over = 7;
	
	/**
	 * Adds all the states to the game
	 * @param gamename is the name of the game
	 */
	public Game(String gamename){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new PlayClassic(playclassic));
		this.addState(new Help(helpscreen));
		this.addState(new Pause(pause));
		this.addState(new Multiplayer(multi));
		this.addState(new Pause2(pause2));
		this.addState(new Help2(help2));
		this.addState(new GameOver(over));
	}
	
	/**
	 * Adds the states to the game container
	 */
	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(menu).init(gc, this);
		this.getState(playclassic).init(gc, this);
		this.getState(helpscreen).init(gc, this);
		this.getState(pause).init(gc, this);
		this.getState(multi).init(gc, this);
		this.getState(pause2).init(gc, this);
		this.getState(help2).init(gc, this);
		this.getState(over);
		this.enterState(menu);
	}
	
	/**
	 * Creates the GameContainer, sets it's size, and starts its
	 * 
	 */
	public static void main(String[] args) {
		AppGameContainer appgc;
		try{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setDisplayMode(1920, 1080, true); //1920,1080 
			appgc.setVSync(true);
			appgc.start();
		}
		catch(SlickException e){
			e.printStackTrace();
		}

	}

}
