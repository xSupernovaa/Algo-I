import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{

    private class RandomIterator implements Iterator<Item>
    {

        private int index = 0;
        private Item[] copy;

        private RandomIterator()
        {
            copy = (Item[]) new Object[size()];
            shuffleArray();
        }

        private void shuffleArray()
        {
            for (int i = 0; i < size(); i++)
            {
                copy[i] = arr[i];
            }
            StdRandom.shuffle(copy, 0, size());
        }

        public boolean hasNext()
        {
            return index != copy.length && copy[index] != null;
        }

        public Item next()
        {
            if (!hasNext())
                throw new NoSuchElementException("control has reached end of list");
            return copy[index++];
        }
    }

    private Item[] arr;
    private int numberOfItems = 0;

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        arr = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return numberOfItems == 0;
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return numberOfItems;
    }


    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < numberOfItems; i++)
        {
            copy[i] = arr[i];
        }
        arr = copy;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException("null values are not allowed");

        if (size() == arr.length)
            resize(arr.length * 2);

        arr[numberOfItems++] = item;
    }


    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty())
            throw new NoSuchElementException("can't remove from an empty list");

        if (size() != 0 && size() == arr.length / 4)
            resize(arr.length / 2);


        int randomIndex = StdRandom.uniform(numberOfItems);
        Item item = arr[randomIndex];
        arr[randomIndex] = arr[numberOfItems - 1];
        arr[numberOfItems - 1] = null;
        numberOfItems--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
            throw new NoSuchElementException("can't sample from an empty list");

        int randomIndex = StdRandom.uniform(numberOfItems);
        Item item = arr[randomIndex];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RandomIterator();
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        String op;

        while (!StdIn.isEmpty())
        {
            op = StdIn.readString();

            switch (op)
            {
                case "s":
                    StdOut.println(rq.sample());
                    break;

                case "dq":
                    StdOut.println(rq.dequeue());
                    break;

                case "en":
                    int in = StdIn.readInt();
                    rq.enqueue(Integer.toString(in));
                    break;

                case "it":
                    for (String s : rq)
                        StdOut.print(s + " ");
                    StdOut.println();
                    break;

                case "isE":
                    StdOut.println(rq.isEmpty());
                    break;

                case "sz":
                    StdOut.println(rq.size());
                    break;


            }
        }


    }
}
