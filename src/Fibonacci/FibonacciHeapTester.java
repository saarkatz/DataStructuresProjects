package Fibonacci;

import java.util.Arrays;
import java.util.Random;

public class FibonacciHeapTester {
    public static void main(String [] args){
        Test1();
        Test2();

        testInsertDeleteMin();
        testInsertDecrease();
        testInsertDelete();
        testRandomSequence();
        testInsertAndMeld();
    }

    private static  void Test1(){
        System.out.println("Test1\n");

        FibonacciHeap heap =new FibonacciHeap();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        FibonacciHeap.HeapNode n = heap.insert(3);
        heap.insert(2);
        FibonacciHeap.HeapNode n2 = heap.insert(5);
        heap.insert(0);
        heap.insert(7);
        heap.insert(10);
        System.out.println("insertions (1,2,3,3,2,5,0,7,10):");
        printDetails(heap);

        deleteAndPrint(heap);

        System.out.println("decreaseKey of 5 to -1 :");
        heap.decreaseKey(n2,6);
        printDetails(heap);

        deleteAndPrint(heap);

        System.out.println("decreaseKey of 3 to -1 :");
        heap.decreaseKey(n,4);
        printDetails(heap);

        deleteAndPrint(heap);
    }

    private static void Test2(){
        System.out.println("Test2\n");
        System.out.println("insert 1000 randoms:");
        FibonacciHeap heap =new FibonacciHeap();

        for (int i = 0; i < 1000; i++) {
            heap.insert(new Random().nextInt(5000));
        }

        deleteAndPrint(heap);
        deleteAndPrint(heap);
        deleteAndPrint(heap);
    }

    private static void testInsertDeleteMin(){
        System.out.println("testInsertDeleteMin" );

        FibonacciHeap heap = new FibonacciHeap();
        int randNum;

        for(int i=0; i < 20000; i++){
            randNum = new Random().nextInt(50000);
            heap.insert(randNum);
        }

        for(int i=0;i<10000;i++) {
            heap.deleteMin();
            if (!heap.check()) {
                System.out.println("err testInsertDeleteMin");
                return;
            }
        }

        System.out.println("testInsertDeleteMin is fine");
        printDetails(heap);
    }

    private static void testInsertDecrease(){
        System.out.println("testInsertDecrease" );

        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode[] h = new FibonacciHeap.HeapNode[20000];
        int randNum;

        for(int i=0;i<20000;i++){
            randNum = new Random().nextInt(50000);
            h[i]=heap.insert(randNum);
        }


        for(int i=0;i<10000;i++) {
            randNum = new Random().nextInt(10000);
            if(h[randNum]!=null)
                heap.decreaseKey(h[randNum], 7000);
            if (!heap.check()) {
                System.out.println("err testInsertDecrease");
                return;
            }
        }

        System.out.println("testInsertDecrease is fine");
        printDetails(heap);
    }

    private static void testInsertDelete() {
        System.out.println("testInsertDelete" );

        FibonacciHeap heap =new FibonacciHeap();
        FibonacciHeap.HeapNode[] h=new FibonacciHeap.HeapNode[20000];
        int randNum;

        for(int i=0;i<20000;i++){
            randNum = new Random().nextInt(50000);
            h[i]=heap.insert(randNum);
        }

        for(int i=0;i<10000;i++) {
            if(h[i]!=null)
                heap.delete(h[i]);
            if (!heap.check()) {
                System.out.println("err testInsertDelete");
                return;
            }
        }

        heap.deleteMin();
        if (!heap.check()) {
            System.out.println("err testInsertDelete");
            return;
        }

        System.out.println("testInsertDelete is fine");
        printDetails(heap);
    }

    private static void testRandomSequence() {
        final int sequenceLength = 50000;
        System.out.println("testRandomSequence" );

        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode[] h = new FibonacciHeap.HeapNode[sequenceLength];
        int itemsHead = 0;
        int itemsTail = 0;
        int randNum;

        int expectedSize = 0;

        for (int i = 0; i < sequenceLength; i++) {
            if (itemsHead > itemsTail)
                randNum = new Random().nextInt(5);
            else
                randNum = 0;

            switch (randNum) {
                case 0:
                case 1:
                case 2:
                    h[itemsHead] = heap.insert(new Random().nextInt(sequenceLength/2));
                    itemsHead++;
                    expectedSize++;
                    break;
                case 3:
                    heap.delete(h[itemsTail]);
                    itemsTail++;
                    expectedSize--;
                    break;
                case 4:
                    heap.decreaseKey(h[itemsTail + new Random().nextInt(itemsHead - itemsTail)], new Random().nextInt(itemsTail + 2));
                    break;
            }
        }

        if (expectedSize != heap.size()) {
            System.out.println("size err testRandomSequence");
            return;
        }

        if (!heap.check()) {
            System.out.println("check err testRandomSequence");
            return;
        }

        System.out.println("testRandomSequence is fine");
        printDetails(heap);
    }

    private static void testInsertAndMeld() {
        final int MaxSize = 30000;
        System.out.println("testInsertAndMeld" );

        FibonacciHeap heap1 =new FibonacciHeap();
        int size1 = new Random().nextInt(MaxSize);
        FibonacciHeap heap2 =new FibonacciHeap();
        int size2 = new Random().nextInt(MaxSize);
        int randNum;

        for (int i = 0; i < size1; i++){
            randNum = new Random().nextInt(MaxSize);
            heap1.insert(randNum);
        }
        for (int i = 0; i < size2; i++){
            randNum = new Random().nextInt(MaxSize);
            heap2.insert(randNum);
        }

        randNum = new Random().nextInt((int)Math.log(MaxSize) + 1);
        for (int i = 0; i < randNum;i++) {
            heap1.deleteMin();
            heap2.deleteMin();
        }

        heap1.meld(heap2);

        if (!heap1.check()) {
            System.out.println("heap1 err testInsertAndMeld");
            return;
        }

        if (!heap2.check()) {
            System.out.println("heap2 err testInsertAndMeld");
            return;
        }

        System.out.println("testInsertAndMeld is fine");
        System.out.println("Heap1:");
        printDetails(heap1);
        System.out.println("Heap2:");
        printDetails(heap2);
    }

    private static void deleteAndPrint(FibonacciHeap heap){
        heap.deleteMin();
        System.out.println("deleteMin: ");
        printDetails(heap);
    }

    private static void printDetails(FibonacciHeap heap){
        System.out.println(getDetails(heap));
    }

    public static String getDetails(FibonacciHeap heap){
        StringBuilder builder = new StringBuilder();
        builder.append("size = ").append(heap.size()).append("\n");
        builder.append("countersRep = ").append(Arrays.toString(heap.countersRep())).append("\n");
        builder.append("check = ").append(heap.check()).append("\n");
        builder.append("potential = ").append(heap.potential()).append("\n");
        return builder.toString();
    }
}
