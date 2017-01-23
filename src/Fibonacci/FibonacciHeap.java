package Fibonacci;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 */
public class FibonacciHeap {
    private static int totalCuts = 0;
    private static int totalLinks = 0;

    private int numMarks;
    private int numRoots;

    private int size;
    private HeapNode min;

    FibonacciHeap() {
        size = 0;
        min = null;
        numRoots = 0;
        numMarks = 0;
    }

   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty() {
    	return size == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key) {
        HeapNode newNode = new HeapNode(key);
        HeapNode minNode = findMin();
        meld(newNode, minNode);
        if (newNode.getKey() < minNode.getKey()) {
           min = newNode;
        }
        numRoots++;
        size++;
        return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin() {
        delete(findMin());
    }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin() {
    	return min;
    }
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2) {
        HeapNode minNode2 = heap2.findMin();
        HeapNode minNode = findMin();
        meld(minNode2, minNode);

        // Keep all members updated.
        if (minNode2.getKey() < minNode.getKey()) {
            min = minNode2;
        }
        this.size = this.size() + heap2.size();
        this.numRoots = this.numRoots + heap2.numRoots;
        this.numMarks = this.numMarks + heap2.numMarks;

        // Empty heap2
        heap2.size = 0;
        heap2.min = null;
        heap2.numRoots = 0;
        heap2.numMarks = 0;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size() {
    	return size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep() {
	    int[] arr = new int[(int)Math.ceil(Math.log(size())/Math.log(2))];
        HeapNode node = findMin();
        for (int i = 0; i < numRoots; i++) {
            arr[node.getRank()]++;
            node = node.getNext();
        }
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) {
        if (size() == 1) {
            min = null;
            numRoots--;
            return;
        }
        if (x.getParent() != null) {
            cascadingCutCounting(x, false);
        }
        while (x.getChild() != null) {
            cutWithoutCounting(x.getChild());
        }
        min = x.getNext(); // In case x was min
        removeNodeFromList(x);
        numRoots--;

        onePassSuccessiveLinking();

        // Find min again.
        x = min;
        HeapNode node = min.getNext();
        while (!node.equals(x)) {
            //iterating over the roots and updating the new min.
            if (node.getKey() < min.getKey()) {
                min = node;
            }
            node = node.getNext();
        }
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) {
    	x.setRank(x.getRank() - delta);
        if(x.getParent() != null && x.getKey() < x.getParent().getKey()) {
            cascadingCut(x);
        }
        onePassSuccessiveLinking();
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() {
    	return numRoots + 2*numMarks;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks() {
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods).
    */
    public static int totalCuts() {
    	return totalCuts;
    }

    private void cutWithoutCounting(HeapNode x) {
        HeapNode parent = x.getParent();
        x.setParent(null);
        if (x.isMark()) {
            x.setMark(false);
            numMarks--;
        }
        parent.setRank(parent.getRank() - 1);
        if (x.getNext() == x) {
            parent.setChild(null);
        }
        else {
            parent.setChild(x.getNext());
            removeNodeFromList(x);
        }
        meld(x, findMin());
        numRoots++;
    }

    private void cut(HeapNode x) {
        cutWithoutCounting(x);
        totalCuts++;
    }
    private void cascadingCutCounting(HeapNode x, boolean doCount) {
        HeapNode parent = x.getParent();
        if (doCount) {
            cut(x);
        }
        else {
            cutWithoutCounting(x);
        }
        if (parent.getParent() != null) {
            if (parent.isMark() == false) {
                parent.setMark(true);
                numMarks++;
            }
            else {
                cascadingCutCounting(parent, doCount);
            }
        }
    }
    private void cascadingCut(HeapNode x) {
        cascadingCutCounting(x, true);
    }

    private HeapNode link(HeapNode node1, HeapNode node2) {
        if (node1.getRank() != node2.getRank()) {
            throw new RuntimeException("Attempting to link nodes of different ranks!");
        }
        if (node1.getKey() > node2.getKey()) {
            HeapNode temp = node1;
            node1 = node2;
            node2 = temp;
        }
        if (node1.getChild() != null) {
            meld(node1.getChild(), node2);
        }
        node1.setChild(node2);
        node2.setParent(node1);
        node1.setRank(node1.getRank() + 1);
        totalLinks++;
        return node1;
    }

    private void onePassSuccessiveLinking() {
        int arraySize = (int)(1.44 * Math.ceil(Math.log(size())/Math.log(2)) + 1); // ceil(log_2(size))
        HeapNode node = findMin();
        HeapNode prev;
        HeapNode heapArray[] = new HeapNode[arraySize];
        HeapNode heapOut = null;
        int initialNimRoots = numRoots;
        for (int i = 0; i < initialNimRoots; i++) {
            // Check if there is already a node with the same rank in the array
            if (heapArray[node.getRank()] == null) {
                heapArray[node.getRank()] = node;
                node = node.getNext();
            }
            // If it is the link it with the existing one one move them to the result
            else {
                prev = node;
                node = node.getNext();
                removeNodeFromList(prev);
                removeNodeFromList(heapArray[prev.getRank()]);
                prev = link(prev, heapArray[prev.getRank()]);

                // Add the linked node to the result
                if (heapOut == null) {
                    heapOut = prev;
                }
                else {
                    meld(heapOut, prev);
                }
                numRoots--;
                heapArray[node.getRank()] = null;
            }
        }
        if (heapOut != null) {
            for (int i = 0; i < arraySize; i++) {
                if (heapArray[i] != null) {
                    meld(heapOut, heapArray[i]);
                    break;
                }
            }
            min = heapOut;
        }
    }

    private void meld(HeapNode first, HeapNode second) {
        first.getPrev().setNext(second);
        second.getPrev().setNext(first);
        HeapNode firstPrev = first.getPrev();
        first.setPrev(second.getPrev());
        second.setPrev(firstPrev);
    }

    private void removeNodeFromList(HeapNode node) {
        node.getNext().setPrev(node.getPrev());
        node.getPrev().setNext(node.getNext());
        node.setNext(node);
        node.setPrev(node);
    }

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in
     * another file
     *
     */
    public class HeapNode {
        private String value;
        private int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;

        private HeapNode(int key, String value) {
            this.value = value;
            this.key = key;
            rank = 0;
            mark = false;
            child = null;
            next = null;
            prev = null;
            parent = null;
        }

        private HeapNode(int key) {
            this(key, null);
        }

        private boolean isMark() {
            return mark;
        }
        private HeapNode getChild() {
            return child;
        }
        private HeapNode getNext() {
            return next;
        }
        public int getKey() {
            return key;
        }
        private int getRank() {
            return rank;
        }
        private HeapNode getParent() {
            return parent;
        }
        private HeapNode getPrev() {
            return prev;
        }
        public String getValue() {
            return value;
        }
        private void setChild(HeapNode child) {
            this.child = child;
        }
        private void setKey(int key) {
            this.key = key;
        }
        private void setMark(boolean mark) {
            this.mark = mark;
        }
        private void setNext(HeapNode next) {
            this.next = next;
        }
        private void setParent(HeapNode parent) {
            this.parent = parent;
        }
        private void setPrev(HeapNode prev) {
            this.prev = prev;
        }
        private void setRank(int rank) {
            this.rank = rank;
        }
        private void setValue(String value) {
            this.value = value;
        }
    }
}
