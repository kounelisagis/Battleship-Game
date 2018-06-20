import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.concurrent.TimeUnit;
import javax.swing.border.*;

class myButton extends JButton
    {
        private int x, y;
        
        public myButton(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        public int[] getCoordinates()
        {
            int a[] = new int[2];
            a[0] = x;
            a[1] = y;
            
            return a;
        }
    }
    
public class GUI extends JPanel implements ActionListener{
    
    private final static int SIZE = Parameters.SIZE;
    
    private JPanel lButton, rButton, textPan;
    private myButton[][] left, right;
    private Player player;
    private Player pc;
    private AI smartPlayer;
    private JLabel resultText;
    private JTextArea jta;
    
    public GUI(Player player, Player pc, AI smartPlayer){
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        this.player = player;
        this.pc = pc;
        this.smartPlayer = smartPlayer;
        
        lButton = new JPanel();     // -> leftButtons
        lButton.setLayout(new GridLayout(SIZE,SIZE));
        rButton = new JPanel();     // -> rightButtons
        rButton.setLayout(new GridLayout(SIZE,SIZE));
        left = new myButton[SIZE][SIZE];
        right = new myButton[SIZE][SIZE];
        
        lButton.setBorder(new EmptyBorder(0, 0, 0, 20));

        
        for (int i=0; i<SIZE; i++)
            for (int j=0; j<SIZE; j++) {
                left[i][j] = new myButton(j, i);
                right[i][j] = new myButton(j, i);
                left[i][j].setMargin(new Insets(0, 0, 0, 0));
                left[i][j].setPreferredSize(new Dimension(70, 70));
                
                if(player.getBoard().getTile(i, j).getType() == TileType.Sea)
                    left[i][j].setBackground(Color.BLUE);
                else if(player.getBoard().getTile(i, j).getType() == TileType.Ship)
                    left[i][j].setBackground(Color.GRAY);
                
                right[i][j].setMargin(new Insets(0, 0, 0, 0));
                right[i][j].setPreferredSize(new Dimension(70, 70));
                right[i][j].setBackground(Color.BLUE);                
                left[i][j].setEnabled(false);
                right[i][j].addActionListener(this);
                lButton.add(left[i][j]);
                rButton.add(right[i][j]);
            }
            


        jta = new JTextArea(10,30);
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setEditable(false);
        jta.setVisible(true);
        textPan = new JPanel();
        textPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        textPan.add(new JScrollPane(jta));


         
        resultText = new JLabel("", SwingConstants.CENTER);
        resultText.setOpaque(true);
        resultText.setVisible(false);
        resultText.setBackground(Color.GREEN);
        resultText.setFont(new Font("Serif", Font.PLAIN, 25));
        
        JPanel gameTest = new JPanel();
        gameTest.setLayout(new BorderLayout());
        gameTest.add(lButton, BorderLayout.LINE_START);
        gameTest.add(rButton, BorderLayout.LINE_END);
        gameTest.add(resultText, BorderLayout.PAGE_START);
        gameTest.add(textPan, BorderLayout.PAGE_END);

        
        add(gameTest);  
    }
    
    public void disableButtons()
    {
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++)
                right[i][j].setEnabled(false);
                
        resultText.setVisible(true);
        resultText.setText(Game.isTheEnd());
    }
       

    public void actionPerformed(ActionEvent e)
    {
        myButton pressed = (myButton) e.getSource();
        
        pressed.setSelected(true);
        pressed.setEnabled(false);
            
        int target[] = pressed.getCoordinates();
            
        player.fire(pc.getBoard(), target);

        if(pc.getBoard().getTile(target[1], target[0]).getType() == TileType.Miss)
            pressed.setBackground(Color.WHITE);
        else if(pc.getBoard().getTile(target[1], target[0]).getType() == TileType.Hit)
            pressed.setBackground(Color.RED);
        
        jta.setText(Game.getInfo());
            
        if(Game.isTheEnd()!="false")
            disableButtons();
        
        else{
            target = smartPlayer.getPCmove();
            
            if(Parameters.TIMEENABLE)
            {
                try{ TimeUnit.SECONDS.sleep(Parameters.SECONDS); }
                catch(InterruptedException ex){ex.getMessage();}
            }
            
            pc.fire(player.getBoard(), target);
            
            if(player.getBoard().getTile(target[1], target[0]).getType() == TileType.Miss)
                left[target[1]][target[0]].setBackground(Color.WHITE);
            else if(player.getBoard().getTile(target[1], target[0]).getType() == TileType.Hit)
                left[target[1]][target[0]].setBackground(Color.RED);
            
        }
        
        jta.setText(Game.getInfo());
        
        if(Game.isTheEnd()!="false")
            disableButtons();

    }
  
}
