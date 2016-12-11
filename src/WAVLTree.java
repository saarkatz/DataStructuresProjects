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
    public boolean empty() {
        return root == null;
    }

    /**
     * public String search(int k)
     *
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        if (root == null) return null;
        WAVLNode result = findKey(root, k);
        if (result == null) {
            return null;
        }
        return result.getValue();
    }

    /**
     * public int insert(int k, String i)
     *
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        WAVLNode newNode = new WAVLNode(k, i);
        newNode.setLeftDifference(1);
        newNode.setRightDifference(1);
        if (root == null) {
            root = newNode;
            return 0;
        }
        WAVLNode node = findInsertionPlace(root, k);
        if (node == null) {
            return -1;
        }
        if (k < node.getKey()) {
            node.setLeft(newNode);
            newNode.setParent(node);
            node.setLeftDifference(node.getLeftDifference() - 1);
        }
        else {
            node.setRight(newNode);
            newNode.setParent(node);
            node.setRightDifference(node.getRightDifference() - 1);
        }
        int insertCase = insertionCase(node);
        while (insertCase != 0) {
            switch (insertCase) {
                case 1:
                    promote(node);
                    node = node.getParent();
                    break;
                case 2: // Case 2 left side
                    rotateRight(node);
                    demote(node);
                    return 1;
                case 3: // Case 2 right side
                    rotateLeft(node);
                    demote(node);
                    return 1;
                case 4: // Case 3 left side
                    rotateLeft(node.getLeft()); // Should never throw NullPointerException, if there are no
                                                // bugs that is.
                    rotateRight(node);
                    demote(node);
                    return 2;
                case 5:
                    rotateRight(node.getLeft());
                    rotateLeft(node);
                    demote(node);
                    return 2;
            }
            insertCase = insertionCase(node); // Here it's only possible to get from case 1
        }
        return 0; // If we got here then no rotations where made;
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
    public String min() {
        if (root == null) {
            return null;
        }
        return minNode(root).getValue();
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (root == null) {
            return null;
        }
        return maxNode(root).getValue();
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

    /////////////////////////////////// Internal helper functions ////////////////////////////////////
    /**
     * @pre node != null
     * @post Rank(node) = Rank(prev(node)) + 1, The rank of the node increased by 1.
     * @post Updates the parent and children of node accordingly
     */
    private static void promote(WAVLNode node) {
        // Increase the difference with children by 1.
        node.setLeftDifference(node.getLeftDifference() + 1);
        node.setRightDifference(node.getRightDifference() + 1);

        // Decrease the difference with parent by 1 if parent is not null.
        if (node.getParent() != null) {
            int relationToParent = node.relationWithParent();
            node.getParent().setDifference(relationToParent,
                    node.getParent().getDifference(relationToParent) - 1);
        }
    }

    /**
     * @pre node != null
     * @post Rank(node) = Rank(prev(node)) - 1, The rank of the node decreased by 1.
     * @post Updates the parent and children of node accordingly
     */
    private static void demote(WAVLNode node) {
        // Decrease the difference with children by 1.
        node.setLeftDifference(node.getLeftDifference() - 1);
        node.setRightDifference(node.getRightDifference() - 1);

        // Increase the difference with parent by 1.
        int relationToParent = node.relationWithParent();
        node.getParent().setDifference(relationToParent,
                node.getParent().getDifference(relationToParent) + 1);
    }

