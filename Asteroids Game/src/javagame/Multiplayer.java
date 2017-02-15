package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * Multiplayer class creates two ships on screen controlled by two different users
 * The users fly around and shoot at one another until one is hit and explodes. The last
 * player remaining is the victor
 * 
 * @author Matt W
 * @version 1.0
 */

public class Multiplayer extends BasicGameState{
	
	//Deceleration of class variables
	private Image background, ship1, ship2;
	private Animation Explode, Bullet, Bullet2;
	private int[] shootdur = {200,200,200};
	private int[] ExplosiveDur = {200,200,200,200,200,200,200,200,200};
	private float RotateLeft = 4;
	private float RotateRight = -4;
	private float shipPositionX2 = 1500;
	private float shipPositionY2 = 540;
	private float shipPositionX = 420;
	private float shipPositionY = 540;
	private float BulletPositionX = shipPositionX + 27;
	private float BulletPositionY = shipPositionY + 110;
	private float BulletPositionX2 = shipPositionX2 + 28;
	private float BulletPositionY2 = shipPositionY2 +25;
	private float angle, size, width, angle2, size2, width2 = 1;
	private float BackWidth;
	private float BackLength;
	private int temp;
	private int time = 300;
	private boolean shooting, shot, quit, spam, exploded, shooting2, shot2, spam2, exploded2, GameOver = false;
	private Circle ShipRec, ShipRec2, BulletCirc, BulletCirc2;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public Multiplayer(int state){
	}

	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		//sets background to an image from file directory
		background = new Image("res/space background.png");
		ship2 = new Image("res/shipUp.png");
		ship1 = new Image("res/shipDown.png");
		
		//array of images used for animations
		Image[] Shoot = {new Image("res/Bullet1.png"), new Image("res/Bullet2.png"), new Image("res/Bullet3.png")};
		Image[] Explosion = {new Image("res/Explose1.png"), new Image("res/Explose2.png"), new Image("res/Explose3.png"), new Image("res/Explose4.png"), new Image("res/Explose5.png"), new Image("res/Explose6.png"), new Image("res/Explose7.png"), new Image("res/Explose8.png"), new Image("res/Explose9.png")};
		
		//asteroid animations initialized using image arrays and integer arrays
		Explode = new Animation(Explosion, ExplosiveDur, true);
		Explode.setLooping(false);
		
