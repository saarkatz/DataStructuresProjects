import com.sun.istack.internal.NotNull;

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
    private int size;

    /**
     * public WAVLTree()
     *
     * Constructor
     */
    public WAVLTree(){
        root = null;
        size = 0;
    }

    /**
     * public boolean empty()
     *
     * returns true if and only if the tree is empty
     */
    // Complexity O(1)
    public boolean empty() {
        return root == null;
    }

    /**
     * public String search(int k)
     *
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    // Complexity O(logn)
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
    // Complexity O(logn)
    public int insert(int k, String i) {
        WAVLNode newNode = new WAVLNode(k, i);
        if (root == null) {
            root = newNode;
            size++;
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
        // Re-balance tree
        int rebalanceOperations = 0;
        int insertCase = insertionCase(node);
        while (insertCase != 0) {
            switch (insertCase) {
                case 1: // Case 1 - Promote
                    promote(node);
                    node = node.getParent();
                    rebalanceOperations++;
                    break;
                case 2: // Case 2 left side
                    rotateRight(node);
                    demote(node);
                    rebalanceOperations += 2;
                    break;
                case 3: // Case 2 right side
                    rotateLeft(node);
                    demote(node);
                    rebalanceOperations += 2;
                    break;
                case 4: // Case 3 left side
                    rotateLeft(node.getLeft());
                    rotateRight(node);
                    demote(node);
                    rebalanceOperations += 3;
                    break;
                case 5: // Case 3 right side
                    rotateRight(node.getRight());
                    rotateLeft(node);
                    demote(node);
                    rebalanceOperations += 3;
                    break;
            }
            insertCase = insertionCase(node);
        }
        // If a rotation moved the root away from the top, fix it.
        if (root.hasParent()) {
            root = root.getParent();
        }
        return rebalanceOperations;
    }

    /**
     * public int delete(int k)
     *
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    // Complexity O(logn)
    public int delete(int k) {
        if (root == null) {
            return -1;
        }
        WAVLNode node = findKey(root, k);
        WAVLNode nodeToBalance;
        if (node == null) {
            return -1;
        }
        if (size() == 1) {
            root = null;
            size--;
            return 0;
        }
        if (!node.hasParent()) {
            // If node is root, replace its fields with one of its children's fields
            // (by this stage it must have at least one) and continue the delete process with the chosen child.
            if (node.hasLeftChild()) {
                node.setKey(node.getLeft().getKey());
                node.setValue(node.getLeft().getValue());
                node = node.getLeft();
            }
            else {
                node.setKey(node.getRight().getKey());
                node.setValue(node.getRight().getValue());
                node = node.getRight();
            }
        }
        if (node.hasLeftChild() && node.hasRightChild()) {
            // If node has two children, replace its fields with its predecessor's/successor's fields
            // And continue the delete process with the predecessor/successor (respectively).
            if (node.getKey() < node.getParent().getKey()) {
                WAVLNode predecessorNode = predecessor(node);
                node.setKey(predecessorNode.getKey());
                node.setValue(predecessorNode.getValue());
                node = predecessorNode;
            }
            else {
                WAVLNode successorNode = successor(node);
                node.setKey(successorNode.getKey());
                node.setValue(successorNode.getValue());
                node = successorNode;
            }
        }
        if (node.isLeaf()) {
            // If node is a leaf, delete the node and update differences.
            node.getParent().setDifference(node.relationWithParent(),
                    node.getParent().getDifference(node.relationWithParent()) + 1);
            nodeToBalance = node.getParent();
            nodeToBalance.setChild(node.relationWithParent(), null);
        }
        else {
            // If node has exactly one child, delete the node and connect its parent to its child
            nodeToBalance = node.getParent();
            if (node.hasLeftChild()) {
                nodeToBalance.setDifference(node.relationWithParent(),
                        nodeToBalance.getDifference(node.relationWithParent()) + node.getLeftDifference());
                nodeToBalance.setChild(node.relationWithParent(), node.getLeft());
                node.getLeft().setParent(node.getParent());
            }
            else {
                nodeToBalance.setDifference(node.relationWithParent(),
                        nodeToBalance.getDifference(node.relationWithParent()) + node.getRightDifference());
                nodeToBalance.setChild(node.relationWithParent(), node.getRight());
                node.getRight().setParent(node.getParent());
            }
        }
        size--;

        //Re-balance stage
        int rebalanceOperations = 0;
        node = nodeToBalance;
        int deleteCase = deletionCase(node);
        while (deleteCase != 0) {
            switch (deleteCase) {
                case 1:
                    demote(node);
                    node = node.getParent();
                    rebalanceOperations++;
                    break;
                case 2:
                    demote(node);
                    demote(node.getRight());
                    node = node.getParent();
                    rebalanceOperations += 2;
                    break;
                case 3:
                    demote(node);
                    demote(node.getLeft());
                    node = node.getParent();
                    rebalanceOperations += 2;
                    break;
                case 4:
                    rotateLeft(node);
                    rebalanceOperations++;
                    break;
                case 5:
                    rotateRight(node);
                    rebalanceOperations++;
                    break;
                case 6:
                    rotateRight(node.getRight());
                    rotateLeft(node);
                    rebalanceOperations += 2;
                    break;
                case 7:
                    rotateLeft(node.getLeft());
                    rotateRight(node);
                    rebalanceOperations += 2;
                    break;
            }
            deleteCase = deletionCase(node);
        }
        // If a rotation moved the root away from the top, fix it.
        if (root.hasParent()) {
            root = root.getParent();
        }
        return rebalanceOperations;
    }

    /**
     * public String min()
     *
     * Returns the iמfo of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    // Complexity O(logn)
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
    // Complexity O(logn)
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
    // Complexity O(n)
    public int[] keysToArray() {
        if (root == null) {
            return  new int[0];
        }
        WAVLNode node = minNode(root);
        int length = size();
        int[] keysArray = new int[length];
        for (int i = 0; i < length; i++){
            keysArray[i] = node.getKey();
            node = successor(node);
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
    // Complexity O(n)
    public String[] infoToArray() {
        if (root == null) {
            return new String[0];
        }
        WAVLNode node = minNode(root);
        int length = size();
        String[] valuesArray = new String[length];
        for (int i = 0; i < length; i++) {
            valuesArray[i] = node.getValue();
            node = successor(node);
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
    // Complexity O(1)
    public int size() {
        return size;
    }

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
     * #pre node != null
     * #post Rank(node) = Rank(prev(node)) + 1, The rank of the node increased by 1.
     * #post Updates the parent and children of node accordingly
     */
    // Complexity O(1)
    private static void promote(WAVLNode node) {
        changeRank(node, 1);
    }

    /**
     * #pre node != null
     * #post Rank(node) = Rank(prev(node)) - 1, The rank of the node decreased by 1.
     * #post Updates the parent and children of node accordingly
     */
    // Complexity O(1)
    private static void demote(WAVLNode node) {
        changeRank(node, -1);
    }

    /**
     * #pre node != null
     * #post Rank(node) = Rank(prev(node)) + amount, The rank of the node increased by amount.
     * #post Updates the parent and children of node accordingly
     */
    // Complexity O(1)
    private static void changeRank(WAVLNode node, int amount) {
        // Change the difference with children by amount.
        node.setLeftDifference(node.getLeftDifference() + amount);
        node.setRightDifference(node.getRightDifference() + amount);

        // If parent is not null, update the difference of parent by -amount.
        if (node.getParent() != null) {
            int relationToParent = node.relationWithParent();
            node.getParent().setDifference(relationToParent,
                    node.getParent().getDifference(relationToParent) - amount);
        }
    }

    /**
     * rotateRight rotates the node with it's LEFT child.
     *
     * #pre node != null
     * #pre node.hasLeftChild()
     * #post Rotates node with it's left child
     * #post Returns the other node in the rotation.
     */
    // Complexity O(1)
    private static WAVLNode rotateRight(WAVLNode node) {
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

        // Change left child's parent to be this node's parent
        leftChild.setParent(node.getParent());
        
        // Change this node to be right child of left child
        leftChild.setRight(node);
        if (node.hasParent()) {
            node.getParent().setChild(node.relationWithParent(), leftChild);
        }
        node.setParent(leftChild); // Here this node's parent is being changed so this must come last.
        return leftChild;
    }

    /**
     * rotateLeft Rotates the node with it's RIGHT child
     *
     * #pre node != null
     * #pre node.hasRightChild()
     * #post Rotates node with it's right child
     * #post Returns the other node in the rotation.
     */
    // Complexity O(1)
    private static WAVLNode rotateLeft(WAVLNode node) {
        WAVLNode rightChild = node.getRight(); // Keep right child

        int leftDifference = node.getLeftDifference();
        int rightDifference = node.getRightDifference();
        int rightChildLeftDifference = rightChild.getLeftDifference();
        int rightChildRightDifference = rightChild.getRightDifference();

        // Change left child of right child to be the child of this node.
        node.setRight(rightChild.getLeft());
        if (rightChild.hasLeftChild()) rightChild.getLeft().setParent(node);

        // Change the differences accordingly
        node.setRightDifference(rightChildLeftDifference);
        rightChild.setLeftDifference(rightDifference);
        rightChild.setRightDifference(rightChildRightDifference + rightDifference);
        node.setLeftDifference(leftDifference - rightDifference);

        // Change right child's parent to be this node's parent
        rightChild.setParent(node.getParent());

        // Change this node to be left child of right child
        rightChild.setLeft(node);
        if (node.hasParent()) {
            node.getParent().setChild(node.relationWithParent(), rightChild);
        }
        node.setParent(rightChild); // Here this node's parent is being changed so this must come last.
        return rightChild;
    }

    /**
     * #pre node != null
     * #post Returns the node with the smallest key which is larger then node.getKey().
     * #post Returns null if no such node exists (i.e This node is the maximum of the tree).
     */
    // Complexity O(logn)
    private static WAVLNode successor(WAVLNode node) {
        if (node.getRight() == null) {
            boolean hasParent = node.getParent() != null;
            boolean isLeftChild = node.relationWithParent() == 0;
            if (hasParent && isLeftChild) {
                return node.getParent();
            }
            else if (hasParent) {
                while (node.relationWithParent() == 1) {
                    node = node.getParent();
                }
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
     * #pre node != null
     * #post Returns the node with the largest key which is smaller then node.getKey().
     * #post Returns null if no such node exists (i.e This node is the minimum of the tree).
     */
    // Complexity O(logn)
    private static WAVLNode predecessor(WAVLNode node) {
        if (node.getLeft() == null) {
            boolean hasParent = node.getParent() != null;
            boolean isRightChild = node.relationWithParent() == 1;
            if (hasParent && isRightChild) {
                return node.getParent();
            }
            else if (hasParent) {
                while (node.relationWithParent() == 0) {
                    node = node.getParent();
                }
                return node.getParent();
            }
            else {
                return null;
            }
        }
        else {
            return maxNode(node.getLeft());
        }
    }

    /**
     * #pre node != null
     * #post Returns the smallest node of the subtree beginning in node (i.e The left most node).
     */
    // Complexity O(logn)
    @NotNull
    private static WAVLNode minNode(@NotNull WAVLNode node) {
        if (node.getLeft() == null) {
            return node;
        }
        else {
            return minNode(node.getLeft());
        }
    }

    /**
     * #pre node != null
     * #post Returns the largest node of the subtree beginning in node (i.e The right most node).
     */
    // Complexity O(logn)
    @NotNull
    private static WAVLNode maxNode(@NotNull WAVLNode node) {
        if (node.getRight() == null) {
            return node;
        }
        else {
            return maxNode(node.getRight());
        }
    }

    /**
     * #pre node != null
     * #post Returns the node containing the key k or null if no such node exists, starting with node.
     */
    // Complexity O(logn)
    private static WAVLNode findKey(WAVLNode node, int k) {
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
     * #pre node != null
     * #post Returns the node after which a node with value k should be inserted or null if a node with key k
     *      exists, starting from node.
     */
    // Complexity O(logn)
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
     * #post Returns the insertion case of the node
     *  0   - No further changes should be made.
     *  1   - Promotion required.
     *  2/3 - Rotation required, left/right.
     *  4/5 - Double rotation required, left/right.
     */
    // Complexity O(1)
    private static int insertionCase(WAVLNode node) {
        if (node == null) return 0;
        boolean leftSideIncorrect = node.getLeftDifference() == 0;
        boolean rightSideIncorrect = node.getRightDifference() == 0;
        if (node.isLeaf() || !(leftSideIncorrect || rightSideIncorrect)) {
            return 0; // Everything is correct
        }
        // At this point we know that either left or right sides are 0.
        else if (node.getLeftDifference() == 1 || node.getRightDifference() == 1) {
            return 1; // The case is 0,1 or 1,0 (Case 1 either way)
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
    /**
     * #post Returns the deletion case of the node
     *  0   - No further changes should be made.
     *  1   - Demotion required, left/right.
     *  2/3 - Double demotion required, left/right.
     *  4/5 - Rotation required, left/right.
     *  6/7 - Double rotation required, left/right.
     */
    // Complexity O(1)
    private static int deletionCase(WAVLNode node) {
        if (node == null) return 0;
        boolean leftSideIncorrect = node.getLeftDifference() == 3;
        boolean rightSideIncorrect = node.getRightDifference() == 3;
        if (node.isLeaf() && node.getLeftDifference() == 2 && node.getRightDifference() == 2) {
            return 1;
        }
        if (!(leftSideIncorrect || rightSideIncorrect)) {
            return 0; // Everything is correct
        }
        // At this point we know there is a problem.
        if (node.getLeftDifference() == 2 || node.getRightDifference() == 2) {
            return 1;
        }
        if (leftSideIncorrect) {
            if (node.getRight().getLeftDifference() == 2 && node.getRight().getRightDifference() == 2) {
                return 2;
            }
            else if (node.getRight().getRightDifference() == 1) {
                return 4;
            }
            else {
                return 6;
            }
        }
        else {
            if (node.getLeft().getRightDifference() == 2 && node.getLeft().getLeftDifference() == 2) {
                return 3;
            }
            else if (node.getLeft().getLeftDifference() == 1) {
                return 5;
            }
            else {
                return 7;
            }
        }

    }

    /**
     * Returns a string representing the structure of the subtree starting at the node.
     */
    private static String subtreeToString(WAVLNode node) {
        if (node == null) {
            return "";
        }
        return "[" +  subtreeToString(node.getLeft()) +
                node.toString() +
                subtreeToString(node.getRight()) + "]";
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
            differences = new int[]{1 ,1};

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

        /** #pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public void setDifference(int side, int difference) { this.differences[side] = difference; }

        /** #pre side == 0 || side == 1, side is either 0 for left or 1 for right */
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

        /** #pre side == 0 || side == 1, side is either 0 for left or 1 for right */
        public int getDifference(int side) { return this.differences[side]; }

        /** #pre side == 0 || side == 1, side is either 0 for left or 1 for right */
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
            return String.format("%1$d,%2$d,%3$d", differences[0], key, differences[1]);
        }
    }
}


