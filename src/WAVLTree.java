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
    } //complexity O(1)
    private int size = 0;
    /**
     * public String search(int k)
     *
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) { //complexity O(logn)
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
    public int insert(int k, String i) { //complexity O(logn) TODO check if already inside?
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
        size++;
        int insertCase = insertionCase(node); //TODO fix return value for repeated rebalancing?
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
    public int delete(int k) { //complexity O(logn)
        WAVLNode node = findKey(root, k);
        if (node == null) {
            return -1;
        }
        if (!(node.hasLeftChild() && node.hasRightChild())) { //if node has only one child overpass it
            int parentDifference = node.getParent().getDifference(node.relationWithParent());
            int nodeDifference = node.getDifference(node.getDifference(node.relationWithParent()));
            if (node.hasLeftChild()) { //left child case
                node.getParent().setLeftDifference(parentDifference + nodeDifference);
                node.getParent().setLeft(node.getLeft());
                node.getLeft().setParent(node.getParent());
            }
            else { //right child case
                node.getParent().setLeftDifference(parentDifference + nodeDifference);
                node.getParent().setRight(node.getRight());
                node.getRight().setParent(node.getParent());
            }
        }
        if (node.getParent().getKey() > node.getKey() || !node.hasParent()) { //node has two children (left case)
            WAVLNode nodeSuccessor = predecessor(node);                         //or is the root. replace it with its
            node.setKey(nodeSuccessor.getKey());                              //predecessor.
            node.setValue(nodeSuccessor.getValue());
            if (nodeSuccessor.isLeaf()) {
                int parentDifference = node.getParent().getDifference(node.relationWithParent());
                nodeSuccessor.getParent().setDifference(nodeSuccessor.relationWithParent(), parentDifference + 1);
                nodeSuccessor.getParent().setChild(nodeSuccessor.relationWithParent(), null);
            }
            else {
                int parentDifference = node.getParent().getDifference(node.relationWithParent());
                int nodeDifference = node.getDifference(node.getDifference(node.relationWithParent()));
                nodeSuccessor.getParent().setDifference(nodeSuccessor.relationWithParent(), parentDifference + nodeDifference);
                nodeSuccessor.getParent().setChild(nodeSuccessor.relationWithParent(), nodeSuccessor.getRight());
            }
        }
        else {
            WAVLNode nodePredecessor = successor(node); //node has two children (right case) replace it with its
            node.setKey(nodePredecessor.getKey());        //successor
            node.setValue(nodePredecessor.getValue());
            if (nodePredecessor.isLeaf()) {
                nodePredecessor.getParent().setChild(nodePredecessor.relationWithParent(), null);
            }
            else {
                nodePredecessor.getParent().setChild(nodePredecessor.relationWithParent(), nodePredecessor.getLeft());
            }
        }
        size--;
        //Rebalance stage

        int rebalancingOperations = 0; //TODO return nothing in switch scope for safety?
        node = node.getParent();
        int deleteCase = deletionCase(node);
        while (deleteCase != 0) {
            switch (deleteCase) {
                case 1:
                    demote(node);
                    node = node.getParent();
                    rebalancingOperations++;
                    break;
                case 2:
                    demote(node);
                    demote(node.getRight());
                    rebalancingOperations += 2;
                    break;
                case 3:
                    rotateLeft(node);
                    if (node.getLeftDifference() == 2 && node.getRightDifference() == 2 && node.isLeaf()) {
                        demote(node);
                        return rebalancingOperations + 2;
                    }
                    return rebalancingOperations + 1;
                case 4:
                    rotateLeft(node);
                    rotateLeft(node);
                    return rebalancingOperations + 2;
                case 5:
                    demote(node);
                    node = node.getParent();
                    rebalancingOperations++;
                    break;
                case 6:
                    demote(node);
                    demote(node.getLeft());
                    rebalancingOperations += 2;
                    break;
                case 7:
                    rotateRight(node);
                    if (node.getLeftDifference() == 2 && node.getRightDifference() == 2 && node.isLeaf()) {
                        demote(node);
                        return rebalancingOperations + 2;
                    }
                    return rebalancingOperations + 1;
                case 8:
                    rotateRight(node);
                    rotateRight(node);
                    return rebalancingOperations + 2;
            }
            deleteCase = deletionCase(node); // Here it's only possible to get from case 1
        }
        return rebalancingOperations; // If we got here then no rebalancing operations were made;
    }

    /**
     * public String min()
     *
     * Returns the iמfo of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() { //complexity O(logn)
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
    public String max() { //complexity O(logn)
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
    public int[] keysToArray() { //complexity O(n) TODO maybe can do it better?
        if (root == null) {
            int[] emptyArray = new int[0];
            return emptyArray;
        }
        WAVLNode node = root;
        while (node.hasLeftChild()) { //get the node with the smallest key to enter the array first
            node = node.getLeft();
        }
        int i = size();
        int[] keysArray = new int[i];
        int j = 0;
        while (j < i) {
            keysArray[j] = successor(node).getKey();
            node = successor(node);
            j++;
        }
        return keysArray;
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */

    public String[] infoToArray() { //complexity O(n) TODO maybe can do it better?
        if (root == null) {
            String[] emptyArray = new String[0];
            return emptyArray;
        }
        WAVLNode node = root;
        while (node.hasLeftChild()) { //get the node with the smallest key to enter the array first
            node = node.getLeft();
        }
        int i = size();
        String[] valuesArray = new String[i];
        int j = 0;
        while (j < i) {
            valuesArray[j] = successor(node).getValue();
            node = successor(node);
            j++;
        }
        return valuesArray;
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     *
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return size;
    } //complexity O(1)

    /**
     * Returns a detailed string representing the tree;
     */
    @Override
    public String toString() {
        if (root == null) {
            return "[]";
        }
        return subtreeToString(root);
    }

    /////////////////////////////////// Internal helper functions ////////////////////////////////////
    /**
     * @pre node != null
     * @post Rank(node) = Rank(prev(node)) + 1, The rank of the node increased by 1.
     * @post Updates the parent and children of node accordingly
     */
    private static void promote(WAVLNode node) { //complexity O(1)
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
    private static void demote(WAVLNode node) { //complexity O(1)
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
    private static void rotateRight(WAVLNode node) { //complexity O(1)
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
        if (node.hasParent()) { node.getParent().setChild(node.relationWithParent(),leftChild); }
        node.setParent(leftChild); // Here this node's parent is being changed so this must come last.
    }

    /**
     * rotateLeft Rotates the node with it's RIGHT child
     *
     * @pre node != null
     * @pre node.hasRightChild()
     * @post Rotates node with it's right child
     */
    private static void rotateLeft(WAVLNode node) { //complexity O(1)
        WAVLNode rightChild = node.getRight(); // Keep right child

        int leftDifference = node.getLeftDifference();
        int rightDifference = node.getRightDifference();
        int rightChildLeftDifference = rightChild.getLeftDifference();
        int rightChildRightDifference = rightChild.getRightDifference();

        // Change left child of right child to be the child of this node. //"right child" instead of "child"?
        node.setRight(rightChild.getLeft());
        if (rightChild.hasLeftChild()) rightChild.getLeft().setParent(node);

        // Change the differences accordingly TODO: maybe this can be done using promote and demote?
        node.setRightDifference(rightChildLeftDifference);
        rightChild.setLeftDifference(rightDifference);
        rightChild.setRightDifference(rightChildRightDifference + rightDifference);
        node.setLeftDifference(leftDifference - rightDifference);

        // Change right child parent to be this node's parent //?
        rightChild.setParent(node.getParent());

        // Change this node to be left child of right child
        rightChild.setLeft(node);
        if (node.hasParent()) { node.getParent().setChild(node.relationWithParent(),rightChild); }
        node.setParent(rightChild); // Here this node's parent is being changed so this must come last.
    }

    /**
     * @pre node != null
     * @post Returns the node with the smallest key which is larger then node.getKey().
     * @post Returns null if no such node exists (i.e This node is the maximum of the tree).
     */
    private static WAVLNode successor(WAVLNode node) { //complexity O(logn)
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
    private static WAVLNode predecessor(WAVLNode node) { //complexity O(logn)
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
    private static WAVLNode minNode(WAVLNode node) { //complexity O(logn)
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
    private static WAVLNode maxNode(WAVLNode node) { //complexity O(logn)
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
    private static WAVLNode findKey(WAVLNode node, int k) { //complexity: O(logn)
        if (node.getKey() == k) {
            return node;
        }
        else if (k < node.getKey() && node.hasLeftChild()) {
            return findKey(node.getLeft(), k);
        }
        else if (k > node.getKey() && node.hasRightChild()) {
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
    private static WAVLNode findInsertionPlace(WAVLNode node, int k) { //complexity O(logn)
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
    private static int insertionCase(WAVLNode node) { //complexity O(1)
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
            if (node.getLeft().getLeftDifference() == 1) {
                return 2;
            }
            else {
                return 4;
            }
        }
        else {
            if (node.getRight().getRightDifference() == 1) {
                return 3;
            }
            else {
                return 5;
            }
        }
    }

    private static int deletionCase(WAVLNode node) { //complexity O(1)
        if (node == null) return 0;
        boolean leftSideIncorrect = node.getLeftDifference() == 3;
        boolean rightSideIncorrect = node.getRightDifference() == 3;
        if (node.isLeaf() || !(leftSideIncorrect || rightSideIncorrect)) {
            return 0; // Everything is correct
        }
        if (leftSideIncorrect) {
            if (node.getRightDifference() == 2) {
                return 1;
            }
            else if (node.getRight().getRightDifference() == 1) {
                return 3;
            }
            else if (node.getRight().getLeftDifference() == 2) {
                return 2;
            }
            else {
                return 4;
            }
        }
        //cases 1,2,3,4 are symmetric to cases 5,6,7,8 respectively
        else {
            if (node.getLeftDifference() == 2) {
                return 5;
            }
            else if (node.getLeft().getLeftDifference() == 1) {
                return 7;
            }
            else if (node.getLeft().getRightDifference() == 2) {
                return 6;
            }
            else {
                return 8;
            }
        }

    } // TODO: Implement and comment.

    /**
     * Returns a string representing the structure of the subtree starting at the node.
     */
    private static String subtreeToString(WAVLNode node) {
        if (node == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(subtreeToString(node.getLeft()));
        stringBuffer.append(node);
        stringBuffer.append(subtreeToString(node.getRight()));
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

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
        public void setLeftDifference(int leftDifference) { this.differences[0] = leftDifference; }
        public void setRightDifference(int rightDifference) { this.differences[1] = rightDifference; }
        public void setKey(int key) { this.key = key; }
        public void setValue(String value) { this.value = value; }

        /** @pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public void setDifference(int side, int difference) { this.differences[side] = difference; }

        /** @pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public void setChild(int side, WAVLNode child) {
            if (side == 0) {
                left = child;
            }
            else {
                right = child;
            }
        }

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

        /** @pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public WAVLNode getChild(int side) { return side == 0 ? left : right; }

        public boolean hasLeftChild() { return left != null; }
        public boolean hasRightChild() { return right != null; }
        public boolean hasParent() { return parent != null; }

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

        @Override
        public String toString() {
            return String.format("(%1$d,%2$d,%3$d)", differences[0], key, differences[1]);
        }
    }
}


