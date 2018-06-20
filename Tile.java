public class Tile
{
    private TileType myType;
    private int x, y;
    
    //CONSTRUCTOR
    public Tile(int x, int y, TileType myType)
    {
        this.x = x;
        this.y = y;
        this.myType = myType;
    }

    //GETTERS - SETTERS
    public void setX(int x) {this.x = x;}
    
    public void setY(int y) {this.y = y;}
    
    public int getX() {return x;}
    
    public int getY() {return y;}
    
    public void setEnum(TileType myEnum) {this.myType=myEnum;}
    
    public TileType getType() {return myType;}
    
    //TILE DRAW
    public void draw(boolean hidden)
    {
        switch(myType)
        {
            case Sea:
                System.out.print("~");
                break;
            case Ship:
                if(hidden)
                    System.out.print("~");
                else
                    System.out.print("s");
                break;
            case Hit:
                System.out.print("X");
                break;
            case Miss:
                System.out.print("o");
                break;
        }        
    }
}
