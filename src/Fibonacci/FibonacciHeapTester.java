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

        System.out.println("testInsertDelete is fine");
        printDetails(heap);
    }

    private static void deleteAndPrint(FibonacciHeap heap){
        heap.deleteMin();
        System.out.println("deleteMin: ");
        printDetails(heap);
    }

    private static void printDetails(FibonacciHeap heap){
        System.out.println("size = " + heap.size());
        System.out.println("countersRep = "+Arrays.toString(heap.countersRep()));
        System.out.println("check = " + heap.check());
        System.out.println("potential = " + heap.potential() + "\n");
    }


}
