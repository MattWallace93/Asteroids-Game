package javagame;

import java.awt.Font;
import java.awt.Shape;
import java.util.Random;
import java.util.Timer;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.*;

/**
 * Images courtesy of http://opengameart.org
 * 
 * PlayClassic class creates a ship on screen and allows the user to move it using certain input.
 * The user has to move around dodging asteroids and can shoot and destroy them with a lazer located
 * on the front of the ship. Once the player gets hit by an asteroid the game is over.
 * 
 * @author Matt W
 * @version 1.0
 */

public class PlayClassic extends BasicGameState{
	
	//Deceleration of class variables
	private Image background, ship;
	private Animation Explode, Bullet, Asteroid, Asteroid1, Asteroid2, Asteroid3, Asteroid4, Asteroid5, Asteroid6, Asteroid7;
	private Random ran = new Random();
	private int[] shootdur = {200,200,200};
	private int[] Astduration = {200,200,200,200,200,200,200,200};
	private int[] ExplosiveDur = {200,200,200,200,200,200,200,200,200};
	private float shipPositionX = 960;
	private float shipPositionY = 540;
	private float AstX = ran.nextInt(500) - 1000;
	private float AstY = ran.nextInt(500) - 1000;
	private float AstX2 = ran.nextInt(500) - 1000;
	private float AstY2 = ran.nextInt(500) - 1000;
	private float AstX3 = ran.nextInt(2000);
	private float AstY3 = 0;
	private float AstX4 = ran.nextInt(2000);
	private float AstY4 = 0;
	private float RevAstX = ran.nextInt(500) + 2000;
	private float RevAstY = ran.nextInt(500) - 1000;
	private float RevAstX2 = ran.nextInt(500) + 2000;
	private float RevAstY2 = ran.nextInt(500) - 1000;
	private float RevAstX3 = 2000;
	private float RevAstY3 = ran.nextInt(1080);
	private float RevAstX4 = 2000;
	private float RevAstY4 = ran.nextInt(1080);
	private float AstDx = 2;
	private float AstDy = 2;
	private float AstDxDown = 0;
	private float AstDyCross = 0;
	private float AstRevDx = -2;
	private float AstRevDy = 2;
	private float RotateLeft = 4;
	private float RotateRight = -4;
	private float BackWidth;
	private float BackLength;
	private float BulletPositionX = shipPositionX + 28;
	private float BulletPositionY = shipPositionY + 25;
	private float angle2 = 1;
	private float size2 = 1;
	private float width2 = 1;
	private boolean shooting, shot, quit, spam, exploded, GameOver, A1, A2, A3, A4, A5, A6, A7, A8 = false;
	private int temp;
	private int count = 10000;
	private int time = 300;
	private int AstCheck = 400;
	private Circle ShipRec, BulletCirc, AstRec, AstRec2, AstRec3, AstRec4, AstRec5, AstRec6, AstRec7, AstRec8, c;
	private int Score = 0;
	
	/**
	 * Constructor to create features of the state
	 * @param state is the state entered from main state
	 */
	public PlayClassic(int state){
	}
	
