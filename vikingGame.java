import java.util.*;
public class vikingGame
{
	public static final int E=4; // E = Exit
	public static final int K=3; // K = King
	public static final int O=2; // O = Defender
	public static final int X=1; // X = Attacker
	public static final int N=0; // N = Nothing
	public static final boolean GAME_DONE=false;
	int turn=1;
	int atkPieces = 24;
	int dfrPieces = 12;
	int kingR = 5;
	int kingC = 5;
	int[][] board={	{4,0,0,1,1,1,1,1,0,0,4},
					{0,0,0,0,0,1,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0},
					{1,0,0,0,0,2,0,0,0,0,1},
					{1,0,0,0,2,2,2,0,0,0,1},
					{1,1,0,2,2,3,2,2,0,1,1},
					{1,0,0,0,2,2,2,0,0,0,1},
					{1,0,0,0,0,2,0,0,0,0,1},
					{0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0},
					{4,0,0,1,1,1,1,1,0,0,4}};
	
	public vikingGame clone()
    {
        vikingGame copy=new vikingGame();
        copy.turn=turn;
        copy.atkPieces=atkPieces;
        copy.dfrPieces=dfrPieces;
        copy.kingR=kingR;
        copy.kingC=kingC;
        // Edit to match this game, not connect4 
        for(int j=0;j<11;j++) 
            for(int i=0;i<11;i++) 
                copy.board[j][i]=board[j][i];
        return copy;
    }
	
	// Returns the turn of the game
	public int getTurn()
	{
		if(turn%2==0)return 2;
		else return 1;
	}
	
	// Checks if new coordinates are located at a king only spot
	public boolean kingOnlySpot(int newX, int newY)
	{
		if(board[newX][newY] == 4 || (newX == 5 && newY == 5)) return true;
		else return false;
	}
	
	// Returns true if obstacle is in path of piece, false otherwise
	public boolean obstacleDetector(int r, int c, int newR, int newC)
	{
		boolean kingPresent = false;
		if(board[r][c] == 3) kingPresent = true;
		// Checks obstacle between rows
		if(r != newR){
			if(r < newR){
				for(int i = 1; i <= newR - r; i++){
					if(kingPresent && (board[r+i][c] == X || board[r+i][c] == O))
						return true;
					if(!kingPresent && board[r+i][c] != 0)
						return true;
				}	
			}
			else{
				for(int i = 1; i <= r - newR; i++){
					if(kingPresent && (board[r-i][c] == X || board[r-i][c] == O))
						return true;
					if(!kingPresent && board[r-i][c] != 0)
						return true;
				}
			}
		}
		// Checks obstacles between columns
		if(c != newC){
			if(c < newC){
				for(int i = 1; i <= newC - c; i++){
					if(kingPresent && (board[r][c+i] == X || board[r][c+i] == O))
						return true;
					if(!kingPresent && board[r][c+i] != 0)
						return true;
				}	
			}
			else{
				for(int i = 1; i <= c - newC; i++){
					if(kingPresent && (board[r][c-i] == X || board[r][c-i] == O))
						return true;
					if(!kingPresent && board[r][c-i] != 0)
						return true;
				}
			}
		}
		return false;
		
	}
	
	public boolean isMoveValid (int r, int c, int newR, int newC) 
	{
		// Conditions that determine a valid move
		boolean obstacle = false, sameSpot = false, isInLine = false, ownsPiece = false, ableToMoveIntoKingSpot = false;
		
		// Validate and throw exception if no token of right type at r,c
		if(r == newR && c == newC) sameSpot = true; // checks if both coordinates pairs are the same
		if(r == newR || c == newC) isInLine = true; // checks if the move is correctly horizontal or vertical
		if((board[r][c] == getTurn()) || (getTurn() == 2 && board[r][c] == 3)) ownsPiece = true; // checks if piece can move that turn
		if(board[r][c] == 3 && kingOnlySpot(newR, newC) == true) ableToMoveIntoKingSpot = true; // check if the king is moving into "king only" spots
		
		// makes sure that there are no obstructions
		if(sameSpot == false){
			obstacle = obstacleDetector(r, c, newR, newC);
		}
		// Prints out boolean statements to test moving for debugging purposes
		//~ System.out.print("Obstaclee: " + obstacle + " \tsameSpot: " + sameSpot +
		  //~ " \tisInLine: " + isInLine + " \t\townsPiece: " + ownsPiece + "\n");
		
		// Determines if the move is valid
		if(obstacle == false && sameSpot == false && isInLine == true && ownsPiece == true) return true;
		else return false; 
	}
	
