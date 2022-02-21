package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private double loadFactor() {
        return 1.0 * size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int t = hash(key);
        return buckets[t].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int t = hash(key);
        if (buckets[t].get(key) == null) {
            size++;
        }
        buckets[t].put(key, value);
        if (loadFactor() > MAX_LF) {
            resize();
        }
    }

    private void resize() {
        ArrayMap<K, V>[] newbuckets = new ArrayMap[buckets.length * 2];
        for (int i = 0; i < newbuckets.length; i += 1) {
            newbuckets[i] = new ArrayMap<>();
        }
        int newnumBuckets = newbuckets.length;
        for (ArrayMap<K, V> bucket : buckets) {
            for (K key : bucket) {
                V value = bucket.get(key);
                int idx = Math.floorMod(key.hashCode(), newnumBuckets);
                newbuckets[idx].put(key, value);
            }
        }
        buckets = newbuckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keyset = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            keyset.addAll(buckets[i].keySet());
        }
        return keyset;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int t = hash(key);
        return buckets[t].remove(key);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int t = hash(key);
        if (buckets[t].get(key) == value) {
            return buckets[t].remove(key);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