	/**
	 * Holds all of the images and animations features of the state
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//sets background to an image from file directory
		background = new Image("res/space background.png");
		ship = new Image("res/shipUp.png");
		
		//array of images used for animations
		Image[] asteroids = {new Image("res/Ast1.png"), new Image("res/Ast2.png"), new Image("res/Ast3.png"), new Image("res/Ast4.png"), new Image("res/Ast5.png"), new Image("res/Ast6.png"), new Image("res/Ast7.png"), new Image("res/Ast8.png")};
		Image[] Shoot = {new Image("res/Bullet1.png"), new Image("res/Bullet2.png"), new Image("res/Bullet3.png")};
		Image[] Explosion = {new Image("res/Explose1.png"), new Image("res/Explose2.png"), new Image("res/Explose3.png"), new Image("res/Explose4.png"), new Image("res/Explose5.png"), new Image("res/Explose6.png"), new Image("res/Explose7.png"), new Image("res/Explose8.png"), new Image("res/Explose9.png")};
		
		//asteroid animations initialized using image arrays and integer arrays
		Asteroid = new Animation(asteroids,Astduration, true);
		Asteroid1 = new Animation(asteroids,Astduration, true);
		Asteroid2 = new Animation(asteroids,Astduration, true);
		Asteroid3 = new Animation(asteroids,Astduration, true);
		Asteroid4 = new Animation(asteroids,Astduration, true);
		Asteroid5 = new Animation(asteroids,Astduration, true);
		Asteroid6 = new Animation(asteroids,Astduration, true);
		Asteroid7 = new Animation(asteroids,Astduration, true);
		
		//explosion animations initialized using image arrays and integer arrays
		Explode = new Animation(Explosion, ExplosiveDur, true);
		Explode.setLooping(false);
		
		//bullet animations initialized using image arrays and integer arrays
		Bullet = new Animation(Shoot, shootdur, true);
		Bullet.setLooping(false);
		
	}
	
	/**
	 * draws the pictures and animations on the screen
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		//draws the background
		background.draw(0, 0);
		//calls method to draw asteroids
		AsteroidDraw();
		//if statement to draw ship and bullets if the ship hasn't been destroyed
		if (exploded == false){
		ship.draw(shipPositionX, shipPositionY);
		ShootDraw();}
		//draws score on the bottom left of screen
		g.drawString("Score: " + Score, 20, 1050);
		//checks to see if the user has hit escape key
		CheckEscape(sbg);
		//checks to see if any of the objects have intersected
		CollisonDetec(sbg);
		//redraws asteroids every 4 seconds if they've been destroyed
		if (AstCheck == 0){
			RedrawAst();
		}
		//if game is over then prints out the final score
		if (GameOver == true){
			g.drawString("You Scored: " + Score, 900, 540);
		}
	}
	
	/**
	 * update is constantly running the methods and incrementing/decrementing counters inside of it
	 */
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException{
		//set temp variable to delta to make it's value accessible to all methods
		temp = delta;
		Input input = gc.getInput();
		//calls method to make asteroids move
		AstMove(AstX, AstY, AstX2, AstY2, AstX3, AstY3, AstX4, AstY4, RevAstX, RevAstY, RevAstX2, RevAstY2, RevAstX3, RevAstY3, RevAstX4, RevAstY4);
		//calls method to keep asteroids from flying out to nowhere
		AstBounds(AstY, AstY2, AstY3, AstY4, RevAstY, RevAstY2, RevAstX3, RevAstX4);
		//calls method to make the ship wrap around screen
		ShipBounds(shipPositionX, shipPositionY);
		//sends the user input to method
		UserInput(input, delta, sbg);
		//timer to respawn destroyed asteroids
		AstCheck--;
		//draws shapes for collision detection
		CollisionShapes();
		//starts the timer after the game has ended to send to game over state
		if (GameOver == true){
			time--;
		}
		//increases the score while player is alive
		if (GameOver == false){
		Score++;
		}
		count--;
		
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
		
		//moves the ship forward by calling method
		if(input.isKeyDown(Input.KEY_W)){
			MoveForward(delta);}
		
		//turns the ship to the left
		if (input.isKeyDown(Input.KEY_A)){ 
			ship.rotate(RotateRight);}
		
		//turns the ship to the right
		if(input.isKeyDown(Input.KEY_D)){  
			ship.rotate(RotateLeft);}
		
		//brings up the pause screen
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{quit = true;}
		
		//shoots the lazer on front of ship
		if (input.isKeyDown(Input.KEY_SPACE)){
			if (spam == false){
			shooting = true;}
		}
		
	}
	
	/**
	 * MoveForward moves the ship forward depending on its positioning
	 * @param delta is an integer to affect speed
	 */
	public void MoveForward(int delta){
		//gets the angle and speed of the ship
		float angle = ship.getRotation();
		float speed = delta * .4f ;
		//moves the ship's position according to it's speed and angle
		shipPositionX += speed * Math.sin(Math.toRadians(angle));
	    shipPositionY -= speed * Math.cos(Math.toRadians(angle));
		
	}
	
