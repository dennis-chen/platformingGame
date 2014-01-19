import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Any object that is drawn by the board is an Actor (walls, spikes, player, etc.)
 * 
 * @Dennis
 * @1.4.14
 */
public class Actor
{
    private String spriteImageName;
    private double x;
    private double y;
    private int width;
    private int height;
    private boolean visible;
    private Image image;
    private Board board;

    /**
     * Constructor for objects of class Actor
     */
    public Actor(int[] startPos, Board board, String spriteImageName)
    {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(spriteImageName));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        visible = true;
        this.x = startPos[0];
        this.y = startPos[1];
        this.board = board;
    }

    /**
     * Returns x coordinate as an int
     */
    public int getXInt(){
        return (int)x;
    }
    
    public double getXDouble(){
        return x;
    }
    
    /*
     * Returns y coordinate as an int
     */
    public int getYInt(){
        return (int)y;
    }
    
    public double getYDouble(){
        return y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public void addX(double x){
        this.x+=x;
    }
    
    public void addY(double y){
        this.y+=y;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public void setImage(String imageName)
    {
        this.spriteImageName = imageName;
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imageName));
        image = ii.getImage();
    }
    
    public String getImageName(){
        return spriteImageName;
    }
    
    public Image getImage(){
        return image;
    }
    
    public Board getBoard(){
        return board;
    }
    
    public void setVisible(boolean visible){
        this.visible = visible;
    }
    
    public boolean isVisible(){
        return visible;
    }
    
    //returns hitbox of object
    public Rectangle getBounds(){
        return new Rectangle(getXInt(),getYInt(), width, height);
    }
    
    //return hitbox if it moves in a certain direction by one pixel
    public Rectangle getFutureBounds(Direction direction)
    {
        switch (direction){
            case LEFT:
                return new Rectangle(getXInt()-1,getYInt(), width, height);
            case RIGHT:
                return new Rectangle(getXInt()+1,getYInt(), width, height);
            case UP:
                return new Rectangle(getXInt(),getYInt()-1, width, height);
            case DOWN:
                return new Rectangle(getXInt(),getYInt()+1, width, height);
        }
        return null;
    }
    
    public enum Direction{
        LEFT, RIGHT, UP, DOWN;
    }
    
}
