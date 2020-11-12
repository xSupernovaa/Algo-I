import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{

    private double[] stats;
    private int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        t = trials;
        stats = new double[trials];
        for (int i = 0; i < trials; i++)
        {

            Percolation perc = new Percolation(n);
            int open = 0;
            while (!perc.percolates())
            {
                while (true)
                {
                    int row = StdRandom.uniform(1, n + 1);
                    int col = StdRandom.uniform(1, n + 1);
                    if (!perc.isOpen(row, col))
                    {
                        perc.open(row, col);
                        break;
                    }
                }
                open++;
            }
            stats[i] = (double) open / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(stats);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        if (t == 1)
            return Double.NaN;

        return StdStats.stddev(stats);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - ((1.96 * stddev() / Math.sqrt(t)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + ((1.96 * stddev()) / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, T);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                    = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats
                .confidenceHi() + "]");

    }
}
