import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree
{

    private int size = 0;
    private Node root = null;
    private RectHV rectQuery;
    private Point2D pointQuery;
    private Stack<Point2D> points;

    private static class Node implements Comparable<Node>
    {
        // the point
        private Point2D p;

        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;

        // the left/bottom subtree
        private Node lb;

        // the right/top subtree
        private Node rt;

        // true--> partition on X, false--> partition on Y
        private boolean vertical;


        private Node(Point2D q)
        {
            p = q;
        }

        public int compareTo(Node that)
        {
            int cmpX = Double.compare(this.p.x(), that.p.x());
            int cmpY = Double.compare(this.p.y(), that.p.y());

            if (that.vertical && cmpX != 0 || cmpY == 0)
            {
                return cmpX;
            }
            else
            {
                return cmpY;
            }
        }
    }

    // construct an empty set of points
    public KdTree()
    {

    }

    // is the set empty?
    public boolean isEmpty()
    {
        return size() == 0;
    }

    // number of points in the set
    public int size()
    {
        return size;
    }


    private Node insert(Node x, Node p)
    {
        if (x == null)
        {
            size++;
            return new Node(p.p);
        }


        int cmp = p.compareTo(x);
        if (cmp < 0)
        {
            x.lb = insert(x.lb, p);
            x.lb.vertical = !x.vertical;

            // below
            if (x.lb.vertical)
            {
                x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            }
            // left
            else
            {
                x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            }
        }
        else if (cmp > 0)
        {
            x.rt = insert(x.rt, p);
            x.rt.vertical = !x.vertical;

            // above
            if (x.rt.vertical)
            {
                x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            }
            // right
            else
            {
                x.rt.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            }
        }
        return x;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("null argument to insert().");


        // disgusting
        if (root == null)
        {
            size++;
            root = new Node(p);
            root.vertical = true;
            root.rect = new RectHV(0, 0, 1, 1);
        }
        Node temp = new Node(p);
        root = insert(root, temp);
        temp = null;
    }


    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("null argument to contains()");

        Node trav = root;
        while (trav != null)
        {
            Node x = new Node(p);
            int cmp = x.compareTo(trav);
            if (cmp < 0)
                trav = trav.lb;
            else if (cmp > 0)
                trav = trav.rt;
            else
                return true;
        }
        return false;
    }


    private void draw(Node x)
    {
        if (x == null)
            return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();
        StdDraw.setPenRadius();
        if (x.vertical)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb);
        draw(x.rt);
    }


    // draw all points to standard draw
    public void draw()
    {
        draw(root);
    }


    private void explore(Node x)
    {

        if (rectQuery.contains(x.p))
            points.push(x.p);

        if (x.lb != null && rectQuery.intersects(x.lb.rect))
            explore(x.lb);

        if (x.rt != null && rectQuery.intersects(x.rt.rect))
            explore(x.rt);

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException("null argument to range()");

        if (isEmpty())
            return null;

        rectQuery = rect;
        points = new Stack<Point2D>();
        explore(root);
        Stack<Point2D> pointsStack = new Stack<Point2D>();
        pointsStack = points;
        points = null;
        return pointsStack;
    }


    private Point2D search(Node x, Point2D min)
    {

        if (x == null)
            return min;

        double currentDist = x.p.distanceSquaredTo(pointQuery);
        double minDist = pointQuery.distanceSquaredTo(min);

        if (x.rect.distanceSquaredTo(pointQuery) > minDist)
            return min;


        if (minDist > currentDist)
            min = x.p;

        if (x.lb != null && x.lb.rect.contains(pointQuery))
        {
            min = search(x.lb, min);
            min = search(x.rt, min);
        }
        else
        {
            min = search(x.rt, min);
            min = search(x.lb, min);
        }

        return min;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {

        if (p == null)
            throw new IllegalArgumentException("null argument to nearest()");

        if (isEmpty())
            return null;
        pointQuery = p;
        Point2D dummy = new Point2D(2, 2);
        Point2D nearest = search(root, dummy);
        dummy = null;
        return nearest;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        KdTree tree = new KdTree();
        String filename = args[0];
        In in = new In(filename);

        while (!in.isEmpty())
        {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        tree.draw();
        StdOut.println(tree.size);
        StdDraw.setPenRadius(0.03);
    }

}
