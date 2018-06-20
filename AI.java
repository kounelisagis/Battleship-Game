import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class AI
{
    private static final int SIZE = Parameters.SIZE;
    
    private ArrayList<Integer> availableSizes = new ArrayList<Integer>();    // contains available sizes of ships
    private ArrayList<Tile> possibleMoves = new ArrayList<Tile>();  // containts possible moves
    private Orientation shipOrientation = null; // stores current ship's orientation
    private Tile first, second; // first -> first hit, second -> second hit - USED FOR ORIENTATION
    private int hitsCounter = 1;    // hitsCounter of tiles hitted
    private Board playerBoard;  // reference to the board of the player
    
    public AI(Board playerBoard)
    {
        this.playerBoard = playerBoard;
        availableSizes.add(2);
        availableSizes.add(3);
        availableSizes.add(3);
        availableSizes.add(4);
        availableSizes.add(5);
    }
    
    
    public int[] getPCmove()
    {
        int[] target = new int[2];
        
        if(Parameters.SMART)
        {
            if(possibleMoves.isEmpty())
            {
                first = null;
                second = null;
                do{
                    target = Game.getRandInput();
                }while( playerBoard.getTile(target[1], target[0]).getType() == TileType.Hit ||
                        playerBoard.getTile(target[1], target[0]).getType() == TileType.Miss ||
                        !noHitsNear(playerBoard.getTile(target[1], target[0])) ||
                        ( !canItFitHorizontal(playerBoard.getTile(target[1], target[0])) && !canItFitVertical(playerBoard.getTile(target[1], target[0])) ) );
                
                if(playerBoard.getTile(target[1], target[0]).getType() == TileType.Ship)
                {
                    possibleMoves.addAll(getValidAdjacentTiles(playerBoard.getTile(target[1], target[0])));
                    first = playerBoard.getTile(target[1], target[0]);
                }
            }
            else
            {
                // select the first tile of the arraylist as target
                target[0] = possibleMoves.get(0).getX();
                target[1] = possibleMoves.get(0).getY();
                possibleMoves.remove(0);
                
                if(second == null && playerBoard.getTile(target[1], target[0]).getType() == TileType.Ship)
                {
                    second = playerBoard.getTile(target[1], target[0]);
                    shipOrientation = findOrientation(first, second);
                    deleteNotOriented();
                }
                
                if(playerBoard.getTile(target[1], target[0]).getType() == TileType.Ship)    //call when pc hits ship from ArrayList
                {
                    if( (getOrientedAdjacentTile(playerBoard.getTile(target[1], target[0])))!=null )
                        possibleMoves.add(getOrientedAdjacentTile(playerBoard.getTile(target[1], target[0])));
                    hitsCounter++;
                }
                
                if(hitsCounter == availableSizes.get(availableSizes.size()-1))    //stop when max
                {
                    possibleMoves.clear();
                    availableSizes.remove(availableSizes.size()-1);
                    hitsCounter = 1;
                }
                
                else if(possibleMoves.isEmpty())
                {
                    availableSizes.remove(Integer.valueOf(hitsCounter));    // removes size of ship hitted
                    hitsCounter = 1;
                }
            }
            
            //FOR DEBUGGING
            //for(int i=0;i<possibleMoves.size();i++)
            //    System.err.print(possibleMoves.get(i).getX() + " " + possibleMoves.get(i).getY() + ",  ");
            
            //System.err.println(availableSizes);
            //System.err.println(" --- ");
        }
        
        else
        {
            do{
                target = Game.getRandInput();
            }while( playerBoard.getTile(target[1], target[0]).getType() == TileType.Hit || playerBoard.getTile(target[1], target[0]).getType() == TileType.Miss );
        }
        
        return target;
    }
    
    
    public void deleteNotOriented()
    {
        Iterator<Tile> itr = possibleMoves.iterator();
        while (itr.hasNext())
            if (findOrientation(itr.next(), first) != shipOrientation)
                itr.remove();
    }
    
    
    public boolean noHitsNear(Tile tile)
    {
        ArrayList<Tile> temp = playerBoard.getAdjacentTiles(tile);
        for(int i=0;i<temp.size();i++)
            if(temp.get(i).getType() == TileType.Hit)
                return false;
        return true;
    }
    
    public boolean noMissesNear(Tile tile)
    {
        ArrayList<Tile> temp = playerBoard.getAdjacentTiles(tile);
        for(int i=0;i<temp.size();i++)
            if(temp.get(i).getType() == TileType.Miss)
                return false;
        return true;
    }
    

    public boolean canItFitHorizontal(Tile myTile)
    {
        int hitsCounter = 0;
        int x = myTile.getX();
        int y = myTile.getY();
        
        //search left
        while( x>=0 && noHitsNear(playerBoard.getTile(y, x)) && playerBoard.getTile(y, x).getType()!=TileType.Miss)    //good near checks only for hits nearby. we have also to check for misses
        {
            hitsCounter++;
            x--;
        }
        
        hitsCounter--;
        x = myTile.getX();
        
        //search right
        while( x<SIZE && noHitsNear(playerBoard.getTile(y, x)) && playerBoard.getTile(y, x).getType()!=TileType.Miss)
        {
            hitsCounter++;
            x++;
        }
        
        if(hitsCounter>=availableSizes.get(0))     //minimum
            return true;
            
        return false;
    }
    
    public boolean canItFitVertical(Tile myTile)
    {
        int hitsCounter = 0;
        int x = myTile.getX();
        int y = myTile.getY();
        
        //search up
        while( y<SIZE && noHitsNear(playerBoard.getTile(y, x)) && playerBoard.getTile(y, x).getType()!=TileType.Miss)
        {
            hitsCounter++;
            y++;
        }
        
        hitsCounter--;
        y = myTile.getY();
        
        //search down
        while( y>=0 && noHitsNear(playerBoard.getTile(y, x)) && playerBoard.getTile(y, x).getType()!=TileType.Miss)
        {
            hitsCounter++;
            y--;
        }
            
        if(hitsCounter>=availableSizes.get(0))      //minimum
            return true;
            
        return false;
    }
    
    
    
    public ArrayList<Tile> getValidAdjacentTiles(Tile tile)
    {
        ArrayList<Tile> list = new ArrayList<Tile>();
        int tileX = tile.getX();
        int tileY = tile.getY();
        
        if(tileX-1>=0 && isStartValid(playerBoard.getTile(tileY, tileX-1), tile))
            list.add(playerBoard.getTile(tileY, tileX-1));
        if(tileX+1<SIZE && isStartValid(playerBoard.getTile(tileY, tileX+1), tile))
            list.add(playerBoard.getTile(tileY, tileX+1));
        if(tileY-1>=0 && isStartValid(playerBoard.getTile(tileY-1, tileX), tile))
            list.add(playerBoard.getTile(tileY-1, tileX));
        if(tileY+1<SIZE && isStartValid(playerBoard.getTile(tileY+1, tileX), tile))
            list.add(playerBoard.getTile(tileY+1, tileX));
        
        return list;
    }
    
    public boolean isStartValid(Tile myTile, Tile startTile)
    {
        if( (myTile.getType()==TileType.Sea || myTile.getType()==TileType.Ship)
        && noHitsNear(myTile)
        && ( findOrientation(myTile, startTile)==Orientation.horizontal && canItFitHorizontal(startTile)
        || findOrientation(myTile, startTile)==Orientation.vertical && canItFitVertical(startTile) ) )
            return true;
            
        return false;
    }
    
    
    
    public Tile getOrientedAdjacentTile(Tile tile)
    {
        Tile toReturn = null;
        int tileX = tile.getX();
        int tileY = tile.getY();
        
        if(shipOrientation == Orientation.horizontal)
        {
            if(tileX-1>=0 && isValid(playerBoard.getTile(tileY, tileX-1)))
                toReturn = playerBoard.getTile(tileY, tileX-1);
            else if(tileX+1<SIZE && isValid(playerBoard.getTile(tileY, tileX+1)))
                toReturn = playerBoard.getTile(tileY, tileX+1);
        }
        else
        {
            if(tileY-1>=0 && isValid(playerBoard.getTile(tileY-1, tileX)))
                toReturn = playerBoard.getTile(tileY-1, tileX);
            else if(tileY+1<SIZE && isValid(playerBoard.getTile(tileY+1, tileX)))
                toReturn = playerBoard.getTile(tileY+1, tileX);
        }
        
        return toReturn;
    }
    
    public boolean isValid(Tile myTile)
    {
        int myX = myTile.getX();
        int myY = myTile.getY();
        
        if((myTile.getType()==TileType.Sea || myTile.getType()==TileType.Ship)
        && findOrientation(first, myTile) == shipOrientation
        && noHitsNear(myTile) )
            return true;
            
        return false;
    }
    
    
    
    public Orientation findOrientation(Tile a, Tile b)
    {
        Orientation temp = null;
        if(a.getX() == b.getX()) temp = Orientation.vertical;
        else if(a.getY() == b.getY()) temp = Orientation.horizontal;
        
        return temp;
    }
}
