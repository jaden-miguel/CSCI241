// Author: Jaden Miguel
// Date: Spring 2020
// Purpose: Heap class for A3

package heap;
import java.util.NoSuchElementException;

/**
 * An instance is a min-heap of distinct values of type V with priorities of
 * type P. Since it's a min-heap, the value with the smallest priority is at the
 * root of the heap.
 */
public final class Heap<V, P extends Comparable<P>> {

    // TODO 1.0: Read and understand the class invariants given in the
    // following comment: check

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually accomplished
     * using its get method. In the complete tree, c[0] is the root; c[2i+1] is the
     * left child of c[i] and c[2i+2] is the right child of i. If c[i] is not the
     * root, then c[(i-1)/2] (using integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     * The tree is complete: 1. `c[0..c.size()-1]` are non-null
     *
     * The tree satisfies the heap property: 2. if `c[i]` has a parent, then
     * `c[i]`'s parent's priority is smaller than `c[i]`'s priority
     *
     * In Phase 3, the following class invariant also must be maintained: 3. The
     * tree cannot contain duplicate *values*; note that duplicate *priorities* are
     * still allowed. 4. map contains one entry for each element of the heap, so
     * map.size() == c.size() 5. For each value v in the heap, its map entry
     * contains in the the index of v in c. Thus: map.get(b[i]) = i.
     */

    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p */
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

    }

    /**
     * Add v with priority p to the heap. The expected time is logarithmic and the
     * worst-case time is linear in the size of the heap. Precondition: p is not
     * null. In Phase 3 only:
     * 
     * @throws IllegalArgumentException if v is already in the heap.
     */
    public void add(V v, P p) throws IllegalArgumentException {

        // phase three addition, throws exception
        if (map.containsKey(v)) {
            throw new IllegalArgumentException();
        }

        c.append(new Entry(v, p));

        // now, we handle map size
        map.put(v, c.size - 1);
        bubbleUp(c.size - 1);
    }

    /**
     * Return the number of values in this heap. This operation takes constant time.
     */
    public int size() {
        return c.size();
    }

    /**
     * Swap c[h] and c[k]. precondition: h and k are >= 0 and < c.size()
     */
    protected void swap(int h, int k) {
        // get and put values
        map.put(c.get(h).value, k);
        map.put(c.get(k).value, h);

        // create temp to reference
        Entry temp = c.get(h);
        c.put(h, c.get(k));
        c.put(k, temp);
    }

    // Bubble c[k] up in heap to its right place.
    // Precondition: Priority of every c[i] >= its parent's priority
    // except perhaps for c[k]
    protected void bubbleUp(int k) {

        int n = ((k - 1) / 2); // calculate parent

        if (n < 0) {return;} // no parent case

        P a = c.get(k).priority;
        P b = c.get(n).priority;

        if (a.compareTo(b) < 0) {
            swap(k, n);
            bubbleUp(n);
        }
    }

    /**
     * Return the value of this heap with lowest priority. Do not change the heap.
     * This operation takes constant time.
     * 
     * @throws NoSuchElementException if the heap is empty.
     */
    public V peek() throws NoSuchElementException {
        //empty heap
        if (c.size == 0) {throw new NoSuchElementException();} 

        return c.get(0).value;
    }

    /**
     * Remove and return the element of this heap with lowest priority. The expected
     * time is logarithmic and the worst-case time is linear in the size of the
     * heap.
     * 
     * @throws NoSuchElementException if the heap is empty.
     */
    public V poll() throws NoSuchElementException {
        //empty heap exception
        if (c.size == 0) {throw new NoSuchElementException();}

        V v = c.get(0).value;
        c.put(0, c.get(c.size - 1)); // replace first element
        c.put(c.size - 1, null); // remove last element
        c.size--;
        bubbleDown(0);

        map.remove(v); // phase 3 invariant

        // iterate over map, put value
        for (int i = 0; i < map.size; i++) {
            V temp = c.get(i).value;
            map.put(temp, i);
        }
        return v;

    }

    /**
     * Bubble c[k] down in heap until it finds the right place. If there is a choice
     * to bubble down to both the left and right children (because their priorities
     * are equal), choose the right child. Precondition: Each c[i]'s priority <= its
     * childrens' priorities except perhaps for c[k]
     */
    protected void bubbleDown(int k) {
        // we need to find our base cases, flip bubbleup

        // no child
        if ((k * 2 + 1) > c.size - 1) {return;}

        int n = smallerChild(k); // calc smaller child

        P one = c.get(k).priority;
        P two = c.get(n).priority;

        // opposite of bubbleup
        if (one.compareTo(two) > 0) {
            swap(k, n);
            bubbleDown(n);
        }
    }

    // Return true if the value v is in the heap, false otherwise.
    // The average case runtime is O(1)
    public boolean contains(V v) {
        return map.containsKey(v);
    }

    /**
     * Change the priority of value v to p. The expected time is logarithmic and the
     * worst-case time is linear in the size of the heap.
     * 
     * @throws IllegalArgumentException if v is not in the heap.
     */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        // check if key is not in heap
        if (!map.containsKey(v)) {throw new IllegalArgumentException();}

        int j = map.get(v); //find value in question
        c.put(j, new Entry(v, p)); //update priority w/ new entry

        //fix heap property if needed
        bubbleUp(j);
        bubbleDown(j);
    }

    // helper spec: return index of the child of k with smaller priority.
    // if only one child exists, return that child's index
    // pre: at least one child exists.
    private int smallerChild(int k) {
        int a = (2 * k + 1);
        int b = (2 * k + 2);

        P small = c.get(a).priority, r;
        try {
            r = c.get(b).priority;
        } 
        catch (IndexOutOfBoundsException e) {
            return a;
        }
        
        return (small.compareTo(r) < 0) ? a : b;
    }

}
