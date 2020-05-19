public class Player
{
    private String name;
    private int fires = 0;
    private int hits = 0;
    private int misses = 0;
    private int repeats = 0;
    
    private Board board;
    private boolean hidden;
    
    //CONSTRUCTOR
    public Player(String name, boolean hidden)
    {
        this.name = name;
        this.hidden = hidden;
        board = new Board(hidden);
    }
    
    public Board getBoard() { return board; }
    
    public void placeAllShips() { board.placeAllShips(); }
    
    public boolean placeShip(Ship ship, Orientation orientation, int[] coordinates)
    {
        return ship.placeShip(board.getTile(coordinates[1], coordinates[0]), orientation, board, true);
    }
    
    public void fire(Board board, int[] coordinates)
    {
       if (board.getTile(coordinates[1], coordinates[0]).getType() == TileType.Ship)
       {
           board.getTile(coordinates[1], coordinates[0]).setEnum(TileType.Hit);
           hits++;
       }
       else if(board.getTile(coordinates[1], coordinates[0]).getType() == TileType.Sea)
       {
           board.getTile(coordinates[1], coordinates[0]).setEnum(TileType.Miss);
           misses++;
       }
       else
           repeats++;
       
       fires++;
    }
    
    public int getFires(){ return fires; }
    
    public void getStats()
    {
        System.out.println("fires: "+fires+"\nhits: "+hits+"\nmisses: "+misses+"\nrepeats: "+repeats);
    }
    
    
    
    
    //USED ONLY FOR GRAPHICS
    public String getStatsString()
    {
        return("-->>" + name + "\nfires: "+fires+"\nhits: "+hits+"\nmisses: "+misses+"\nrepeats: "+repeats);
    }
    
    
}