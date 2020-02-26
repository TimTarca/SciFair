public class AIPlayer implements Player
{
	public int depth;
	AIPlayer(){
		depth = 1;
	}
    AIPlayer(int aiDepth){
		depth = aiDepth;
	}
    public int[] getMove(vikingGame game)
    {
        //int[] moves= game.getMoves();
        System.out.println(eval(game));
        return negamax(game,depth,-100000000,100000000);
    }
    public static int[][] shuffle(int[][] m)
    {
		for(int i=0;i<m.length;i++)
		{
			int j=(int)(Math.random()*m.length);
			int[] temp=m[i];
			m[i]=m[j];
			m[j]=temp;
		}
		return m;
	}
    
    //return the bestMove, bestValue
    public static int[] negamax(vikingGame game, int depth,int a,int b)
    {
		if(game.winner()==true) return new int[]{-1,-1,-1,-1,-1000001-depth};
        if(depth==0)
            return new int[]{-1,-1,-1,-1,eval(game)};
        int[] bestMove=new int[5];
        bestMove[4]=-10000001; //bestvalue
        int[][] moves=shuffle(game.possibleMoves());
        
        for(int i=0;i<moves.length;i++)
        {
            int[] move=moves[i];
            vikingGame child=game.move(move[0],move[1],move[2],move[3]);
            int v=-negamax(child, depth-1,-b,-a)[4];
            
            if(v>bestMove[4])
            {
               // store move into first 4 slots in bestmove
               // store v into last slot;
               bestMove[0] = move[0];
               bestMove[1] = move[1];
               bestMove[2] = move[2];
               bestMove[3] = move[3];
               bestMove[4] = v;
            }
            if(v>a)a=v;
            if(a>=b)break;
        }
		return bestMove;
    }
    
   public static int eval(vikingGame game)
    {
		
		if(game.turn%2==0) return (game.dfrPieces * 2) - game.atkPieces;
		return game.atkPieces - (game.dfrPieces * 2);
    }
}

