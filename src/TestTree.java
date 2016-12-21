public class TestTree {
    static int testNum = 1;
    final static boolean VERBOSE = false;

    public static void main(String[] args) {
        WAVLTree tree = new WAVLTree();
        TestInsert(tree, 5, 0, "[1,5,1]");
        TestInsert(tree, 3, 1, "[[1,3,1]1,5,2]");
        TestInsert(tree, 13, 0, "[[1,3,1]1,5,1[1,13,1]]");
        TestInsert(tree, 27, 2, "[[1,3,1]2,5,1[2,13,1[1,27,1]]]");
        TestInsert(tree, 50, 3, "[[1,3,1]2,5,1[[1,13,1]1,27,1[1,50,1]]]");
        TestDelete(tree, 27, 0, "[[1,3,1]2,5,1[[1,13,1]1,50,2]]");
        TestDelete(tree, 50, 0, "[[1,3,1]2,5,2[1,13,1]]");
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
            throw new RuntimeException("[Error " + String.valueOf(testNum) + "]: Exception was" +
                    "thrown during run of insert(" + String.valueOf(key) + ").\n" +
                    "Message:\n" + rte.toString());
        }
        if (numOperations != rebalancingOperations && (VERBOSE || verbose)) {
            System.out.println("[Warning " + String.valueOf(testNum) + "]: Number of rebalancing " +
                    "operations (" + String.valueOf(numOperations) +
                    ") in insert(" + String.valueOf(key) + ") does not match " +
                    "expected value (" + String.valueOf(rebalancingOperations) + ").");
        }
        String result = tree.toString();
        int length = Math.min(result.length(), expected.length());
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                throw new RuntimeException("[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                        "after insert(" + String.valueOf(key) + ") is incorrect!\n" +
                        result + "\n" + String.format("%-" + String.valueOf(i) + "s\n", "^") +
                        expected);
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
            throw new RuntimeException("[Error " + String.valueOf(testNum) + "]: Exception was" +
                    "thrown during run of delete(" + String.valueOf(key) + ").\n" +
                    "Message:\n" + rte.toString());
        }
        if (numOperations != rebalancingOperations && (VERBOSE || verbose)) {
            System.out.println("[Warning " + String.valueOf(testNum) + "]: Number of rebalancing " +
                    "operations (" + String.valueOf(numOperations) +
                    ") in delete(" + String.valueOf(key) + ") does not match " +
                    "expected value (" + String.valueOf(rebalancingOperations) + ").");
        }
        String result = tree.toString();
        int length = Math.min(result.length(), expected.length());
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) != expected.charAt(i)) {
                throw new RuntimeException("[Error " + String.valueOf(testNum) + "]: Resulting tree " +
                        "after delete(" + String.valueOf(key) + ") is incorrect!\n\t\t" +
                        result + "\n\t\t" + String.format("%" + String.valueOf(i+1) + "s\n\t\t", "^") +
                        expected);
            }
        }
        testNum++;
    }
}

