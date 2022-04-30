package bearmaps.proj2ab;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private Node[] pq;
    private int n;
    //private HashSet<T> hs;
    HashMap<T, Integer> hm;

    private class Node {
        T item;
        double priority;

        Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

    }

    public ArrayHeapMinPQ(int initCapacity) {
        pq = new ArrayHeapMinPQ.Node[initCapacity + 1];
//        for (int i = 1; i < pq.length; i++) {
//            pq[i] = new Node();
//        }
        hm = new HashMap<>();
        n = 0;
    }

    public ArrayHeapMinPQ() {
        this(1);
    }

    private void resize(int capacity) {
        assert capacity > n;
        Node[] tmp = new ArrayHeapMinPQ.Node[capacity];
        for (int i = 1; i <= n; i++) {
            tmp[i] = pq[i];
        }
        pq = tmp;

    }



    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null. */
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }
        pq[++n] = new Node(item, priority);
        hm.put(item, n);
        swim(n);

    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k / 2, k);
            k = k / 2;
        }

    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }

    }
    private boolean greater(int i, int j) {
        return pq[i].priority > pq[j].priority;
    }

    private void exch(int i, int j) {
        T key1 = pq[i].item;
        T key2 = pq[j].item;
        Node tmp = pq[i];
        pq[i] = pq[j];
        pq[j] = tmp;
        hm.put(key1, j);
        hm.put(key2, i);
    }

    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {
        return hm.containsKey(item);


    }
    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return pq[1].item;

    }
    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        T min = pq[1].item;
        // System.out.println(hm.size());
        exch(1, n--);
        hm.remove(min);
        pq[n + 1] = null;
        sink(1);
        if (n > 0 && (double) n / (pq.length - 1) < 1.0 / 4) { //!!
            resize(pq.length / 2);
        }
        return min;


    }
    /* Returns the number of items in the PQ. */
    public int size() {
        return n;

    }
    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int idx = hm.get(item);
        pq[idx].priority = priority;
        swim(idx);
        sink(idx);


    }

//    private static void main(String[] args) {
//        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
//        int i = 0;
//    }

}
