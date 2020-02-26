import java.util.*;

public class HumanPlayer implements Player
{
    
    public int[] getMove(vikingGame game)
    {
        Scanner scan=new Scanner(System.in);
        int[][] possibleMoves=game.possibleMoves();
        System.out.println(game);
        for(int i=0;i<possibleMoves.length;i++)
        {
			System.out.println(i+") "+Arrays.toString(possibleMoves[i]));
		}
        
        int op=-1;
        while (op<0 || op>=possibleMoves.length)
        {
			System.out.print("What option? ");
			op=scan.nextInt();
		}
        
        return possibleMoves[op];
    }
}
