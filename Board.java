import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{
    
    private Timer timer;
    private Player player;
    private Wall wall;
    private Enemy spike;
    private boolean ingame;
    private int levelNum;
    private int boardWidth;
    private int boardHeight;
    private ArrayList<Actor> actorList;
    private MapReader mapReader;
    private long startTime;
    private long completionTime;
    private SoundPlayer soundPlayer;
    
    public final int FINAL_LEVEL = 5;
    
    public Board(){
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        ingame = true;
        MapReader mapReader = new MapReader(this);
        levelNum = 1;
        actorList = mapReader.generateLevel(levelNum);
        //boardsize is set by the method call above
        for(Actor actor: actorList)
        {
            if(actor instanceof Player)
            {
                player = (Player) actor;
            }
        }
        startTime = System.currentTimeMillis();
        timer = new Timer(5, this);
        timer.start();
    }
    
    public void setSize(int[] boardSize)
    {
        boardWidth = boardSize[0];
        boardHeight = boardSize[1]; 
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
    }
    
    public int getWidth()
    {
        return boardWidth;
    }
    
    public int getHeight()
    {
        return boardHeight;
    }
    
    public int getLevel()
    {
        return levelNum;
    }
    
    public void setLevel(int level)
    {
        this.levelNum = level;
    }
   
    public void paint(Graphics g){
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
        for(Actor actor: actorList)
        {   if (!(actor instanceof Player))
            {
                g2d.drawImage(actor.getImage(), actor.getXInt(), actor.getYInt(), this);
            }
        }
        if(levelNum == FINAL_LEVEL)
        {
            this.displayVictoryMessage(g2d);
        }
        g2d.drawImage(player.getImage(), player.getXInt(), player.getYInt(), this);
        //draw player last to make sure player is on front layer
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void displayVictoryMessage(Graphics2D g2d)
    {
        completionTime = (completionTime == 0L) ? System.currentTimeMillis(): completionTime;
        if (soundPlayer == null)
        {
            soundPlayer = new SoundPlayer();
            soundPlayer.play("victorySong.wav");
        }
        Long elapsedTime = completionTime - startTime;
        Font font = new Font("SansSerif", Font.BOLD, 50);
        g2d.setFont(font);
        this.setForeground(Color.white);
        String victoryMessage = "Congratulations, you win!";
        String timeMessage = "Your time was: "+ elapsedTime/1000 + " seconds!";
        g2d.drawString(victoryMessage,60,200);
        g2d.drawString(timeMessage,60,400);
    }
    
    public void actionPerformed(ActionEvent e){
        
        player.tic();
        repaint();
    }
    
    public void setPlayer(Player player)
    {
        this.player = player; 
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public ArrayList<Actor> getActorList()
    {
        return actorList;
    }
    
    public void setActorList(ArrayList<Actor> actorList)
    {
        this.actorList = actorList;
    }
    
    private class TAdapter extends KeyAdapter{
        
        public void keyReleased(KeyEvent e){
            player.keyReleased(e);
        }
        
        public void keyPressed(KeyEvent e){
            player.keyPressed(e);
        }
    }
}

    
    
    
    
        
        