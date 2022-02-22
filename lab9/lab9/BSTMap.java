package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (p.key.compareTo(key) == 0) {
            return p.value;
        } else if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {//root or bottle
            size++;
            return new Node(key, value);
        } else if (p.key.compareTo(key) == 0) {//reset
            p.value = value;
        } else if (p.key.compareTo(key) > 0) {//insert to left
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
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
        Set<K> set = new HashSet<>();
        keySetHelper(set, root);
        return set;
    }

    private void keySetHelper(Set<K> set, Node p) {
        if (p == null) {
            return;
        }
        set.add(p.key);
        keySetHelper(set, p.left);
        keySetHelper(set, p.right);
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        Node p = findparent_by_key(root, key);//find the parent
        V ans;
        if (p == null) {
            if (root.key == key) {//root node
                ans = root.value;
                root = modify_imitate_remove(root);
            } else {//didn't find
                ans = null;
            }
        } else {
            if (p.left != null && p.left.key == key) {//left child
                Node c = p.left;
                ans = c.value;
                p.left = modify_imitate_remove(c);
            } else {//right child
                Node c = p.right;
                ans = c.value;
                p.right = modify_imitate_remove(c);
            }
        }
        return ans;
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        Node p = findparent_by_key(root, key);
        if (p == null) {
            if (!(root.key == key && root.value == value)) {
                return null;
            } else {
                root = modify_imitate_remove(root);
            }
        } else {
            if (p.left != null && p.left.key == key) {
                Node c = p.left;
                if (c.value != value) {
                    return null;
                }
                p.left = modify_imitate_remove(c);
            } else {
                Node c = p.right;
                if (c.value != value) {
                    return null;
                }
                p.right = modify_imitate_remove(c);
            }
        }
        return value;
    }

    private Node findparent_by_key(Node p, final K key) {//find the parent of the node which is going to be removed
        if (p == null || key.compareTo(root.key) == 0) {
            return null;
        } else if ((p.left != null && p.left.key.compareTo(key) == 0)
                || (p.right != null && p.right.key.compareTo(key) == 0)) {
            return p;
        }

        Node p1 = findparent_by_key(p.left, key);
        if (p1 != null) {
            return p1;
        }
        return findparent_by_key(p.right, key);
    }

    private Node modify_imitate_remove(final Node p) {
        if (p.left == null && p.right == null) {//0 child
            return null;
        } else if (p.left == null) {//1 child
            copyContent(p, p.right);
            p.right=p.right.right;
        } else if (p.right == null) {
            copyContent(p, p.left);
            p.left = p.left.left;
        } else {
            Node l = p.left;
            Node d, dp;
            if (l.right == null) {
                d = l;
                dp = p;
                copyContent(p, d);
                dp.left = modify_imitate_remove(d);
            } else {
                while (l.right.right != null) {
                    l = l.right;
                }
                dp = l;
                d = l.right;
                copyContent(p, d);
                dp.right = modify_imitate_remove(d);
            }
        }
        return p;
    }

    private void copyContent(Node des, Node src) {
        des.value = src.value;
        des.key = src.key;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
