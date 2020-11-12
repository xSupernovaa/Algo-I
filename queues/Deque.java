import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{

    // Doubly linked list node
    private class Node
    {
        private Node before;
        private Node after;
        private Item item;

        private Node(Item newItem)
        {
            item = newItem;
        }
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node it = head;

        public boolean hasNext()
        {
            return it != null;
        }

        public Item next()
        {
            if (!hasNext())
                throw new NoSuchElementException(
                        "control has reached end of list or no items to iterate on");

            Item item = it.item;
            it = it.before;
            return item;
        }
    }


    // Pointers to the first and last elements in the linked list
    private Node head;
    private Node tail;

    private int size = 0;


    // construct an empty deque
    public Deque()
    {

    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException("null items are not allowed");

        size++;
        Node newNode = new Node(item);

        if (size == 1)
        {
            head = newNode;
            tail = newNode;
            return;
        }

        newNode.before = head;
        head.after = newNode;
        head = newNode;

    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException("null items are not allowed");

        size++;
        Node newNode = new Node(item);

        if (size() == 1)
        {
            head = newNode;
            tail = newNode;
            return;
        }
        newNode.after = tail;
        tail.before = newNode;
        tail = newNode;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty())
            throw new NoSuchElementException("can't remove from an empty list");

        size--;
        Item item = head.item;
        if (!isEmpty())
        {
            head = head.before;
            head.after = null;
        }
        else
        {
            head = null;
            tail = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty())
            throw new NoSuchElementException("can't remove from an empty list");

        size--;
        Item item = tail.item;
        if (!isEmpty())
        {
            tail = tail.after;
            tail.before = null;
        }
        else
        {
            tail = null;
            head = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        Deque<Integer> dq = new Deque<Integer>();

        while (!StdIn.isEmpty())
        {
            String c = StdIn.readString();
            int x;
            switch (c)
            {
                case "al":
                    x = StdIn.readInt();
                    dq.addLast(x);
                    break;

                case "af":
                    x = StdIn.readInt();
                    dq.addFirst(x);
                    break;

                case "rf":
                    StdOut.println(dq.removeFirst());
                    break;

                case "rl":
                    StdOut.println(dq.removeLast());
                    break;
                case "isE":
                    StdOut.println(dq.isEmpty());
                    break;
                case "sz":
                    StdOut.println(dq.size());
                    break;
                case "it":
                    for (Integer i : dq)
                        StdOut.println(i);
                    break;
            }

        }
    }
}
