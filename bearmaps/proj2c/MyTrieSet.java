package bearmaps.proj2c;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

// sout


public class MyTrieSet implements TrieSet61B {
    //private static final int R = 128;
    private Node root;

    private class Node {
        char c;
        private boolean isKey;
        //private DataIndexedCharMap next; // map
        private HashMap<Character, Node> next; // children
        private Node() {
            // this.c = c;
            isKey = false;
            //next = new DataIndexedCharMap<Node>(R);
            next = new HashMap<>();
        }

    }
//    private static class DataIndexedCharMap<T> {
//        private T[] items;
//        public DataIndexedCharMap(int R) {
//            items = (T[]) new Object[R];
//
//        }
//        public void put(char c, T val) {
//            items[c] = val;
//        }
//        public T get(char c) {
//            return items[c];
//        }
//
//    }

    public MyTrieSet() {
        // root = new Node();
        clear();
    }



    /** Clears all items out of Trie */
    public void clear() {
        root = new Node();


    }

    /** Returns true if the Trie contains KEY, false otherwise */
    public boolean contains(String key) {
//        if (key == null) {
//            throw new IllegalArgumentException("argument to contains() is null");
//
//        }
        return contains(root, key);




    }

    private boolean contains(Node curr, String key) {
        if (key.isEmpty()) {
            return curr.isKey;
        }
        char c = key.charAt(0);
        String rest = key.substring(1);

        Node nextNode = curr.next.get(c);
        if (nextNode == null) {
            return false;
        } else {
            return contains(nextNode, rest);
        }

    }

    /** Inserts string KEY into Trie */

//    @Override
//    public void add(String key) { // iterative version
//        if (key == null || key.length() < 1) {
//            return;
//        }
//        Node curr = root;
//        for (int i = 0, n = key.length(); i < n; i++) {
//            char c = key.charAt(i);
//            if (!curr.next.containsKey(c)) {
//                curr.next.put(c, new Node(c, false));
//            }
//            curr = curr.next.get(c);
//        }
//        curr.isKey = true;
//
//    }

    public void add(String key) { // recursive version
        add(root, key);
    }

    private void add(Node curr, String key) {
        if (key == null || key.isEmpty()) { // modified
            curr.isKey = true;
            return;
        }
        char c = key.charAt(0);
        String rest = key.substring(1);

        Node nextNode = curr.next.get(c);
        if (nextNode == null) {
            nextNode = new Node();
            nextNode.c = c;
            curr.next.put(c, nextNode);
        }
        add(nextNode, rest);

    }

    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        ArrayList<String> results = new ArrayList<>();
        Node curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            curr = curr.next.get(prefix.charAt(i));
            if (curr == null) {
                return new ArrayList<>(); // or null, same idea
            }
        }
        collectionHelper(curr, prefix, results);
        return results;



    }

    private void collectionHelper(Node n, String prefix, List<String> results) {
        if (n.isKey) {
            results.add(prefix);
        }
        Set<Character> keys = n.next.keySet();
        for (Character c : keys) {
            Node nextNode = n.next.get(c);
            collectionHelper(nextNode, prefix + nextNode.c, results); // not n.c
        }

    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) { // prefix do not need to be a word
        // throw new UnsupportedOperationException();
        Node curr = root;
        String prefix = "";
        for (int i = 0; i < key.length(); i++) {
            Node nextNode = curr.next.get(key.charAt(i));
            if (nextNode == null) {
                break;
            } else {
                prefix += key.charAt(i);

            }
        }
        return prefix;

    }
}