	// Stores a list of possible moves, first two numbers select a piece, and the last two are the destination.
	public int[][] possibleMoves()
	{
		// Array with the length for hypothetical max moves
		int[][] list = new int[20*24][4];
		int listR = 0;
		for(int r = 0; r < 11; r++)
		{
			for(int c = 0; c < 11; c++)
			{ 
				// Checks horizontally for available spots
				for(int PossibleC = 0; PossibleC < 11; PossibleC++)
				{
					if(isMoveValid(r,c,r,PossibleC))
					{
						list[listR][0] = r;
						list[listR][1] = c;
						list[listR][2] = r;
						list[listR][3] = PossibleC;
						listR++;
					}
				}
				// Checks vertically for available spots
				for(int PossibleR = 0; PossibleR < 11; PossibleR++)
				{
					if(isMoveValid(r,c,PossibleR,c))
					{
						list[listR][0] = r;
						list[listR][1] = c;
						list[listR][2] = PossibleR;
						list[listR][3] = c;
						listR++;
					}
				}
			}
		}
		// Refined array containing only possible moves
		int[][] out=new int[listR][4];
		for(int i=0;i<out.length;i++)out[i]=list[i];
		
		//~ for (int[] row : out)
			//~ System.out.println(Arrays.toString(row));
		return out;
	}
	
	// Permanently moves the piece
	public void permanentMove(int r, int c, int newR, int newC)
	{
		boolean validMove = false;
		if(isMoveValid(r,c,newR,newC)){
			// Moving the king
			if(getTurn() == 2 && board[r][c] == 3){
				board[newR][newC] = 3;
				kingR = newR;
				kingC = newC;
			}
			else
				// Moves other pieces
				board[newR][newC] = getTurn();
			board[r][c] = 0;
			// turn increment
			turn++;
			validMove = true;
		}
		capturePiece(validMove,newR,newC);
	}
	// Moves a piece for a child game
	public vikingGame move(int r, int c, int newR, int newC)
	{
		//~ vikingGame copy=new vikingGame();
		vikingGame copy = clone();
		copy.permanentMove(r,c,newR,newC);
		return copy;
	}
	
	// Captures a piece that is sandwhiched between enemies either vertically or horizontally
	private void capturePiece(boolean validMove, int r, int c)
	{
		int movedPiece = board[r][c];
		if(validMove)
		{
			if(r < 9){
				// Checks downward
				if(board[r+1][c] != K && movedPiece != board[r+1][c] && (movedPiece == board[r+2][c] || E  == board[r+2][c]))
					removePiece(r+1,c);
			}
			if(r > 1){
				// Checks upward
				if(board[r-1][c] != K && movedPiece != board[r-1][c] && (movedPiece == board[r-2][c] || E == board[r-2][c]))
					removePiece(r-1,c);
			}
			if(c < 9){
				// Checks right
				if(board[r][c+1] != K && movedPiece != board[r][c+1] && (movedPiece == board[r][c+2] || E == board[r][c+2]))
					removePiece(r,c+1);
			}
			if(c > 1){
				// Checks left
				if(board[r][c-1] != K && movedPiece != board[r][c-1] && (movedPiece == board[r][c-2] || E == board[r][c-2]))
					removePiece(r,c-1);
			}
		}
	}
	// Removes pieces from the board and subtracts piece counter
	private void removePiece(int r, int c)
	{
		if(board[r][c] == O) dfrPieces--;
		if(board[r][c] == X) atkPieces--;
		board[r][c] = 0;
	}
	
	public boolean winner()
	{
		// Pieces up, down, right, left around king
		int upCoord, downCoord, rightCoord, leftCoord;
		// Act as short cuts
		boolean upEnemy = false, downEnemy = false, rightEnemy = false, leftEnemy = false;
		
		// Determines if king wins
		if((kingR == 0 && kingC == 0) || (kingR == 0 && kingC == 10) || (kingR == 10 && kingC == 0) || (kingR == 10 && kingC == 10)){
			//printWinner(2);
			return true;
		}
		//Determines if Attackers win when king is on board side
		if(dfrPieces == 0){
			// Left or Right side
			if(kingC == 0 || kingC == 10){
				upCoord = board[kingR+1][kingC]; downCoord = board[kingR-1][kingC]; 
				if(upCoord == 1) upEnemy = true;
				if(downCoord == 1) downEnemy = true;
				// Left side
				if(kingC == 0){
					rightCoord = board[kingR][kingC+1];
					if(upEnemy && downEnemy && rightCoord == 1){
						//printWinner(1);
						return true;
					}
				}
				// Right side
				if(kingC == 10){
					leftCoord = board[kingR][kingC-1];
					if(upEnemy && downEnemy && leftCoord == 1){
						//printWinner(1);
						return true;
					}
				}
			}
			// Top or bottom side
			if(kingR == 0 || kingR == 10){
				leftCoord = board[kingR][kingC-1]; rightCoord = board[kingR][kingC+1]; 
				if(leftCoord == 1) leftEnemy = true;
				if(rightCoord == 1) rightEnemy = true;
				// Top side
				if(kingR == 0){
					downCoord = board[kingR+1][kingC];
					if(leftEnemy && rightEnemy && downCoord == 1){
						//printWinner(1);
						return true;
					}
				}
				// Bottom side
				if(kingR == 10){
					upCoord = board[kingR-1][kingC];
					if(leftEnemy && rightEnemy && upCoord == 1){
						//printWinner(1);
						return true;
					}
				}
			}
		}
		if(kingR > 0){
			upCoord = board[kingR-1][kingC];
			if(upCoord == 1 || upCoord == 4) upEnemy = true;
		}
		if(kingR < 10){
			downCoord = board[kingR+1][kingC];
			if(downCoord == 1 || downCoord == 4) downEnemy = true;
		}
		if(kingC < 10){
			rightCoord = board[kingR][kingC+1];
			if(rightCoord == 1 || rightCoord == 4) rightEnemy = true;
		}
		if(kingC > 0){
			leftCoord = board[kingR][kingC-1];
			if(leftCoord == 1 || leftCoord == 4) leftEnemy = true;
		}
		if(upEnemy && downEnemy && rightEnemy && leftEnemy){
			//printWinner(1);
			return true;
		}
		return false;
	}
	