//    /**
//     * @pre node.relationWithChild(child) == 0 || node.relationWithChild(child) == 1
//     * @post updates the differences of
//     */
//    private static void switchRanks(WAVLNode node, WAVLNode child) TODO delete?

    /**
     * rotateRight rotates the node with it's LEFT child.
     *
     * @pre node != null
     * @pre node.hasLeftChild()
     * @post Rotates node with it's left child
     */
    private static void rotateRight(WAVLNode node) {
        WAVLNode leftChild = node.getLeft(); // Keep left child

        int rightDifference = node.getRightDifference();
        int leftDifference = node.getLeftDifference();
        int leftChildRightDifference = leftChild.getRightDifference();
        int leftChildLeftDifference = leftChild.getLeftDifference();

        // Change right child of left child to be the child of this node.
        node.setLeft(leftChild.getRight());
        if (leftChild.hasRightChild()) leftChild.getRight().setParent(node);

        // Change the differences accordingly
        node.setLeftDifference(leftChildRightDifference);
        leftChild.setRightDifference(leftDifference);
        leftChild.setLeftDifference(leftChildLeftDifference + leftDifference);
        node.setRightDifference(rightDifference - leftDifference);

        // Change left child parent to be this node's parent
        leftChild.setParent(node.getParent());
        
        // Change this node to be right child of left child
        leftChild.setRight(node);
        node.setParent(leftChild); // Here this node's parent is being changed so this must come last.
    }

    /**
     * rotateLeft Rotates the node with it's RIGHT child
     *
     * @pre node != null
     * @pre node.hasRightChild()
     * @post Rotates node with it's right child
     */
    private static void rotateLeft(WAVLNode node) {
        WAVLNode rightChild = node.getRight(); // Keep right child

        int leftDifference = node.getLeftDifference();
        int rightDifference = node.getRightDifference();
        int rightChildLeftDifference = rightChild.getLeftDifference();
        int rightChildRightDifference = rightChild.getRightDifference();

        // Change left child of right child to be the child of this node.
        node.setRight(rightChild.getLeft());
        if (rightChild.hasLeftChild()) rightChild.getLeft().setParent(node);

        // Change the differences accordingly TODO: maybe this can be done using promote and demote?
        node.setRightDifference(rightChildLeftDifference);
        rightChild.setLeftDifference(rightDifference);
        rightChild.setRightDifference(rightChildRightDifference + rightDifference);
        node.setLeftDifference(leftDifference - rightDifference);

        // Change right child parent to be this node's parent
        rightChild.setParent(node.getParent());

        // Change this node to be left child of right child
        rightChild.setLeft(node);
        node.setParent(rightChild); // Here this node's parent is being changed so this must come last.
    }

    /**
     * @pre node != null
     * @post Returns the node with the smallest key which is larger then node.getKey().
     * @post Returns null if no such node exists (i.e This node is the maximum of the tree).
     */
    private static WAVLNode successor(WAVLNode node) {
        if (node.getRight() == null) {
            boolean hasParent = node.getParent() != null;
            boolean isLeftChild = node.relationWithParent() == 0;
            if (hasParent && isLeftChild) {
                return node.getParent();
            }
            else {
                return null;
            }
        }
        else {
            return minNode(node.getRight());
        }
    }

    /**
     * @pre node != null
     * @post Returns the node with the largest key which is smaller then node.getKey().
     * @post Returns null if no such node exists (i.e This node is the minimum of the tree).
     */
    private static WAVLNode predecessor(WAVLNode node) {
        if (node.getLeft() == null) {
            boolean hasParent = node.getParent() != null;
            boolean isRightChild = node.relationWithParent() == 1;
            if (hasParent && isRightChild) {
                return node.getParent();
            } else {
                return null;
            }
        } else {
            return maxNode(node.getLeft());
        }
    }

    /**
     * @pre node != null
     * @post Returns the smallest node of the subtree beginning in node (i.e The left most node).
     */
    private static WAVLNode minNode(WAVLNode node) {
        if (node.getLeft() == null) {
            return node;
        }
        else {
            return minNode(node.getLeft());
        }
    }

    /**
     * @pre node != null
     * @post Returns the largest node of the subtree beginning in node (i.e The right most node).
     */
    private static WAVLNode maxNode(WAVLNode node) {
        if (node.getRight() == null) {
            return node;
        }
        else {
            return maxNode(node.getRight());
        }
    }

    /**
     * @pre node != null
     * @post Returns the node containing the key k or null if no such node exists, starting with node.
     */
    private static WAVLNode findKey(WAVLNode node, int k) {
        if (node.getKey() == k) {
            return node;
        }
        else if (k > node.getKey() && node.hasLeftChild()) {
            return findKey(node.getLeft(), k);
        }
        else if (k < node.getKey() && node.hasRightChild()) {
            return findKey(node.getRight(), k);
        }
        else {
            return null;
        }
    }

    /**
     * @pre node != null
     * @post Returns the node after which a node with value k should be inserted or null if a node with key k
     *      exists, starting from node.
     */
    private static WAVLNode findInsertionPlace(WAVLNode node, int k) {
        if (node.getKey() == k) {
            return null;
        }
        else if (k < node.getKey() && node.hasLeftChild()) {
            return findInsertionPlace(node.getLeft(), k);
        }
        else if (k > node.getKey() && node.hasRightChild()) {
            return findInsertionPlace(node.getRight(), k);
        }
        else {
            return node;
        }
    }

    /**
     * @post Returns the insertion case of the node
     *  0   - No further changes should be made.
     *  1   - Promotion required.
     *  2/3 - Rotation required, left/right.
     *  4/5 - Double rotation required, left/right.
     */
    private static int insertionCase(WAVLNode node) {
        if (node == null) return 0;
        boolean leftSideIncorrect = node.getLeftDifference() == 0;
        boolean rightSideIncorrect = node.getRightDifference() == 0;
        if (node.isLeaf() || !(leftSideIncorrect || rightSideIncorrect)) {
            return 0; // Everything is correct
        }
        // At this point we know that either left or right sides are 0.
        else if (node.getLeftDifference() == 1 || node.getRightDifference() == 1) {
            return 1; // The case is 0,1 or 1,0
        }
        // At this point we know that it's case 2 or 3, both of which need to know the side.
        else if (leftSideIncorrect) {
            if (node.getLeft().getRightDifference() == 1) {
                return 2;
            }
            else {
                return 4;
            }
        }
        else {
            if (node.getRight().getLeftDifference() == 1) {
                return 3;
            }
            else {
                return 5;
            }
        }
    }

    private static int deletionCase(WAVLNode node) { return 0; } // TODO: Implement and comment.
    //////////////////////////////////////////// WAVLNode ////////////////////////////////////////////
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

        // Constructor
        public WAVLNode(int k, String i) {
            parent = null;
            left = null;
            right = null;
            differences = new int[2];

            key = k;
            value = i;
        }

        // Setters
        public void setParent(WAVLNode parent) { this.parent = parent; }
        public void setLeft(WAVLNode left) { this.left = left; }
        public void setRight(WAVLNode right){ this.right = right; }
        public void setRightDifference(int rightDifference) { this.differences[0] = rightDifference; }
        public void setLeftDifference(int leftDifference) { this.differences[1] = leftDifference; }
        public void setKey(int key) { this.key = key; }
        public void setValue(String value) { this.value = value; }

        /** @pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public void setDifference(int side, int difference) { this.differences[side] = difference; }

        // Getters
        public WAVLNode getParent() { return parent; }
        public WAVLNode getLeft() { return left; }
        public WAVLNode getRight() { return right; }
        public int getLeftDifference() { return differences[0]; }
        public int getRightDifference() { return differences[1]; }
        public int getKey() { return key; }
        public String getValue() { return value; }

        /** @pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public int getDifference(int side) { return this.differences[side]; }

        public boolean hasLeftChild() { return left != null; }
        public boolean hasRightChild() { return right != null; }

        // Methods
        /**
         * public boolean isLeaf()
         *
         * Returns true iff the node is leaf (i.e both it's children are null).
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * public int relationWithParent()
         *
         * returns 0 if this is left child of it's parent, 1 if it's right child of it's parent or -1 if
         * there is no parent.
         */
        public int relationWithParent() {
            if (parent == null) {
                return -1;
            }
            return parent.relationWithChild(this);
        }

        /**
         * public int relationWithChild(WAVLNode node)
         *
         * returns 0 if node is right child of this, 1 if node is left child of this, otherwise -1.
         * TODO: Decide if null is a child of a leaf and if so what sould be returned,
         * TODO: or -1 should be returned anyway. Will return -1 anyway for now.
         */
        public int relationWithChild(WAVLNode node) {
            if (node == null) {
                return -1;
            }
            else if (node == left) {
                return 0;
            }
            else if (node == right) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
}


