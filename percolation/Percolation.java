import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private WeightedQuickUnionUF QU;
    private WeightedQuickUnionUF QU2;
    private boolean[] open;
    private int dimension;
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0)
            throw new IllegalArgumentException("constructor.");

        dimension = n;
        // Create N*N Grid + 2 virtual sites
        QU = new WeightedQuickUnionUF((n * n) + 2);
        QU2 = new WeightedQuickUnionUF((n * n) + 1);
        open = new boolean[((n * n) + 2)];

        // Union first and last row with virtual sites
        for (int i = 1; i <= n; i++)
        {
            QU.union(0, i);
            QU2.union(0, i);
            QU.union((n * n) + 1, i + (n * (n - 1)));
        }
    }

    private int convert(int row, int col)
    {
        return col + dimension * (row - 1);
    }

    private boolean validate(int x)
    {
        return x <= dimension * dimension && x > 0;
    }

    private boolean validate(int row, int col)
    {
        return (row > 0 && row <= dimension) && (col > 0 && col <= dimension);
    }

    private void neighbors(int site, int incDec)
    {
        int newSite = site + incDec;
        if (validate(newSite) && open[newSite])
        {
            QU.union(site, site + incDec);
            QU2.union(site, site + incDec);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int site = convert(row, col);

        if (!validate(site) || !validate(row, col))
            throw new IllegalArgumentException("out of bounds argument to open.");

        if (open[convert(row, col)])
            return;
        openSites++;
        open[convert(row, col)] = true;

        if (col != dimension)
            neighbors(site, 1);

        if (col != 1)
            neighbors(site, -1);


        neighbors(site, dimension);
        neighbors(site, -dimension);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (!validate(convert(row, col)) || !validate(row, col))
            throw new IllegalArgumentException("out of bounds argument to isOpen.");

        return open[convert(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (!validate(convert(row, col)) || !validate(row, col))
        {
            throw new IllegalArgumentException("out of bounds argument to isFull.");
        }

        if (!isOpen(row, col))
            return false;

        return QU2.find(0) == QU2.find(convert(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        if (dimension == 1)
            return open[1];
        if (dimension == 2)
            return QU2.find(3) == QU2.find(0) || QU2.find(4) == QU2.find(0);
        return QU.find(0) == QU.find(dimension * dimension + 1);
    }

    // test client (optional)
    public static void main(String[] args)
    {
    }
}
