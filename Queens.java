import java.io.*;

class Position{
	public int row;
	public int col;

	public Position(int i, int j)
	{
		row = i;
		col = j;
	}	
}

class Queens{

        private static BufferedReader stdin = new BufferedReader( new InputStreamReader( System.in ) );

	// A zero in a slot in the board means that that slot is
	// available for placing a queen
	public static Position nextAvailable(int[][]  board, Position current)
	{
		int currentRow = current.row;
		int currentCol = current.col;

		//Scan the rest of the current row first
		for(int j = currentCol + 1; j < board.length; j++)
			if(board[currentRow][j] == 0)
				return new Position(currentRow, j);

		// Then scan the rest of the rows
		for(int i = currentRow+1; i < board.length; i++)
			for(int j = 0; j < board.length; j++)
				if(board[i][j] == 0)
					return new Position(i, j);

		// No available position has been found
		return null;
	}


	/* We have three kinds of cells

		available			0
		occupied			-1	
		threatened			x

	   Here x is a positive number that equals the number of queens
	   that threaten that cell.

	   When a queen is placed in (pos.row, pos.col), cells in the
	   row pos.row that are available, need to be set to threatened.
	   Similarly, cells in column pos.col, and cells in the two diagonals
	   that pass through (pos.row, pos.col).
	*/
	
	public static void update(int[][] board, Position pos)
	{
		// Update the row
		for(int j = 0; j < board.length; j++)
			board[pos.row][j]++;

		// Update the column
		for(int i = 0; i < board.length; i++)
			board[i][pos.col]++;

		// Update one diagonal
		int min = minimum(pos.row, pos.col);
		int i = pos.row - min;
		int j = pos.col - min;
		while((i < board.length) && (j < board.length))
		{
			board[i][j]++;
			i++; j++;
		}

		// Update the other diagonal
		min = minimum((board.length-1)-pos.row, pos.col);
		i = pos.row + min;
		j = pos.col - min;
		while((i >= 0) && (j < board.length))
		{
			board[i][j]++;
			i--; j++;
		}

		// Place the queen
		board[pos.row][pos.col] = -1;

	}

	public static int[][] copy(int[][] board)
	{
		int[][] temp = new int[board.length][board.length];
		
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board.length; j++)
				temp[i][j] = board[i][j];

		return temp;
	}
	
	public static int minimum(int x, int y)
	{
		if(x <= y) return x;
		else return y;	
	}
	
	// This function attempts to place n queens on an n by n chessboard.
	// It is assumed that n is a non-negative integer and that board
	// is an n by n 2-d int array, initially containing all zeros.

	public static boolean placeQueens(int n, int[][] board)
	{

		if(n == 0)
			return true;
		// There are positive number of queens to place
		else
		{
			Position start = new Position(-1, board.length-1);
			Position nextPosition = nextAvailable(board, start);
			while(nextPosition != null)
			{
				// place a queen in the first available position and attempt
				// to place the rest. But, save a copy of the board before
				// updating it, so that it can be restored later, if 
				// necessary
				int[][] oldBoard = copy(board);
				update(board, nextPosition);	
				boolean done = placeQueens(n-1, board);

				// If done is true, then we have managed to place
				// the rest of the queeens
				if(done)
					return true;

				// Otherwise, we were not able to place the rest of
				// the queens, given this placement of the current queen
				else
				{	
					// undo the current queen placement and find the
					// next available position
					// Copy the contents of oldBoard back into board
					//**********************************************
					// Earlier I had tried board = oldBoard. I got
					// strange results. Why?
					//**********************************************	
					for(int i = 0; i < board.length; i++)
						for(int j = 0; j < board.length; j++)
							board[i][j] = oldBoard[i][j];

					nextPosition = nextAvailable(board, nextPosition); 
				}
			} // end while
			
			// None of the choices for the current queen have worked
			return false;

		} // end else
	} // end function

	public static void printBoard(int[][] board)
	{
                for(int i = 0; i < board.length; i++)
                {
                        for(int j = 0; j < board.length; j++)
                                if(board[i][j] == -1)
                                        System.out.print("Q ");
                                else
                                        System.out.print("x ");
                        System.out.println("");
                }

	}

        public static void main(String[] args)
        {
		
		int n;
		// Prompt the user
                System.out.println("Type the chess board size." );
                try{

                	// Read a line of text from the user.
                        String input = stdin.readLine();

                        // converts a String into an int value
                        n = Integer.parseInt( input );

			int[][] board = new int[n][n];

			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
					board[i][j] = 0;

			placeQueens(n, board);

			System.out.println("Here is a placement of the queens.");
			printBoard(board);

		}
                catch(java.io.IOException e)
                {
                	System.out.println(e);
                }

		
	}

} // end class
