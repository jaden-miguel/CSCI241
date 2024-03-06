// Author: Jaden Miguel
// Date: Spring 2020
// Purpose: HashTable class for A3

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */

package heap;


public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        //create new pair, acquire hash value
        Pair temp = buckets[getHashCode(key)];

        while (temp != null) {
            if (temp.key == key) {return temp.value;}
            temp = temp.next;
        }
        //no mapping otherwise
        return null;
    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Runtime: average case O(1); worst case O(size^2 + a.length)*/
    public V put(K key, V val) {
        //find key, create temp
        int k = getHashCode(key);
        Pair temp = buckets[k];

        while (temp != null) {
            if (temp.key == key) {   //key match
                V v = temp.value;
                temp.value = val;
                return v;
            } 
            else if (temp.next == null) {  //null next entry
                temp.next = new Pair(key, val);
                size++;
                growIfNeeded();
                return null;
            }
            temp = temp.next;
        }

        //thus, buckets[k] is null
        buckets[k] = new Pair(key, val);
        size++;
        growIfNeeded();
        return null;
    }



    // returns hashCode of a key, needed for get & put
    private int getHashCode(K key) {
        return Math.abs(key.hashCode()) % (buckets.length);
    }

    // Return true if this map contains a mapping for the specified key.
    // Otherwise, return false.
    // Runtime: average case O(1); worst case O(size) 
    public boolean containsKey(K key) {
        //create temp pair, call for key
        Pair temp = buckets[getHashCode(key)];

        while (temp != null) {
            if (temp.key == key) {return true;}
            temp = temp.next;
        }
        
        return false;
    }

    // Remove the mapping for the specified key from this map if present.
    // Return the previous value associated with key, or null if there was no
    // mapping for key.
    // Runtime: average case O(1); worst case O(size)
    public V remove(K key) {

        //find hashcode of key, create temp
        int k = getHashCode(key);
        Pair temp = buckets[k];
        V val;

        //base case for null
        if (temp == null) {return null;} 
        
        // key is first pair
        if (temp.key == key) {
            val = temp.value;
            if (temp.next == null) {
                buckets[k] = null;
            }
            else {
                buckets[k] = temp.next;
            }
            size--;
            return val;
        } 
        //key is further down
        else {
            while (temp.next != null) {
                if (temp.next.key == key) {
                    val = temp.next.value;
                    //set to previous value
                    temp.next = temp.next.next;
                    size--;
                    return val;
                }
                //reset
                temp = temp.next;
            }
        }
        return null;

    }



    // helper method for put, streamlined 
    // check the load factor; if it exceeds 0.8, double the array size
    // (capacity) and rehash values from the old array to the new array 
    // essentially creating a new bucket array, storing & removing references
    private void growIfNeeded() {
        //calculate load factor
        double n = (double)size / (double)buckets.length;

        if (n > 0.8) {
            //avoid repop errors
            int i = size;  
            size = 0; 

            Pair[] temp = buckets;
            buckets = createBucketArray(buckets.length * 2); //doubling array

            //iterate over pairs 
            for (Pair a : temp) {
                while (a != null) {
                    Pair a2 = a.next;   
                    a.next = null;      
                    put(a.key, a.value);    
                    a = a2;
                }
            }
            //actual size
            size = i;
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
