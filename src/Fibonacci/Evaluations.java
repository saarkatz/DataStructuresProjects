package Fibonacci;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.System.nanoTime;

public class Evaluations {
    private final static int BASE_SIZE = 1000;
    private final static String HEADER_FORMAT = "%-4s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s%n";
    private final static String BODY_FORMAT = "%-4d\t%-10d\t%-10f\t%-10d\t%-10d\t%-10d%n";
    private final static String HEADER = String.format(HEADER_FORMAT, "No.", "No. items", "Run-Time",
            "totalLinks", "totalCuts", "Potential");
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("evaluations_Fibo.txt");
        writer.write(HEADER);
        long timeStart;
        long timeEnd;
        for (int i = 0; i <= 3 ; i++) {
            FibonacciHeap heap = new FibonacciHeap();
            int size = i * BASE_SIZE;
            if (i == 0) {
                size = 1000;
            }

            // Fill the heap with values
            timeStart = nanoTime();
            for (int j = 0; j < size; j++) {
                heap.insert(size - j);
            }
            timeEnd = nanoTime();
            long elapsedTime = timeEnd - timeStart;

            writer.write(String.format(BODY_FORMAT, i, size, elapsedTime / 1000000000.0, FibonacciHeap.totalLinks(),
                    FibonacciHeap.totalCuts(), heap.potential()));
        }

        writer.write(HEADER);

        int linksOffset = 0;
        for (int i = 1 ; i <= 3 ; i++) {
            FibonacciHeap heap = new FibonacciHeap();
            int size = i * BASE_SIZE;
            if (i == 0) {
                size = 1000;
            }

            // Fill the heap with values
            long start = nanoTime();
            for (int j = 0; j < size; j++) {
                heap.insert(size - j);
            }
            for (int k = 0; k < size/2; k++) {
                heap.deleteMin();
            }
            long elapsedTime = nanoTime() - start;

            if (i == 0) {
                linksOffset = FibonacciHeap.totalLinks();
            }

            writer.write(String.format(BODY_FORMAT, i, size, elapsedTime / 1000000000.0, FibonacciHeap.totalLinks() - linksOffset,
                    FibonacciHeap.totalCuts(), heap.potential()));
            //writer.write(FibonacciHeapTester.getDetails(heap));
        }

        writer.flush();
        writer.close();
    }
}
