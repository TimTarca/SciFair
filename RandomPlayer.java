public class RandomPlayer implements Player
{
    
    public int[] getMove(vikingGame game)
    {
        int[][] possibleMoves=game.possibleMoves();
        return possibleMoves[(int)(Math.random()*possibleMoves.length)];
    }
    
   
}
