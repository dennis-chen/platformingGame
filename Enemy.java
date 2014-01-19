
/**
 * Enemy class is differentiated by the logic in the Player class, where if a collided object is an 
 * instanceof the enemy, player dies. probably some more elegant way to do it/make this a less redundant class?
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    /**
     * Constructor for objects of class Enemy
     */
    public Enemy(int[] startPos, Board board, String sprite_image)
    {
        super(startPos, board, sprite_image);
    }
}
