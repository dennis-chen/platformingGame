import javax.swing.JFrame;

public class GameRunner extends JFrame{
    
        public GameRunner(){
                add(new Board());
                pack();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
                setTitle("Game!");
                setResizable(false);
                setVisible(true);
            }
        
        public static void main(String[] args){
            new GameRunner();
        }
    }
