package Fibonacci;

public class SampleTester {

    public static void main(String[] args) {
        FibonacciHeap h = new FibonacciHeap();
        FibonacciHeap.HeapNode n = h.insert(100);
        if (n.getKey() == 100) {
            System.out.println("SampleTester finished successfully!");
        } else {
            System.out.println("Sample Test failed...");
        }
    }
}
