package Fibonacci;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 */
public class FibonacciHeap {
    static int totalCuts;
    static int totalLinks;

    int size;
    HeapNode min;

    FibonacciHeap() {
        size = 0;
        min = null;
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
    	return null;//new HeapNode(); // should be replaced by student code
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin() { delete(findMin()); }

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
    	  return; // TODO should be replaced by student code
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
	    int[] arr = new int[42];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) {
        if (x.equals(min)) {
            //updating min is required and we know the new min is one of the other roots.
            HeapNode node = x.getNext();
            min = node;
            while (x != node) {
                //iterating over the roots and updating the new min.
                if (node.getKey() < min.getKey()) {
                    min = node;
                }
                node = node.getNext();
            }
        }
        HeapNode child = x.getChild();
        HeapNode node = child.getNext();
        //cascading cuts for all the children of x.
        cascadingCut(child);
        while (node != child) {
            cascadingCut(node);
            node = node.getNext();
        }
        //now x has no children, only left to disconnect it from the heap.
        x.setParent(null);
        x.getParent().setRank(x.getParent().getRank() - 1);
        if (x.getNext().equals(x)) {
            x.getParent().setChild(null);
        }
        else {
            x.getParent().setChild(x.getNext());
            x.getPrev().setNext(x.getNext());
            x.getNext().setPrev(x.getPrev());
        }
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) {
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() {
    	return 0; // should be replaced by student code
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
    	return totalLinks; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts() {
    	return totalCuts; // should be replaced by student code
    }
    
    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in
     * another file
     *
     */
    private static void cut(HeapNode x) {
        HeapNode parent = x.getParent();
        x.setParent(null);
        x.setMark(false);
        parent.setRank(parent.getRank() - 1);
        if (x.getNext() == x) {
            parent.setChild(null);
        }
        else {
            parent.setChild(x.getNext());
            x.getPrev().setNext(x.getNext());
            x.getNext().setPrev(x.getPrev());
        }
        totalCuts++;
    }
    private static void cascadingCut(HeapNode x) {
        HeapNode parent = x.getParent();
        cut(x);
        if (parent.getParent() != null) {
            if (parent.isMark() == false) {
                parent.setMark(true);
            }
            else {
                cascadingCut(parent);
            }
        }
    }

    public class HeapNode{
        private String value;
        private int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;

        public HeapNode(int key, String value) {
            this.value = value;
            this.key = key;
            rank = 0;
            mark = false;
            child = null;
            next = null;
            prev = null;
            parent = null;
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