	/**
	 * Draws the bullet in front of the ship and sends it to another method
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
				angle2 = ship.getRotation();
				size2 = ship.getHeight();
				width2 = ship.getWidth();
				}
		//draws the bullet according to above information
		Bullet.draw(BulletPositionX + ((width2 * (float)(Math.sin(Math.toRadians(angle2))))/2), BulletPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2));
		//method to move the bullet
		Shoot(temp, angle2);
			}
		
	}
	
	/**
	 * Shoot moves the bullet in a straight line from the front of the ship
	 * @param delta is an integer to affect speed
	 * @param angle is the angle of the ship needed for math
	 */
	public void Shoot(int delta, float angle){
		//sets shot boolean to true to prevent user from firing until this shot has finished
		shot = true;
		float speed = delta * .9f ;
		//moves the bullets position according to the speed and angle
		BulletPositionX += speed * Math.sin(Math.toRadians(angle));
	    BulletPositionY -= speed * Math.cos(Math.toRadians(angle));
	    //checks to see if the bullet has went out of bounds
	    BulletBounds(BulletPositionX, BulletPositionY);
		
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
	
	/**
	 * ShipBounds checks to see if the user flies a ship off screen and
	 * wraps the ship to the opposite side.
	 * @param x is the x coordinate of the ship
	 * @param y is the y coordinate of the ship
	 */
	public void ShipBounds(float x, float y){
		//sets variables to the size of the screen
		BackWidth = background.getWidth();
		BackLength = background.getHeight();
		
		//if statements to ship wrap around screen
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
	}

	/**
	 * AsteroidDraw draws the asteroid animations on the screen if they haven't been destroyed
	 */
	public void AsteroidDraw(){
		//if statements to draw asteroids only if they're not destroyed
		if (A1 == false){
		Asteroid.draw(AstX, AstY);
		}
		
		if (A2 == false){
		Asteroid1.draw(AstX2, AstY2);
		}
		
		if (A3 == false){
		Asteroid2.draw(AstX3, AstY3);
		}
		
		if (A4 == false){
		Asteroid3.draw(AstX4, AstY4);
		}
		
		if (A5 == false){
		Asteroid4.draw(RevAstX, RevAstY);
		}
		
		if (A6 == false){
		Asteroid5.draw(RevAstX2, RevAstY2);
		}
		
		if (A7 == false){
		Asteroid6.draw(RevAstX3, RevAstY3);
		}
		
		if (A8 == false){
		Asteroid7.draw(RevAstX4, RevAstY4);
		}	
	}
	
	/**
	 * RedrawAst resets the boolean values associated with each asteroid to false after
	 * a certain amount of time after destruction in order to keep constant asteroids in game
	 */
	public void RedrawAst(){
		
			A1 = false;
			A2 = false;
			A3 = false;
			A4 = false;
			A5 = false; 
			A6 = false;
			A7 = false; 
			A8 = false;
		
			//resets the timer to redraw every time method is ran
		AstCheck = 400;
	}
	
	/**
	 * AstBounds keeps the asteroids from flying infinitely in one direction and replaces them
	 * on the opposite side of the screen once they hit out of bounds
	 * @param y is the y coordinate for the first asteroid
	 * @param y2 is the y coordinate for the second asteroid
	 * @param y3 is the y coordinate for the third asteroid
	 * @param y4 is the y coordinate for the fourth asteroid
	 * @param y5 is the y coordinate for the fifth asteroid
	 * @param y6 is the y coordinate for the sixth asteroid
	 * @param y7 is the y coordinate for the seventh asteroid
	 * @param y8 is the y coordinate for the eighth asteroid
	 */
	public void AstBounds(float y, float y2, float y3, float y4, float y5, float y6, float y7, float y8){
		
		//if statements to reset the x and y of an asteroid if it flies off screen
		if (y>1080){
			AstX = (ran.nextInt(500)-1000);
			AstY = (ran.nextInt(500)-1000);
		}
		if (y2>1080){
			AstX2 = ran.nextInt(500)-1000;
			AstY2 = ran.nextInt(500)-1000;
		}
		if (y3>1080){
			AstX3 = ran.nextInt(2000);
			AstY3 = 0;
		}
		if (y4>1080){
			AstX4 = ran.nextInt(2000);
			AstY4 = 0;
		}
		if (y5>1080){
			RevAstX = ran.nextInt(500)+2000;
			RevAstY = ran.nextInt(500)-1000;	
		}
		if (y6>1080){
			RevAstX2 = ran.nextInt(500)+2000;
			RevAstY2 = ran.nextInt(500)-1000;	
		}
		if (y7<0){
			RevAstX3 = 2000;
			RevAstY3 = ran.nextInt(1080);	
		}
		if (y8<0){
			RevAstX4 = 2000;
			RevAstY4 = ran.nextInt(1080);	
		}
	}
	
	/**
	 * AstMove takes the x and y for each asteroid and increments them by a speed variable
	 * which causes the asteroids to move in a straight line
	 * @param x is the x coordinate for the first asteroid
	 * @param y is the y coordinate for the first asteroid
	 * @param x2 is the x coordinate for the second asteroid
	 * @param y2 is the y coordinate for the second asteroid
	 * @param x3 is the x coordinate for the third asteroid
	 * @param y3 is the y coordinate for the third asteroid
	 * @param x4 is the x coordinate for the fourth asteroid
	 * @param y4 is the y coordinate for the fourth asteroid
	 * @param Revx is the x coordinate for the fifth asteroid
	 * @param Revy is the y coordinate for the fifth asteroid
	 * @param Revx2 is the x coordinate for the sixth asteroid
	 * @param Revy2 is the y coordinate for the sixth asteroid
	 * @param Revx3 is the x coordinate for the seventh asteroid
	 * @param Revy3 is the y coordinate for the seventh asteroid
	 * @param Revx4 is the x coordinate for the eighth asteroid
	 * @param Revy4 is the y coordinate for the eighth asteroid
	 */
	public void AstMove(float x, float y, float x2, float y2,float x3, float y3, float x4, float y4, float Revx, float Revy, float Revx2, float Revy2, float Revx3, float Revy3, float Revx4, float Revy4){
		//if statements to increment the position of each asteroid if it hasn't been destroyed
		if (A1 == false){
			AstX = x + (AstDx);
			AstY = y + (AstDy);}
		if (A2 == false){
			AstX2 = x2 + (AstDx);
			AstY2 = y2 + (AstDy);}
		if (A3 == false){
			AstX3 = x3 + (AstDxDown);
			AstY3 = y3 + (AstDy);}
		if (A4 == false){
			AstX4 = x4 + (AstDxDown);
			AstY4 = y4 + (AstDy);}
		if (A5 == false){
			RevAstX = Revx + (AstRevDx);
			RevAstY = Revy + (AstRevDy);}
		if (A6 == false){
			RevAstX2 = Revx2 + (AstRevDx);
			RevAstY2 = Revy2 + (AstRevDy);}
		if (A7 == false){
			RevAstX3 = Revx3 + (AstRevDx);
			RevAstY3 = Revy3 + (AstDyCross);}
		if (A8 == false){
			RevAstX4 = Revx4 + (AstRevDx);
			RevAstY4 = Revy4 + (AstDyCross);}
		
		//method that increases the speed of asteroids after a certain amount of time
		IncreaseSpeed();
	}
	
	/**
	 * IncreaseSpeed updates the speed variable of the asteroids after
	 * the counter hits a certain point
	 */
	public void IncreaseSpeed(){
		//if statement to increment the speed variables at certain intervals
		if (count == 8500 || count == 6500 || count == 4000 || count == 2000){
			AstDx += 3;
			AstDy += 3;
			AstRevDx -= 3;
			AstRevDy += 3;
		}
	}
	
	/**
	 * CollisonShapes places circles around each object if it hasn't been 
	 * destroyed to be used for collision detection
	 */
	public void CollisionShapes(){
		//if statement to place circles around the ship and it's bullet only if it hasn't been destroyed
		if (exploded == false){
		ShipRec = new Circle(shipPositionX + 34, shipPositionY + 34, 28);
		BulletCirc = new Circle(BulletPositionX, BulletPositionY, 9);}
		
		//if statement to place circles around asteroids only if they haven't been destroyed
		if (A1 == false){
		AstRec = new Circle(AstX + 12, AstY + 12, 15);}
		
		if (A2 == false){
		AstRec2 = new Circle(AstX2 + 12 , AstY2 + 12, 15);}
		
		if (A3 == false){
		AstRec3 = new Circle(AstX3 + 12, AstY3 + 12, 15);}
		
		if (A4 == false){
		AstRec4 = new Circle(AstX4 + 12, AstY4 + 12, 15);}
		
		if (A5 == false){
		AstRec5 = new Circle(RevAstX + 12, RevAstY + 12, 15);}
		
		if (A6 == false){
		AstRec6 = new Circle(RevAstX2 + 12, RevAstY2 + 12, 15);}
		
		if (A7 == false){
		AstRec7 = new Circle(RevAstX3 + 12, RevAstY3 + 12, 15);}
		
		if (A8 == false){
		AstRec8 = new Circle(RevAstX4 + 12, RevAstY4 + 12, 15);}
	}
	
	/**
	 * CollisionDetec uses if statements to check if any of the shapes have
	 * intersected and executes certain code if they do
	 * @param sbg allows the switching of states in the game
	 * @throws SlickException
	 */
	public void CollisonDetec(StateBasedGame sbg) throws SlickException{
		
		// checks to see if ship collides with asteroids and draws explosion animation at it's coordinates if so
		if (ShipRec.intersects(AstRec) || ShipRec.intersects(AstRec2) || ShipRec.intersects(AstRec3) || ShipRec.intersects(AstRec4) || ShipRec.intersects(AstRec5) || ShipRec.intersects(AstRec6) || ShipRec.intersects(AstRec7) || ShipRec.intersects(AstRec8)){
			GameOver = true;
			exploded = true;
			Explode.draw(shipPositionX, shipPositionY);
			
		}
		
		//if statements to check to see if the bullet collides with any asteroids
		//draws explosion animations if so and randomly resets the asteroids x and y to its starting side
		//also resets the bullet position to the front of ship, allowing it to be shot again
		//increments the score by 1000 for each asteroid shot
		if (BulletCirc.intersects(AstRec)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec.setLocation(5000, 5000);
			AstX = (ran.nextInt(500)-1000);
			AstY = (ran.nextInt(500)-1000);
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A1 = true;
			Score += 1000;
			}
			
		if  (BulletCirc.intersects(AstRec2)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec2.setLocation(5000, 5000);
			AstX2 = ran.nextInt(500) - 1000;
			AstY2 = ran.nextInt(500) - 1000;
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A2 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec3)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec3.setLocation(5000, 5000);
			AstX3 = ran.nextInt(2000);
			AstY3 = 0;
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A3 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec4)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec4.setLocation(5000, 5000);
			AstX4 = ran.nextInt(2000);
			AstY4 = 0;
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A4 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec5)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec5.setLocation(5000, 5000);
			RevAstX = ran.nextInt(500)+2000;
			RevAstY = ran.nextInt(500)-1000;
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A5 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec6)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec6.setLocation(5000, 5000);
			RevAstX2 = ran.nextInt(500)+2000;
			RevAstY2 = ran.nextInt(500)-1000;	
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A6 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec7)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec7.setLocation(5000, 5000);
			RevAstX3 = 2000;
			RevAstY3 = ran.nextInt(1080);
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A7 = true;
			Score += 1000;
			}
		
		if (BulletCirc.intersects(AstRec8)){
			Explode.draw(BulletPositionX, BulletPositionY);
			AstRec8.setLocation(5000, 5000);
			RevAstX4 = 2000;
			RevAstY4 = ran.nextInt(1080);	
			BulletPositionX = shipPositionX + (width2 * (float)(Math.sin(Math.toRadians(angle2))))/2;
			BulletPositionY = shipPositionY + (-1)*((size2 * (float)(Math.cos(Math.toRadians(angle2))))/2) + 35;
			shot = false;
			shooting = false;
			spam = false;
			A8 = true;
			Score += 1000;
			}

		//once the game is over and time reaches 0, the reset method is called on this state
		//then the game switches to the gameover state
		if (time < 0){
			reset();
			sbg.enterState(7);
		}
	}
	/**
	 * reset takes and sets all the variables back to their starting value
	 * and resets the map/images so it can be played again
	 */
	private void reset() {
		
		shipPositionX = 960;
		shipPositionY = 540;
		AstX = ran.nextInt(500) - 1000;
		AstY = ran.nextInt(500) - 1000;
		AstX2 = ran.nextInt(500) - 1000;
		AstY2 = ran.nextInt(500) - 1000;
		AstX3 = ran.nextInt(2000);
		AstY3 = 0;
		AstX4 = ran.nextInt(2000);
		AstY4 = 0;
		RevAstX = ran.nextInt(500) + 2000;
		RevAstY = ran.nextInt(500) - 1000;
		RevAstX2 = ran.nextInt(500) + 2000;
		RevAstY2 = ran.nextInt(500) - 1000;
		RevAstX3 = 2000;
		RevAstY3 = ran.nextInt(1080);
		RevAstX4 = 2000;
		RevAstY4 = ran.nextInt(1080);
		AstDx = 2;
		AstDy = 2;
		AstDxDown = 0;
		AstDyCross = 0;
		AstRevDx = -2;
		AstRevDy = 2;
		BulletPositionX = shipPositionX + 28;
		BulletPositionY = shipPositionY + 25;		
		shooting = false;
		shot = false;
		quit = false;
		spam = false;
		exploded = false;
		GameOver = false;
		count = 10000;
		time = 300;
		Score = 0;
	}

	/**
	 * CheckEscape allows the user to enter pause screen and quit the game
	 * @param sbg allows switching between game states
	 */
	public void CheckEscape(StateBasedGame sbg){
		if(quit == true){
			reset();
			sbg.enterState(3);
			quit = false;
		}
	}
	
	//returns the id for the current state
	public int getID(){
		return 1;
	}
}
