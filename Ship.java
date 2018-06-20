import java.util.*;

public abstract class Ship
{
    private static final int SIZE = Parameters.SIZE;
    
    private Tile startTile;
    private Orientation orientation;
    private int size;

    public int getSize()
    {
        return size;
    }
    
    public void setSize(int size)
    {
        this.size = size;
    }
    
    public boolean placeShip(Tile startTile, Orientation orientation, Board board, boolean verbose)
    {
        this.startTile = startTile;
        this.orientation = orientation;
        
        try{
            //throw exception--
            isOutOfBounds();
            isOverlap(board);
            isAdjacent(board);
            //-----------------
            
            int startX = startTile.getX();
            int startY = startTile.getY();
            
            //CHANGE THE TYPE OF EVERY TILE
            if(orientation==Orientation.horizontal)
                for(int j=0; j<size; j++)
                    board.getTile(startY, startX+j).setEnum(TileType.Ship);
            
            else
                for(int i=0; i<size; i++)
                    board.getTile(startY+i, startX).setEnum(TileType.Ship);
            
            return true;
        }
        
        catch(Exception e)
        {
            if(verbose)
                System.err.println(e);
            
            return false;
        }
    }
    
    
    
    public void isOutOfBounds() throws OversizeException
    {
        if(orientation==Orientation.horizontal)
        {
            if(startTile.getY()<0 || startTile.getY() >= SIZE)
                throw new OversizeException();
            if(startTile.getX()<0 || startTile.getX()+size-1 >= SIZE)
                throw new OversizeException();
        }
        else
        {
            if(startTile.getX()<0 || startTile.getX() >= SIZE)
                throw new OversizeException();
            if(startTile.getY()<0 || startTile.getY()+size-1 >= SIZE)
                throw new OversizeException();
        }
    }
    
    public void isOverlap(Board board) throws OverlapTilesException
    {
        if(orientation==Orientation.horizontal)
        {
            int startX = startTile.getX();
            int startY = startTile.getY();
            for(int j=0; j<size; j++)
                if(board.getTile(startY, startX+j).getType() == TileType.Ship )
                    throw new OverlapTilesException();
        }
        else
        {
            int startX = startTile.getX();
            int startY = startTile.getY();
            for(int i=0; i<size; i++)
                if(board.getTile(startY+i, startX).getType() == TileType.Ship )
                    throw new OverlapTilesException();
        } 
    }
    
    public void isAdjacent(Board board) throws AdjancentTilesException
    {
        if(orientation==Orientation.horizontal)
        {
            int startX = startTile.getX();
            int startY = startTile.getY();
            for(int j=0; j<size; j++)
            {
                List<Tile> temp = board.getAdjacentTiles(board.getTile(startY, startX + j));
                for(int k=0; k<temp.size(); k++)
                    if(temp.get(k).getType() == TileType.Ship)
                        throw new AdjancentTilesException();
            }
        }
        else
        {
            int startX = startTile.getX();
            int startY = startTile.getY();
            for(int i=0; i<size; i++)
            {
                List<Tile> temp = board.getAdjacentTiles(board.getTile(startY + i, startX));
                for(int k=0; k<temp.size(); k++)
                    if(temp.get(k).getType() == TileType.Ship )
                        throw new AdjancentTilesException();
            }
        } 
    }
}


//Exceptions Declaration
class AdjancentTilesException extends Exception
{
    public AdjancentTilesException() { super(); }
}

class OversizeException extends Exception
{
    public OversizeException() { super(); }
}

class OverlapTilesException extends Exception
{
    public OverlapTilesException() { super(); }
}