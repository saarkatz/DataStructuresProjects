/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */
public class WAVLTree {
    private WAVLNode root;

    /**
     * public WAVLTree()
     *
     * Constructor
     */
    public WAVLTree(){
    }

    /**
     * public boolean empty()
     *
     * returns true if and only if the tree is empty
     */
    public boolean empty() { // TODO
        return false; // to be replaced by student code
    }

    /**
     * public String search(int k)
     *
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) { // TODO
        return "42";  // to be replaced by student code
    }

    /**
     * public int insert(int k, String i)
     *
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) { // TODO
        return 42;
    }

    /**
     * public int delete(int k)
     *
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) { // TODO
        return 42;    // to be replaced by student code
    }

    /**
     * public String min()
     *
     * Returns the iמfo of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() { // TODO
        return "42"; // to be replaced by student code
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() { // TODO
        return "42"; // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() { // TODO
        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() { // TODO
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     *
     * precondition: none
     * postcondition: none
     */
    public int size() { // TODO
        return 42; // to be replaced by student code
    }

    /**
     * public class WAVLNode
     *
     * If you wish to implement classes other than WAVLTree
     * (for example WAVLNode), do it in this file, not in
     * another file.
     * This is an example which can be deleted if no such classes are necessary.
     */
    public class WAVLNode {
        // References to neighbors
        private WAVLNode parent;
        private WAVLNode left;
        private WAVLNode right;
        private int[] differences;

        // Fields of WASVLNode
        private int key;
        private String value;

        // Setters
        public void setParent(WAVLNode parent) {}
        public void setLeft(WAVLNode left) {}
        public void setRight(WAVLNode right){}
        public void setLeftDifference(int leftDifference) {}
        public void setRightDifference(int rightDifference) {}
        public void setKey(int key) {}
        public void setValue(String value) {}

        // Getters
        public WAVLNode getParent() {return null;}
        public WAVLNode getLeft() {return null;}
        public WAVLNode getRight() {return null;}
        public int getLeftDifference() {return 0;}
        public int getRightDifference() {return 0;}
        public int getKey() {return 0;}
        public String getValue() {return null;}

        // Methods

        /**
         * public boolean isLeaf()
         *
         * Returns true iff the node is leaf (i.e both it's children are null).
         */
        public boolean isLeaf() {return false;} // TODO

        /**
         * public int whichChild(WAVLNode node)
         *
         * returns 0 if node is right child, 1 if node is left child, otherwise -1.
         * TODO: Decide if null is a child of a leaf and if so what sould be returned,
         * TODO: or -1 should be returned anyway.
         */
        public int whichChild(WAVLNode node) {return 0;} // TODO
    }
}


