import java.util.ArrayList;
import java.awt.event.KeyEvent;

/**
 * Player in game
 * 
 * @author Dennis 
 * @version 1.4.14
 */
public class Player extends Actor
{
    private boolean hasJumped;
    private boolean hasDoubleJumped;
    private boolean onGround;
    private double dx;
    private double dy;
    private ArrayList<Actor> actorList; //make this so there's only one variable per class
    public final double MaxFallSpeed = 3.5;
    private int[] spawnPoint;
    private SoundPlayer soundPlayer;
    //private <Actor> ArrayList Actors;
    /**
     * Constructor for objects of class Player
     */
    public Player(int[] startPos, Board board, String sprite_image)
    {
        super(startPos, board, sprite_image);
        hasJumped = true;
        dx = 0;
        dy = 0;
        spawnPoint = startPos;
        soundPlayer = new SoundPlayer();
    }
    
    public void tic(){
        this.checkDeath();
        this.checkNextLevel();
        this.move();
        this.chooseImage();
    }
    
    public void move(){
        onGround = false; 
        //increment dy to account for gravity, until character reaches max fall speed
        if (dy < MaxFallSpeed){dy+=.06;}
        //move appropriate dx and dy as long as it doesn't collide
        if (dx > 0){
            for(int i = 0; i < Math.abs(dx); i++){
                if(!willCollide(Direction.RIGHT)){this.addX(1);}
            }}
        else if(dx < 0){
            for(int i = 0; i < Math.abs(dx); i++){
                if(!willCollide(Direction.LEFT)){this.addX(-1);}
            }
        }
        if (dy > 0){
            for(int i = 0; i < Math.abs(dy); i++){
                if(!willCollide(Direction.DOWN)){this.addY(1);}
                else{hasJumped = false;
                     hasDoubleJumped = false;
                     onGround = true;} //player has landed, allow them to jump 
            }}
        else if(dy < 0){
            for(int i = 0; i < Math.abs(dy); i++){
                if(!willCollide(Direction.UP)){this.addY(-1);}
                else
                {
                    dy = 0;//if player collides with an object while jumping, have them 
                    //start falling immediately
                    
                } 
            }
        }
        
    }
    
    public String getOnGround()
    {
        if (onGround)
        {
            return "true";
        }
        return "false";
    }
    
    public void keyPressed(KeyEvent e){
        
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            dx = -1.6;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1.6;
        }

        if (key == KeyEvent.VK_UP) {
            if(!hasJumped && onGround)//can only jump if on ground, but can double jump after running off an edge
            {
                hasJumped = true;
                dy = -2.5;
                soundPlayer.play("jumpSound.wav");
            }
            else if(!hasDoubleJumped)
            {
                hasDoubleJumped = true;
                dy = -2.5;
                soundPlayer.play("jumpSound.wav");
            }
        }
        
        if (key == KeyEvent.VK_DOWN) {
            dy+=.2;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
        }
        
        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
    
    public void checkDeath()
    {
        if (hitEnemy())
        {
            this.setVisible(false);
            this.setX(spawnPoint[0]);
            this.setY(spawnPoint[1]);
            dx = 0;
            dy = 0;
            this.setVisible(true);
            soundPlayer.play("deathSound.wav");
        }
    }
    
    public void checkNextLevel()
    {
        Board myBoard = this.getBoard();
        MapReader mapReader = new MapReader(myBoard);
        //if player has gone halfway offscreen, switch to the next or previous levels
         if(this.getXDouble() > this.getBoard().getWidth() - this.getWidth()/2 ){
             ArrayList<Actor> nextLevelActors = mapReader.generateLevel( myBoard.getLevel() + 1 );
             nextLevelActors.add(this);
             myBoard.setPlayer(this);
             myBoard.setActorList(nextLevelActors); //assume going offscreen on the right means next level
             this.actorList = nextLevelActors;
             myBoard.setLevel(myBoard.getLevel() + 1);
             this.setX(-this.getWidth()/2);
        }
        else if(this.getXDouble() < -this.getWidth()/2){
            ArrayList<Actor> previousLevelActors = mapReader.generateLevel( myBoard.getLevel() - 1 );
            previousLevelActors.add(this);
            myBoard.setPlayer(this);
            myBoard.setActorList(previousLevelActors); //assume going offscreen on the left means previous level
            this.actorList = previousLevelActors;
            myBoard.setLevel(myBoard.getLevel() - 1);
            this.setX(this.getBoard().getWidth() - this.getWidth()/2);
        }
        else if(this.getYDouble() > this.getBoard().getHeight() - this.getHeight()/2){
            ArrayList<Actor> nextLevelActors = mapReader.generateLevel( myBoard.getLevel() + 1 );
            nextLevelActors.add(this);
            myBoard.setPlayer(this);
            myBoard.setActorList(nextLevelActors); //assume going offscreen on the bottom means next level
            this.actorList = nextLevelActors;
            myBoard.setLevel(myBoard.getLevel() + 1);
            this.setY(-this.getHeight()/2);
        }
        else if(this.getYDouble() < -this.getHeight()/2){
            ArrayList<Actor> previousLevelActors = mapReader.generateLevel( myBoard.getLevel() - 1 );
            previousLevelActors.add(this);
            myBoard.setPlayer(this);
            myBoard.setActorList(previousLevelActors); //assume going offscreen on the top means previous level
            this.actorList = previousLevelActors;
            myBoard.setLevel(myBoard.getLevel() - 1);
            this.setY(this.getBoard().getHeight() - this.getHeight()/2);
        }
    }
    
    public void setSpawnPoint(int[] spawnPoint)
    {
        this.spawnPoint = spawnPoint; 
    }
    
    public boolean willCollide(Direction direction)
    {
        if (actorList == null)
        {
            actorList = this.getBoard().getActorList();
        }
        for (Actor actor: actorList)
        {
            if (actor instanceof Wall)
            {   
                Wall wall = (Wall) actor;
                if(this.getFutureBounds(direction).intersects(wall.getBounds()))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hitEnemy()
    {
        if (actorList == null)
        {
            actorList = this.getBoard().getActorList();
        }
        for (Actor actor: actorList)
        {
            if (actor instanceof Enemy)
            {   
                Enemy enemy = (Enemy) actor;
                if(this.getBounds().intersects(enemy.getBounds()))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void chooseImage()
    {
        if(dx > 0 && dy > 0 && !onGround)
        {
            this.setImage("playerRightDown.png");
        }
        else if(dx > 0 && dy > 0 && onGround)
        {
            if (this.getImageName().equals("playerRight1.png"))
            {
                this.setImage("playerRight2.png");
            }
            else
            {
                this.setImage("playerRight1.png");
            }
        }
        else if(dx > 0 && dy < 0)
        {
            this.setImage("playerRightUp.png");
        }
        else if(dx == 0 && dy > 0 && !onGround)
        {
            this.setImage("playerDown.png");
        }
        else if(dx == 0 && dy == 0 && onGround)
        {
            this.setImage("player.png");
        }
        else if(dx == 0 && dy < 0)
        {
            this.setImage("playerUp.png");
        }
        else if(dx < 0 && dy > 0 && !onGround)
        {
            this.setImage("playerLeftDown.png");
        }
        else if(dx < 0 && dy > 0 && onGround)
        {
            if (this.getImageName().equals("playerLeft1.png"))
            {
                this.setImage("playerLeft2.png");
            }
            else
            {
                this.setImage("playerLeft1.png");
            }
        }
        else if(dx < 0 && dy < 0)
        {
            this.setImage("playerLeftUp.png");
        }
        else
        {
            this.setImage("player.png");
        }
    }
    
}
