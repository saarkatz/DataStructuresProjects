public class TestTree {
    static int testNum = 1;
    final static boolean VERBOSE = true;
    final static boolean ERROR_ON_MISTAKE = true;

    public static void main(String[] args) {
        WAVLTree tree = new WAVLTree();
        TestInsert(tree, 10, 0, "[1,10,1]");
        TestInsert(tree, 20, 1, "[2,10,1[1,20,1]]");
        TestInsert(tree, 30, 3, "[[1,10,1]1,20,1[1,30,1]]");
        TestInsert(tree, 40, 2, "[[1,10,1]2,20,1[2,30,1[1,40,1]]]");
        TestInsert(tree, 50, 3, "[[1,10,1]2,20,1[[1,30,1]1,40,1[1,50,1]]]");
        TestInsert(tree, 25, 5, "[[[1,10,1]1,20,1[1,25,1]]1,30,1[2,40,1[1,50,1]]]");
        System.out.println(tree);
        tree = new WAVLTree();
        TestInsert(tree, 50, 0, "[1,50,1]");
        TestInsert(tree, 40, 1, "[[1,40,1]1,50,2]");
        TestInsert(tree, 30, 3, "[[1,30,1]1,40,1[1,50,1]]");
        TestInsert(tree, 20, 2, "[[[1,20,1]1,30,2]1,40,2[1,50,1]]");
        TestInsert(tree, 10, 3, "[[[1,10,1]1,20,1[1,30,1]]1,40,2[1,50,1]]");
        TestInsert(tree, 25, 5, "[[[1,10,1]1,20,1[1,25,1]]1,30,1[2,40,1[1,50,1]]]");
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
        int i;
        for (i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                break;
            }
        }
        if ((i < length && result.charAt(i) != expected.charAt(i)) || result.length() != expected.length()) {
            String message = "[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                    "after insert(" + String.valueOf(key) + ") is incorrect!\n\t" +
                    result + "\n\t" + String.format("%" + String.valueOf(i+1) + "s\n\t", "^") +
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
        int i;
        for (i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                break;
            }
        }
        if ((i < length && result.charAt(i) != expected.charAt(i)) || result.length() != expected.length()) {
            String message = "[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                    "after delete(" + String.valueOf(key) + ") is incorrect!\n\t\t" +
                    result + "\n\t\t" + String.format("%" + String.valueOf(i+1) + "s\n\t\t", "^") +
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

