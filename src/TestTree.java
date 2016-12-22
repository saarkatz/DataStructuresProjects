public class TestTree {
    static int testNum = 1;
    final static boolean VERBOSE = true;
    final static boolean ERROR_ON_MISTAKE = true;

    public static void main(String[] args) {
        WAVLTree tree = new WAVLTree();
        TestInsert(tree, 10, 0, "[1,10,1]");
        TestInsert(tree, 20, 1, "[2,10,1[1,20,1]]"); // Case 1
        TestInsert(tree, 30, 3, "[[1,10,1]1,20,1[1,30,1]]"); // Case 2
        TestInsert(tree, 40, 2, "[[1,10,1]2,20,1[2,30,1[1,40,1]]]"); // Case 1
        TestInsert(tree, 50, 3, "[[1,10,1]2,20,1[[1,30,1]1,40,1[1,50,1]]]"); // Case 2
        TestInsert(tree, 25, 5, "[[[1,10,1]1,20,1[1,25,1]]1,30,1[2,40,1[1,50,1]]]"); // Case 3
        TestInsert(tree,  5, 3, "[[[[1,5,1]1,10,2]1,20,2[1,25,1]]1,30,2[2,40,1[1,50,1]]]"); // Case 1
        TestInsert(tree, 15, 0, "[[[[1,5,1]1,10,1[1,15,1]]1,20,2[1,25,1]]1,30,2[2,40,1[1,50,1]]]");
        TestInsert(tree, 27, 1, "[[[[1,5,1]1,10,1[1,15,1]]1,20,1[2,25,1[1,27,1]]]1,30,2[2,40,1[1,50,1]]]"); // Case 1
        TestDelete(tree, 25, 0, "[[[[1,5,1]1,10,1[1,15,1]]1,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestDelete(tree, 10, 0, "[[[2,5,1[1,15,1]]1,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestDelete(tree,  5, 0, "[[[1,15,1]2,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestDelete(tree, 50, 3, "[[[1,15,1]1,20,1[1,27,1]]1,30,2[1,40,1]]"); // Case 2
        TestDelete(tree, 27, 0, "[[[1,15,1]1,20,2]1,30,2[1,40,1]]");
        TestDelete(tree, 40, 2, "[[1,15,1]2,20,2[1,30,1]]"); // Case 3
        TestInsert(tree, 17, 1, "[[2,15,1[1,17,1]]1,20,2[1,30,1]]");
        TestDelete(tree, 30, 3, "[[1,15,1]2,17,2[1,20,1]]"); // Case 4
        TestDelete(tree, 15, 1, "[2,17,1[1,20,1]]"); // Case 1
        System.out.println(tree);
        tree = new WAVLTree();
        TestInsert(tree, 50, 0, "[1,50,1]");
        TestInsert(tree, 40, 1, "[[1,40,1]1,50,2]"); // Case 1
        TestInsert(tree, 30, 3, "[[1,30,1]1,40,1[1,50,1]]"); // Case 2
        TestInsert(tree, 20, 2, "[[[1,20,1]1,30,2]1,40,2[1,50,1]]"); // Case 1
        TestInsert(tree, 10, 3, "[[[1,10,1]1,20,1[1,30,1]]1,40,2[1,50,1]]"); // Case 2
        TestInsert(tree, 35, 5, "[[[1,10,1]1,20,2]1,30,1[[1,35,1]1,40,1[1,50,1]]"); // Case 3
        TestInsert(tree, 55, 3, "[[[1,10,1]1,20,2]2,30,1[[1,35,1]2,40,1[2,50,1[1,55,1]]]"); // Case 1
        TestInsert(tree, 45, 0, "[[[1,10,1]1,20,2]2,30,1[[1,35,1]2,40,1[[1,45,1]1,50,1[1,55,1]]]");
        TestInsert(tree, 32, 1, "[[[1,10,1]1,20,2]2,30,1[[[1,32,1]1,35,2]1,40,1[[1,45,1]1,50,1[1,55,1]]]"); // Case 1
        TestDelete(tree, 35, 0, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,1[[1,45,1]1,50,1[1,55,1]]]");
        TestDelete(tree, 50, 0, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,1[2,45,1[1,55,1]]]");
        TestDelete(tree, 45, 0, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,2[1,55,1]]]");
        TestDelete(tree, 10, 3, "[[1,20,1]2,30,1[[1,32,1]1,40,1[1,55,1]]]"); // Case 2
        TestDelete(tree, 32, 0, "[[1,20,1]2,30,1[2,40,1[1,55,1]]]");
        TestDelete(tree, 20, 2, "[[1,30,1]2,40,2[1,55,1]]"); // Case 3
        TestInsert(tree, 47, 1, "[[1,30,1]2,40,1[[1,47,1]1,55,2]]");
        TestDelete(tree, 30, 3, "[[1,40,1]2,47,2[1,55,1]]"); // Case 4
        TestDelete(tree, 55, 3, "[[1,40,1]1,47,2]"); // Case 1
        System.out.println(tree);
        System.out.println("Done!");
    }

    static void TestInsert(WAVLTree tree, int key, int rebalancingOperations, String expected) {
        TestInsert(tree, key, rebalancingOperations, expected, false);
    }

    static void TestInsert(WAVLTree tree, int key, int rebalancingOperations, String expected, boolean verbose) {
        int numOperations;
        try {
            numOperations = tree.insert(key, "");
        } catch (RuntimeException rte) {
            System.err.println("[Error " + String.valueOf(testNum) + "]: Exception was " +
                    "thrown during run of insert(" + String.valueOf(key) + ").\n" +
                    "Message:\n" + rte.toString());
            throw rte;
        }
        if (numOperations != rebalancingOperations && (VERBOSE || verbose)) {
            System.err.println("[Warning " + String.valueOf(testNum) + "]: Number of rebalancing " +
                    "operations (" + String.valueOf(numOperations) +
                    ") in insert(" + String.valueOf(key) + ") does not match " +
                    "expected value (" + String.valueOf(rebalancingOperations) + ").");
        }
        String result = tree.toString();
        int length = Math.min(result.length(), expected.length());
        char[] diff = new char[length];
        boolean different = false;
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                diff[i] = '^';
                different = true;
            }
            else {
                diff[i] = ' ';
            }
        }
        if (different || result.length() != expected.length()) {
            String message = "[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                    "after insert(" + String.valueOf(key) + ") is incorrect!\n\t\t" +
                    result + "\n\t\t" + String.valueOf(diff) + "\n\t\t" +
                    expected;
            if (ERROR_ON_MISTAKE) {
                throw new RuntimeException(message);
            }
            else {
                System.err.println(message);
            }
        }
        testNum++;
    }

    static void TestDelete(WAVLTree tree, int key, int rebalancingOperations, String expected) {
        TestDelete(tree, key, rebalancingOperations, expected, false);
    }

    static void TestDelete(WAVLTree tree, int key, int rebalancingOperations, String expected, boolean verbose) {
        int numOperations;
        try {
            numOperations = tree.delete(key);
        }
        catch (RuntimeException rte) {
            System.err.println("[Error " + String.valueOf(testNum) + "]: Exception was " +
                    "thrown during run of delete(" + String.valueOf(key) + ").\n" +
                    "Message:\n" + rte.toString());
            throw rte;
        }
        if (numOperations != rebalancingOperations && (VERBOSE || verbose)) {
            System.err.println("[Warning " + String.valueOf(testNum) + "]: Number of rebalancing " +
                    "operations (" + String.valueOf(numOperations) +
                    ") in delete(" + String.valueOf(key) + ") does not match " +
                    "expected value (" + String.valueOf(rebalancingOperations) + ").");
        }
        String result = tree.toString();
        int length = Math.min(result.length(), expected.length());
        char[] diff = new char[length];
        boolean different = false;
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                diff[i] = '^';
                different = true;
            }
            else {
                diff[i] = ' ';
            }
        }
        if (different || result.length() != expected.length()) {
            String message = "[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                    "after delete(" + String.valueOf(key) + ") is incorrect!\n\t\t" +
                    result + "\n\t\t" + String.valueOf(diff) + "\n\t\t" +
                    expected;
            if (ERROR_ON_MISTAKE) {
                throw new RuntimeException(message);
            }
            else {
                System.err.println(message);
            }
        }
        testNum++;
    }
}

