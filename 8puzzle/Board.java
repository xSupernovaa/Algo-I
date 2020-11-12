import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board
{
    private final int[][] grid;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        N = tiles[0].length;
        grid = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                grid[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(N);
        s.append("\n");

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                s.append(grid[i][j]);
                s.append(" ");
            }
            s.append("\n");
        }
        s.append("\n");

        return s.toString();
    }

    // board dimension n
    public int dimension()
    {
        return N;
    }

    // is the tile in position (i, j) in place?
    private boolean inplace(int i, int j)
    {
        if (grid[i][j] == 0)
            return true;

        int correct = (N * i) + (j + 1);
        return correct == grid[i][j];
    }


    // number of tiles out of place
    public int hamming()
    {
        int sumHamming = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (!inplace(i, j))
                    sumHamming++;
            }
        }
        return sumHamming;
    }


    private int tileManhattan(int tile, int currentRow, int currentColumn)
    {
        if (tile == 0)
            return 0;

        int correctRow, correctColumn;

        if (tile % N == 0)
        {
            correctColumn = N - 1;
            correctRow = (tile / N) - 1;
        }
        else
        {
            correctColumn = (tile % N) - 1;
            correctRow = tile / N;
        }
        int distance = Math.abs(currentRow - correctRow) + Math
                .abs(currentColumn - correctColumn);
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {

        int sumManhattan = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                sumManhattan += tileManhattan(grid[i][j], i, j);
            }
        }
        return sumManhattan;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this)
            return true;

        if (y == null)
            return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;

        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (this.grid[i][j] != that.grid[i][j])
                    return false;
            }
        }

        return true;

    }

    private Board neighbor(int i, int j, int zeroRow, int zeroColumn)
    {
        Board neighbor = new Board(this.grid);
        neighbor.grid[zeroRow][zeroColumn] = this.grid[i][j];
        neighbor.grid[i][j] = 0;
        return neighbor;
    }

    private boolean validate(int i, int j)
    {
        return i >= 0 && i < N && j >= 0 && j < N;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        Stack<Board> boardStack = new Stack<Board>();
        int zeroRow = 0, zeroColumn = 0;
        boolean done = false;

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (grid[i][j] == 0)
                {
                    zeroRow = i;
                    zeroColumn = j;
                    done = true;
                    break;
                }
            }
            if (done)
            {
                break;
            }
        }

        // Down
        if (validate(zeroRow + 1, zeroColumn))
        {
            boardStack.push(neighbor(zeroRow + 1, zeroColumn, zeroRow, zeroColumn));
        }

        // Up
        if (validate(zeroRow - 1, zeroColumn))
        {
            boardStack.push(neighbor(zeroRow - 1, zeroColumn, zeroRow, zeroColumn));
        }

        // Left
        if (validate(zeroRow, zeroColumn - 1))
        {
            boardStack.push(neighbor(zeroRow, zeroColumn - 1, zeroRow, zeroColumn));
        }

        // Right
        if (validate(zeroRow, zeroColumn + 1))
        {
            boardStack.push(neighbor(zeroRow, zeroColumn + 1, zeroRow, zeroColumn));
        }

        return boardStack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        Board twin = new Board(grid);

        if (grid[0][0] != 0 && grid[0][1] != 0)
        {
            twin.grid[0][0] = grid[0][1];
            twin.grid[0][1] = grid[0][0];
        }
        else
        {
            twin.grid[1][0] = grid[1][1];
            twin.grid[1][1] = grid[1][0];
        }
        return twin;

    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        int[][] arr = { { 1, 2, 3 }, { 4, 8, 0 } };
        Board test = new Board(arr);

        StdOut.println("MANHATTAN: " + test.manhattan() + "\n" + test.toString());
        for (Board b : test.neighbors())
        {
            StdOut.println("MANHATTAN: " + b.manhattan() + "\n" + b.toString());
        }

    }
}
