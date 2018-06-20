import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.*;
import javax.swing.*;

public class Game
{
    private static final int SIZE = Parameters.SIZE;
    
    private static Scanner in;
    private static Player player, pc;
    private static Board pcBoard;
    private static Board playerBoard;
    private static AI smartPlayer;
    
    public static void main(String[] args)
    {
        player = new Player("Player", false);
        pc = new Player("PC", true); // true -> hide ships
        
        playerBoard = player.getBoard();
        pcBoard = pc.getBoard();
        
        smartPlayer = new AI(playerBoard);
        
        pc.placeAllShips();
        
        if(Parameters.GRAPHICS)
        {
            player.placeAllShips();
            JFrame frame = new JFrame("Battleship");
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GUI td = new GUI(player, pc, smartPlayer);
            frame.add(td);
            frame.pack();   //sizes the frame so that all its contents are at their preferred sizes
        }

        else
        {
            try{
                in = new Scanner(System.in);    // THROWS EXCEPTION
                placePlayerShips();
                gameLoop();
                printResults();
                in.close();
            }
            catch(Exception e){ System.err.println(e); e.printStackTrace(System.err); }
        }
    }
    
    
    
    //CUSTOM-MADE
    public static void gameLoop()
    {
        System.out.println("_______________________________________________________________________________");
        System.out.println("TIME FOR SOME FIRE!");
            
        while( !playerBoard.allShipsSunk() && !pcBoard.allShipsSunk()
            && (!Parameters.MOVESCHECK || player.getFires() < Parameters.MAXMOVES) )
        {
            int[] target = new int[2];   //0-> x, 1-> y
            
            //PLAYER's TURN
            do{
                System.out.println("Give collumn and row to hit");
                target[0] = in.nextInt();
                target[1] = in.nextInt();
            }while(target[0]<0 || target[0]>SIZE-1 || target[1]<0 || target[1]>SIZE-1);
            
            player.fire(pc.getBoard(), target);
            Board.drawboards(player.getBoard(), pc.getBoard());
            
            System.out.println("====================================================================================");
            
            if(pc.getBoard().allShipsSunk())    // EXIT WHEN PLAYER WINS
                break;
            
            
            //PC's TURN
            target = smartPlayer.getPCmove();
            
            if(Parameters.TIMEENABLE)
            {
                try{ TimeUnit.SECONDS.sleep(Parameters.SECONDS); }
                catch(InterruptedException ex){}
            }
            
            pc.fire(playerBoard, target);
            Board.drawboards(player.getBoard(), pc.getBoard());
        }
    }
    
    
    
    
    
    //Place all ships
    public static void placePlayerShips()
    {
        System.out.println("Do you want to place your ships randomly? Yes or No?");
        
        if(randomPlace())
        {
            player.placeAllShips();
            Board.drawboards(playerBoard, pcBoard);
        }
        
        else
        {
            Ship[] ships = new Ship[5];
            
            ships[0] = new Carrier();
            ships[1] = new Battleship();
            ships[2] = new Cruiser();
            ships[3] = new Submarine();
            ships[4] = new Destroyer();
            
            int[] coordinates;
            Orientation orientation;
            
            for(int i = 0; i<5; i++)
            {
                do{
                    System.out.println("For the ship " + (i+1) + " with size " + ships[i].getSize());
                    coordinates = getInput();
                    orientation = getOrientation();
                }while(!player.placeShip(ships[i], orientation, coordinates));
                
                Board.drawboards(playerBoard, pcBoard);
            }
        }
    }
        
    
    public static void printResults()
    {
        if(player.getBoard().allShipsSunk())
            System.out.println("\n-- THE WINNER IS PC! --");
                
        else if(pc.getBoard().allShipsSunk())
            System.out.println("\n-- THE WINNER IS PLAYER! --");
            
        else
            System.out.println("\n-- TIE --");
        
        System.out.println("\nSTATISTICS:");
        System.out.println("->PLAYER");
        player.getStats();
        System.out.println("\n->PC");
        pc.getStats();
    }
    
    
    public static int[] getInput()
    {
        int[] coordinates = new int[2]; //0->x  1->y
        
        System.out.println("Give collumn and row:");
        coordinates[0] = in.nextInt();
        coordinates[1] = in.nextInt();
        
        while(coordinates[0]<0 || coordinates[0]>SIZE-1 || coordinates[1]<0 || coordinates[1]>SIZE-1)
        {
            System.out.println("Give collumn and row AGAIN:");
            coordinates[0] = in.nextInt();
            coordinates[1] = in.nextInt();
        }
        
        return coordinates;
    }
    
    public static int[] getRandInput()
    {
        int[] coordinates = new int[2]; //0->x  1->y
        
        Random r = new Random();
        coordinates[0] = r.nextInt(SIZE);
        coordinates[1] = r.nextInt(SIZE);
        
        return coordinates;
    }
    
    public static Orientation getOrientation()
    {
        System.out.println("H for horizontal, V for vertical");
        char ans = in.next().charAt(0);
        
        while(ans!= 'v' && ans!= 'V' && ans!= 'h' && ans!= 'H')
        {
            System.out.println("ONLY H AND V");
            ans = in.next().charAt(0);
        }
        
        if(ans=='v' || ans=='V')
            return Orientation.vertical;
        else
            return Orientation.horizontal;
    }
    
    public static boolean randomPlace()
    {
        System.out.println("Y for true, N for false");
        char ans = in.next().charAt(0);
        
        while(ans!= 'y' && ans!= 'Y' && ans!= 'n' && ans!= 'N')
        {
            System.out.println("ONLY Y AND N");
            ans = in.next().charAt(0);
        }
        
        if(ans == 'y' || ans == 'Y')
            return true;
            
        else
            return false;
    }
    







    //=========GRAPHICS METHODS==============
        public static String getInfo()
    {
        return player.getStatsString() + '\n' + pc.getStatsString();
    }
    
    
    public static String isTheEnd()
    {
        String toReturnString;
        
        if(player.getBoard().allShipsSunk())
            toReturnString = "\n-- THE WINNER IS PC! --";

        else if(pc.getBoard().allShipsSunk())
            toReturnString = "\n-- THE WINNER IS PLAYER! --";
            
        else if(Parameters.MOVESCHECK && pc.getFires() == Parameters.MAXMOVES   )
            toReturnString = "\n-- TIE --";
        else 
            toReturnString = "false";
        
        return toReturnString;
    }
    //=======================================
    
}

