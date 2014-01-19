//code taken from richard lee and then modified
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.lang.Math;

public class MapReader {
    
    
    final static int TILESIZE = 28;//in pixels
     
    final static int BLUE = 0x0000ff;
    final static int YELLOWGREEN = 0x39b54a;
    final static int GREENCYAN = 0x00a99d;
    
    final static int BROWN = 0x603913; //wall
    final static int RED = 0xff0000; //spike
    final static int WHITE = 0xffffff; //blank space
    final static int GREEN = 0x00ff00; //player creation & spawn point
    final static int YELLOW = 0xfff200; //player spawn point
    final static int GREY = 0x707070; //up arrow
    final static int MAGENTA = 0xff00ff; //victory screen picture
    final static int PINK = 0xec008c; //victory screen picture 2
    final static int ORANGE = 0xf7941d; //torches
    final static int CYAN = 0x00ffff; //flag
    final static int VIOLET = 0x662d91; //bricks
    
    
    
    private  Board board;
    
    public MapReader(Board board)
    {
        this.board = board;
    }

    //loads map by reading map images and inserting blocks based on pixel color
    public ArrayList<Actor> generateLevel(int levelNum)
    {
        BufferedImage img = this.loadImage("level" + levelNum + ".png");
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w); //getRGB writes into the "pixels" array so we don't save what the method returns
        int[][] map = new int[w][h]; 
        ArrayList<Actor> levelActors = new ArrayList<Actor>();
        
        //convert 1-D array result from getRGB into a 2-D array
        for(int col = 0; col < w; col++)
        {
            for(int row = 0; row < h; row++)
            {
                int color = pixels[row*w+col];
                map[col][row] = color & 0xffffff; //no idea what the & 0xffff statement does but you dont get colors otherwise
            }
        }
        board.setSize(new int[]{w*TILESIZE,h*TILESIZE});
        //setSize of board before creating actorlist or player encounters some very nasty bugs
        for(int col = 0; col < w; col++)
        {
            for(int row = 0; row < h; row++)
            {
                switch(map[col][row])
                {
                    case GREY:
                        levelActors.add(new Actor(new int[]{col*TILESIZE,row*TILESIZE} ,board, "upArrow.png")); break;
                    case BROWN:
                        levelActors.add(new Wall(new int[]{col*TILESIZE,row*TILESIZE} ,board, "wall.png")); break;
                    case GREEN:
                        if(board.getPlayer() == null)
                        {
                            Player player = new Player(new int[]{col*TILESIZE,row*TILESIZE}, board, "player.png");
                            levelActors.add(player); 
                        }
                        else
                        {
                            board.getPlayer().setSpawnPoint(new int[]{col*TILESIZE,row*TILESIZE});
                        }
                        break;
                    case YELLOW:
                        board.getPlayer().setSpawnPoint(new int[]{col*TILESIZE,row*TILESIZE}); break;
                    case MAGENTA:
                        levelActors.add(0,new Actor(new int[]{col*TILESIZE,row*TILESIZE} ,board, "victoryBackGround.png")); break; //ensure that background image is drawn first
                    case PINK:
                        levelActors.add(0,new Actor(new int[]{col*TILESIZE,row*TILESIZE} ,board, "victoryBackGround2.png")); break; //ensure that background image is drawn first
                    case ORANGE:
                        levelActors.add(new Actor(new int[]{col*TILESIZE,row*TILESIZE} ,board, "torch.png")); break; 
                    case CYAN:
                        levelActors.add(new Actor(new int[]{col*TILESIZE,row*TILESIZE} ,board, "flag.png")); break; 
                    case RED:
                        boolean wallAbove = map[col][row - 1] == BROWN;
                        boolean wallBelow = map[col][row + 1] == BROWN;
                        boolean wallLeft = map[col - 1][row] == BROWN;
                        boolean wallRight = map[col + 1][row] == BROWN;
                        boolean spikeAbove = map[col][row - 1] == RED;
                        boolean spikeBelow = map[col][row + 1] == RED;
                        boolean spikeLeft = map[col - 1][row] == RED;
                        boolean spikeRight = map[col + 1][row] == RED;
                        String spike;
                        //find some way to refactor this monster
                        if( ( (wallBelow && wallLeft) || (wallAbove && wallBelow && wallLeft) ) && spikeRight && spikeAbove)
                        {
                            spike = "spikeCornerFaceRightUp.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if( ( (wallBelow && wallRight) || (wallAbove && wallBelow && wallRight) ) && spikeLeft && spikeAbove)
                        {
                            spike = "spikeCornerFaceLeftUp.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if((wallAbove && wallLeft) && spikeRight && spikeBelow)
                        {
                            spike = "spikeCornerFaceRightDown.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if((wallAbove && wallRight) && spikeLeft && spikeBelow)
                        {
                            spike = "spikeCornerFaceLeftDown.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if( (wallAbove && wallLeft && wallRight) || wallAbove)
                        {
                            spike = "spikeFaceDown.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if(( (wallAbove && wallBelow && wallLeft && wallRight) || (wallBelow && wallLeft && wallRight) || (wallBelow) ||(wallBelow && wallAbove) )
                                && !spikeAbove)
                        {
                            spike = "spikeFaceUp.png";
                            BufferedImage spikeImage = this.loadImage(spike);
                            int spikeOffset = TILESIZE - Math.min(spikeImage.getWidth(), spikeImage.getHeight());
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE + spikeOffset}, board, spike)); break;
                        }
                        else if(wallLeft)
                        {
                            spike = "spikeFaceRight.png";
                            
                            BufferedImage spikeImage = this.loadImage(spike);
                            
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                        else if(wallRight || (wallLeft && wallRight) )
                        {
                            spike = "spikeFaceLeft.png";
                            BufferedImage spikeImage = this.loadImage(spike);
                            int spikeOffset = TILESIZE - Math.min(spikeImage.getWidth(), spikeImage.getHeight());
                            levelActors.add(new Enemy(new int[]{col*TILESIZE + spikeOffset,row*TILESIZE}, board, spike)); break;
                        }
                        else
                        {
                            spike = "spike.png";
                            levelActors.add(new Enemy(new int[]{col*TILESIZE,row*TILESIZE}, board, spike)); break;
                        }
                    case WHITE:
                        break;
                }
            }
        }
        
        return levelActors;
    }
    
    public BufferedImage loadImage(String fileName){    
        BufferedImage img;
        
        try{
            img = ImageIO.read(this.getClass().getResource(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return img;
    }
    
}