	private void printWinner(int whoWon)
	{
		turn--;
		if(whoWon == 2)
			System.out.println("Defenders win\t\tTurns Taken: " + turn);
		else
			System.out.println("Attackers win\t\tTurns Taken: " + turn);
	}
	
	// Prints out a string version of the board
	public String toString()
	{
		// Below print statement is more for debugging purposes
		String s="";
		String top="+";
		for(int i=0;i<11;i++)top+="-"+i%10+"-";
		top+="+\n";
		int j=0;
		for (int[] row : board)
		{
			s+=j%10;
			j++;
			for(int p:row)
			{
				if(p==0)s+="   ";
				if(p==1)s+=" x ";
				if(p==2)s+=" o ";
				if(p==3)s+=" K ";
				if(p==4)s+=" . ";
				
			}
			s+="|\n";
		}
		return top+s+top;
	}
	
	public static void main(String[] args)
	{
		double brachingFactor = 0;
		double totalTimeAtk = 0;
		double totalTimeDfr = 0;
		int atkMoves = 0;
		int dfrMoves = 0;
		int branchingFactor = 0;
		int drawCounter = 0;
		vikingGame v = new vikingGame();
		//~ Player p1=new RandomPlayer();
		Player p1=new AIPlayer(5);
		//~ Player p1=new HumanPlayer();
		Player p2=new AIPlayer(3);
		//~ Player p2=new HumanPlayer();
		
		while(v.winner() == GAME_DONE)
		{
			if(v.atkPieces == 0) break;
			if(v.dfrPieces == 0) drawCounter++;
			if(drawCounter == 20){
				System.out.println("Draw");
				break;
			}
			int[] move;
			long x=System.nanoTime();
			if(v.getTurn()==1){
				move=p1.getMove(v);
				atkMoves++;
				totalTimeAtk += ((System.nanoTime()-x)/1e9);
			}
			else{
				move=p2.getMove(v);
				dfrMoves++;
				totalTimeDfr += ((System.nanoTime()-x)/1e9);
			}
			branchingFactor += v.possibleMoves().length;
			System.out.println("Turn: " + v.turn);
			System.out.println((System.nanoTime()-x)/1e9);
			System.out.println("" + Arrays.toString(move));
			v.permanentMove(move[0],move[1],move[2],move[3]);
			System.out.println("Attacker Pieces: " + v.atkPieces + "\tDefender Pieces: " + v.dfrPieces);
			System.out.println(v);
		}
		System.out.printf("Total Time: %.3f%n", (totalTimeAtk + totalTimeDfr));
		System.out.println("Turns: " + (v.turn-1));
		System.out.println("Branching Factor: " + (branchingFactor/v.turn + "\n"));
		System.out.println("Attacker Pieces Left: " + v.atkPieces);
		System.out.println("Defender Pieces Left: " + v.dfrPieces + "\n");
		System.out.printf("\nTotal Time Attackers: %.3f%n", totalTimeAtk);
		System.out.printf("Total Time Defenders: %.3f%n%n", totalTimeDfr);
		System.out.printf("Average Computation Time: %.5f%n", (totalTimeAtk/atkMoves));
		System.out.printf("Average Computation Time: %.5f%n", (totalTimeDfr/dfrMoves));
		System.out.println("\nAttacker Moves: " + atkMoves + "\nDefender Moves: " + dfrMoves);
	}
}


