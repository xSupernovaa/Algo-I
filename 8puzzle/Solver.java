import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{
    private final Stack<Board> path;
    private final boolean isSolvable;

    private final class Node implements Comparable<Node>
    {
        private final Board board;
        private final Node parent;
        private final int priority;
        private int zeroRow, zeroCol;

        private Node(Board b, Node p)
        {
            board = b;
            parent = p;
            if (parent == null)
                priority = board.manhattan();
            else
                priority = this.board.manhattan() + (parent.priority - parent.board.manhattan())
                        + 1;
        }

        public int compareTo(Node that)
        {
            int prim = this.priority - that.priority;
            if (prim != 0)
                return prim;
            else
                return this.board.manhattan() - that.board.manhattan();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException("null argument to constructor");

        MinPQ<Node> nodeMinPQ = new MinPQ<Node>();
        nodeMinPQ.insert(new Node(initial, null));
        nodeMinPQ.insert(new Node(initial.twin(), null));
        Node current = null;

        while (true)
        {
            current = nodeMinPQ.delMin();
            if (current.board.isGoal())
                break;
            for (Board b : current.board.neighbors())
                if (current.parent == null || !current.parent.board.equals(b))
                    nodeMinPQ.insert(new Node(b, current));
        }
        
        path = new Stack<>();
        while (current.parent != null)
        {
            path.push(current.board);
            current = current.parent;
        }
        path.push(current.board);

        isSolvable = path.peek().equals(initial);

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable())
            return -1;
        return path.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable())
            return null;
        return path;
    }

    // test client (see below)
    public static void main(String[] args)
    {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