		//explosion animations initialized using image arrays and integer arrays
		Bullet = new Animation(Shoot, shootdur, true);
		Bullet2 = new Animation(Shoot, shootdur, true);
		Bullet.setLooping(false);
		Bullet2.setLooping(false);
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		//draws background
		background.draw(0, 0);
		//if statement to draw ship1 and bullets if the ship hasn't been destroyed
		if (exploded == false){
		ship1.draw(shipPositionX, shipPositionY);
		ShootDraw();}
		//if statement to draw ship2 and bullets if the ship hasn't been destroyed
		if (exploded2 == false){
		ship2.draw(shipPositionX2, shipPositionY2);
		ShootDrawTwo();}
		//checks to see if the user has hit escape key
		CheckEscape(sbg);
		//checks to see if any of the ships have intersected
		CollisonDetec(sbg);
		//if statement to check if game is over and print out which player wins
		if (GameOver == true){
			if (exploded == true){
				g.drawString("Player 1 Wins!", 900, 540);}
			else if (exploded2 == true){
				g.drawString("Player 2 Wins!", 900, 540);
			}
		}
		
	}

	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		//set temp variable to delta to make it's value accessible to all methods
		temp = delta;
		Input input = gc.getInput();
		//sends the user input to method
		UserInput(input, delta, sbg);
		//calls method to make the ships wrap around screen
		ShipBounds(shipPositionX, shipPositionY, shipPositionX2, shipPositionY2);
		//draws shapes for collision detection
		CollisionShapes();
		//starts the timer after the game has ended to send to game over state
		if (GameOver == true){
			time--;
		}
		
	}
	/**
	 * Receives user input from update method and checks with corresponding
	 * if statements
	 * @param input is the user input being received
	 * @param delta is an integer to affect speed
	 * @param sbg allows the game to switch between states
	 * @throws SlickException
	 */
	public void UserInput(Input input, int delta, StateBasedGame sbg) throws SlickException{
		
		//brings up the pause screen
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{quit = true;}
		
		//moves ship1 forward by calling method
		if(input.isKeyDown(Input.KEY_W)){
			OneMoveForward(delta);}
		
		//turns ship1 to the left
		if (input.isKeyDown(Input.KEY_A)){ 
			ship1.rotate(RotateRight);}
		
		//turns ship1 to the right
		if(input.isKeyDown(Input.KEY_D)){  
			ship1.rotate(RotateLeft);}
		
		//shoots the lazer on front of ship1
		if (input.isKeyDown(Input.KEY_SPACE)){
			if (spam == false){
				shooting = true;}
		}
		
		//moves ship2 forward
		if(input.isKeyDown(Input.KEY_UP)){
			TwoMoveForward(delta);}

		//turns ship2 to the right
		if(input.isKeyDown(Input.KEY_LEFT)){
			ship2.rotate(RotateRight);}
		
		//turns ship2 to the left
		if(input.isKeyDown(Input.KEY_RIGHT)){
			ship2.rotate(RotateLeft);}
		
		//shoots the lazer on the front of ship2
		if(input.isKeyDown(Input.KEY_ENTER)){
			if (spam2 == false){
				shooting2 = true;}
		}
		
	}
	
	/**
	 * OneMoveForward moves ship1 forward depending on its positioning
	 * @param delta is an integer to affect speed
	 */
	public void OneMoveForward(int delta){
		//gets the angle of the ship
		float angle = ship1.getRotation();
		float speed = delta * .4f ;
		//moves the ship's position according to it's speed and angle
		shipPositionX += (-1)*(speed * Math.sin(Math.toRadians(angle)));
	    shipPositionY -= (-1)*(speed * Math.cos(Math.toRadians(angle)));
		
	}
	
	/**
	 * TwoMoveForward moves ship2 forward depending on its positioning
	 * @param delta is an integer to affect speed
	 */
	public void TwoMoveForward(int delta){
		
		//gets the angle of the ship
		float angle2 = ship2.getRotation();
		float speed = delta * .4f ;
		//moves the ship's position according to it's speed and angle
		shipPositionX2 += speed * Math.sin(Math.toRadians(angle2));
	    shipPositionY2 -= speed * Math.cos(Math.toRadians(angle2));
	}
	
	/**
	 * ShipBounds checks to see if the user flies a ship off screen and
	 * wraps the ship to the opposite side.
	 * @param x is the x coordinate of ship1
	 * @param y is the y coordinate of ship1
	 * @param x2 is the x coordinate of ship2
	 * @param y2 is the y coordiante of ship2
	 */
	public void ShipBounds(float x, float y, float x2, float y2){
		//sets variables to the size of the screen
		BackWidth = background.getWidth();
		BackLength = background.getHeight();
		
		//if statements to make ships wrap around screen
		if (x > BackWidth){
			shipPositionX = -100;
		}
		if (x < -100){
			shipPositionX = BackWidth;
		}
		if (y > BackLength){
			shipPositionY = -100;
		}
		if (y < -100){
			shipPositionY = BackLength;
		}
		
		if (x2 > BackWidth){
			shipPositionX2 = -100;
		}
		if (x2 < -100){
			shipPositionX2 = BackWidth;
		}
		if (y2 > BackLength){
			shipPositionY2 = -100;
		}
		if (y2 < -100){
			shipPositionY2 = BackLength;
		}
		
	}

	/**
	 * Draws the bullet in front of ship1 and sends it to another method
	 * in order to move the bullet's position
	 */
	public void ShootDraw(){
		// if statement to allow the user to shoot only if bullet has collided or went out of bounds
		if (shooting == true){
			spam = true;
			//if statement to get necessary information for each shot
			if (shot == false){
				BulletPositionX = shipPositionX + 28;
				BulletPositionY = shipPositionY + 25;
				angle = ship1.getRotation();
				size = ship1.getHeight();
				width = ship1.getWidth();
				}
		//draws the bullet according to above information
		Bullet.draw(BulletPositionX + (-1)*((width * (float)(Math.sin(Math.toRadians(angle))))/2), BulletPositionY + ((size * (float)(Math.cos(Math.toRadians(angle))))/2));
		//method to move the bullet
		Shoot(temp, angle);
			}
		
	}
	
	/**
	 * Draws the bullet in front of ship2 and sends it to another method
	 * in order to move the bullet's position
	 */
	public void ShootDrawTwo(){
		// if statement to allow the user to shoot only if bullet has collided or went out of bounds
		if (shooting2 == true){
			spam2 = true;
			//if statement to get necessary information for each shot
			if (shot2 == false){
				BulletPositionX2 = shipPositionX2 + 28;
				BulletPositionY2 = shipPositionY2 + 25;
				angle2 = ship2.getRotation();
				size2 = ship2.getHeight();
				width2 = ship2.getWidth();
			}
		//draws the bullet according to above information
		Bullet.draw(BulletPositionX2 + ((width2 * (float)(Math.sin(Math.toRadians(angle2))))/2), BulletPositionY2 + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2));
		//method to move the bullet
		ShootTwo(temp, angle2);
		}
	}
	
	/**
	 * Shoot moves the bullet in a straight line from the front of ship1
	 * @param delta is an integer to affect speed
	 * @param angle is the angle of the ship needed for math
	 */
	public void Shoot(int delta, float angle){
		//sets shot boolean to true to prevent user from firing until this shot has finished
		shot = true;
		float speed = delta * .9f ;
		//moves the bullets position according to the speed and angle
		BulletPositionX -= speed * Math.sin(Math.toRadians(angle));
	    BulletPositionY += speed * Math.cos(Math.toRadians(angle));
	    //checks to see if the bullet has went out of bounds
	    BulletBounds(BulletPositionX, BulletPositionY);
		
	}
	
	/**
	 * ShootTwo moves the bullet in a straight line from the front of ship2
	 * @param delta is an integer to affect speed
	 * @param angle is the angle of the ship needed for math
	 */
	public void ShootTwo(int delta, float angle2){
		//sets shot boolean to true to prevent user from firing until this shot has finished
		shot2 = true;
		float speed = delta * .9f ;
		//moves the bullets position according to the speed and angle
		BulletPositionX2 += speed * Math.sin(Math.toRadians(angle2));
	    BulletPositionY2 -= speed * Math.cos(Math.toRadians(angle2));
	    //checks to see if the bullet has went out of bounds
	    BulletBoundsTwo(BulletPositionX2, BulletPositionY2);
		
	}
	
	/**
	 * BulletBounds checks the x and y position of a bullet and resets booleans that
	 * allow the user to shoot if it travels off screen
	 * @param x is the x coordinate of the current bullet
	 * @param y is the y coordinate of the current bullet
	 */
	public void BulletBounds(float x, float y){
		//if statement to check if bullet is out of bounds
		if (x > BackWidth || x < -100 || y > BackLength || y < -100){
			shot = false;
			shooting = false;
			spam = false;
			 
		}
	}
	
	/** BulletBoundsTwo checks the x and y position of a bullet and resets booleans that
	 * allow the user to shoot if it travels off screen
	 * @param x is the x coordinate of the current bullet
	 * @param y is the y coordinate of the current bullet
	 */
	public void BulletBoundsTwo(float x, float y){
		//if statement to check if bullet is out of bounds
		if (x > BackWidth || x < -100 || y > BackLength || y < -100){
			shot2 = false;
			shooting2 = false;
			spam2 = false;
			 
		}
	}
	
	/**
	 * CollisonShapes places circles around each object if it hasn't been 
	 * destroyed to be used for collision detection
	 */
	public void CollisionShapes(){
		//if statement to place circles around ship1 and it's bullet only if it hasn't been destroyed
		if (exploded == false){
			ShipRec = new Circle(shipPositionX + 34, shipPositionY + 40, 28);
			BulletCirc = new Circle(BulletPositionX + (-1)*((width * (float)(Math.sin(Math.toRadians(angle))))/2), BulletPositionY + ((size * (float)(Math.cos(Math.toRadians(angle))))/2), 9);;
		}
		//if statement to place circles around ship2 and it's bullet only if it hasn't been destroyed
		if (exploded2 == false){
		ShipRec2 = new Circle(shipPositionX2 + 34, shipPositionY2 + 34, 28);
		BulletCirc2 = new Circle(BulletPositionX2 + ((width2 * (float)(Math.sin(Math.toRadians(angle2))))/2), BulletPositionY2 + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2), 9);}
	}
	
	/**
	 * CollisionDetec uses if statements to check if any of the shapes have
	 * intersected and executes certain code if they do
	 * @param sbg allows the switching of states in the game
	 * @throws SlickException
	 */
	public void CollisonDetec(StateBasedGame sbg){
		//checks to see if the bullet of ship1 collides with ship2 and draws an explosion animation at ship2's coordinates if so
		if (BulletCirc.intersects(ShipRec2)){
			GameOver = true;
			exploded2 = true;
			Explode.draw(shipPositionX2, shipPositionY2);
		}
		
		//checks to see if the bullet of ship2 collides with ship1 and draws an explosion animation at ship1's coordinates if so
		if (BulletCirc2.intersects(ShipRec)){
			GameOver = true;
			exploded = true;
			Explode.draw(shipPositionX, shipPositionY);
		}
		
		//once the game is over and time reaches 0, the reset method is called on this state
		//then the game switches to the gameover state
		if (time == 0){
			reset();
			sbg.enterState(7);
		}
	}
	
	/**
	 * CheckEscape allows the user to enter pause screen and quit the game
	 * @param sbg allows switching between game states
	 */
	public void CheckEscape(StateBasedGame sbg){
		if(quit == true){
			sbg.enterState(5);
			quit = false;
		}
	}

	/**
	 * reset takes and sets all the variables back to their starting value
	 * and resets the map/images so it can be played again
	 */
	public void reset(){
		shipPositionX2 = 1500;
		shipPositionY2 = 540;
		shipPositionX = 420;
		shipPositionY = 540;
		BulletPositionX = shipPositionX + 27;
		BulletPositionY = shipPositionY + 110;
		BulletPositionX2 = shipPositionX2 + 28;
		BulletPositionY2 = shipPositionY2 +25;
		time = 300;
		shooting = false; 
		shot = false;
		quit = false;
		spam = false; 
		exploded = false;
		shooting2 = false;
		shot2 = false; 
		spam2 = false; 
		exploded2 = false; 
		GameOver = false;
	}
	
	//returns the id for the current state
	public int getID(){
		return 4;
	}
}