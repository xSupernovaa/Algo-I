import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointSET
{
    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET()
    {
        set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    // number of points in the set
    public int size()
    {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("null argument to insert()");
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("null argument to contains()");

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        for (Point2D p : set)
        {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException("null argument to range()");
        Stack<Point2D> points = new Stack<Point2D>();

        for (Point2D p : set)
        {
            if (rect.contains(p))
                points.push(p);
        }

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("null argument to nearest()");

        if (set.isEmpty())
            return null;

        double minDist = 2;
        Point2D nearestPoint = null;
        for (Point2D q : set)
        {
            double current = q.distanceSquaredTo(p);
            if (current < minDist)
            {
                minDist = current;
                nearestPoint = q;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        PointSET set = new PointSET();
        for (int i = 0; i < 10; i++)
        {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();

            set.insert(new Point2D(x, y));
        }

        StdDraw.setPenRadius(0.1);

        set.draw();

        Point2D y = new Point2D(0.417, 0.362);

        Point2D x = set.nearest(y);
        StdDraw.setPenColor(StdDraw.RED);
        StdOut.println(x);
        x.draw();
        StdDraw.setPenColor(StdDraw.CYAN);
        y.draw();


    }

}
