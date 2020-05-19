import java.util.Random;
import java.util.*;

public class Board
{
    private static final int SIZE = Parameters.SIZE;
    
    
    private Tile[][] board = new Tile[SIZE][SIZE];
    private boolean hidden;     // TRUE => DO NOT DRAW SHIPS
    
    //CONSTRUCTOR
    public Board(boolean hidden)
    {
        for(int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                board[i][j] = new Tile(j, i ,TileType.Sea);
        this.hidden = hidden;
    }
    
    public Tile getTile(int y, int x)
    {
        return board[y][x];
    }
    
    public ArrayList<Tile> getAdjacentTiles(Tile tile)
    {
        ArrayList<Tile> list = new ArrayList<Tile>();
        int tileX = tile.getX();
        int tileY = tile.getY();

        //left
        if(tileX-1>=0)   list.add(board[tileY][tileX-1]);
        //right
        if(tileX+1<SIZE)   list.add(board[tileY][tileX+1]);
        //up
        if(tileY-1>=0)   list.add(board[tileY-1][tileX]);
        //down
        if(tileY+1<SIZE)   list.add(board[tileY+1][tileX]);
        
        return list;
    }
    
    public void placeAllShips()
    {
        Random r = new Random();
        int[] target;
        int orient; // for Orientation selection
        Orientation orientation;
        
        Ship[] ships = new Ship[5];
        
        ships[0] = new Carrier();
        ships[1] = new Battleship();
        ships[2] = new Cruiser();
        ships[3] = new Submarine();
        ships[4] = new Destroyer();
        
        for(int i = 0; i<5; i++)
            do{
                target = Game.getRandInput();
                
                orient = r.nextInt(2);
                orientation = (orient==0) ? Orientation.horizontal : Orientation.vertical;

            }while(!ships[i].placeShip(board[target[1]][target[0]], orientation, this, false));
    }
    
    //DRAW BOTH BORDS
    public static void drawboards(Board b1, Board b2)
    {
        System.out.println("     - - Y  O  U - -\t\t\t- O  P  P  O  N  E  N  T -");
        System.out.print("  ");
        for(int i=0;i<SIZE;i++)
            System.out.print(i + "  ");
        
        System.out.print("\t\t  ");
            
        for(int i=0;i<SIZE;i++)
            System.out.print(i + "  ");
            
        System.out.print("\n");
            
        for(int i=0;i<b1.SIZE;i++)
        {
            System.out.print(i + " ");
            
            for(int j=0;j<b1.SIZE;j++)
            {
                b1.board[i][j].draw(b1.hidden);
                System.out.print("  ");
            }
            
            System.out.print("\t\t" + i + " ");
                
            for(int k=0;k<b2.SIZE;k++)
            {
                b2.board[i][k].draw(b2.hidden);
                System.out.print("  ");
            }
                
            System.out.println();
        }
    }
    
    //CHECK FOR SUNK SHIPS
    public boolean allShipsSunk()
    {
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++)
                if(board[i][j].getType() == TileType.Ship)
                    return false;
        return true;
    }
    
}
