import java.util.Arrays;
import java.util.Comparator;

public class TestTree {
    private static int testNum = 1;
    private static String testSuiteName = "";
    private static boolean testSuiteFailed = false;
    private static boolean testSuiteAborted = false;

    final static boolean VERBOSE = true;
    final static boolean ERROR_ON_MISTAKE = true;

    public static void main(String[] args) {
        StartTestSuite("Insert 1");
        WAVLTree tree = new WAVLTree();
        TestSize(tree, 0);
        TestInsert(tree, 10, 0, "[1,10,1]");
        TestSize(tree, 1);
        TestInsert(tree, 20, 1, "[2,10,1[1,20,1]]"); // Case 1
        TestInsert(tree, 30, 3, "[[1,10,1]1,20,1[1,30,1]]"); // Case 2
        TestInsert(tree, 40, 2, "[[1,10,1]2,20,1[2,30,1[1,40,1]]]"); // Case 1
        TestInsert(tree, 50, 3, "[[1,10,1]2,20,1[[1,30,1]1,40,1[1,50,1]]]"); // Case 2
        TestInsert(tree, 25, 5, "[[[1,10,1]1,20,1[1,25,1]]1,30,1[2,40,1[1,50,1]]]"); // Case 3
        TestInsert(tree,  5, 3, "[[[[1,5,1]1,10,2]1,20,2[1,25,1]]1,30,2[2,40,1[1,50,1]]]"); // Case 1
        TestInsert(tree, 15, 0, "[[[[1,5,1]1,10,1[1,15,1]]1,20,2[1,25,1]]1,30,2[2,40,1[1,50,1]]]");
        TestInsert(tree, 27, 1, "[[[[1,5,1]1,10,1[1,15,1]]1,20,1[2,25,1[1,27,1]]]1,30,2[2,40,1[1,50,1]]]"); // Case 1
        // Trying to insert an existing key.
        TestInsert(tree, 27, -1, "[[[[1,5,1]1,10,1[1,15,1]]1,20,1[2,25,1[1,27,1]]]1,30,2[2,40,1[1,50,1]]]");
        TestSize(tree, 9);
        EndTestSuite();

        StartTestSuite("Delete 1");
        TestDelete(tree, 25, 0, "[[[[1,5,1]1,10,1[1,15,1]]1,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestSize(tree, 8);
        TestDelete(tree, 10, 0, "[[[2,5,1[1,15,1]]1,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestDelete(tree,  5, 0, "[[[1,15,1]2,20,2[1,27,1]]1,30,2[2,40,1[1,50,1]]]");
        TestDelete(tree, 50, 3, "[[[1,15,1]1,20,1[1,27,1]]1,30,2[1,40,1]]"); // Case 2
        TestDelete(tree, 27, 0, "[[[1,15,1]1,20,2]1,30,2[1,40,1]]");
        TestDelete(tree, 40, 2, "[[1,15,1]2,20,2[1,30,1]]"); // Case 3
        TestSize(tree, 3);
        TestInsert(tree, 17, 1, "[[2,15,1[1,17,1]]1,20,2[1,30,1]]");
        TestDelete(tree, 30, 3, "[[1,15,1]2,17,2[1,20,1]]"); // Case 4
        TestDelete(tree, 15, 1, "[2,17,1[1,20,1]]"); // Case 1
        TestDelete(tree, 17, 1, "[1,20,1]"); // Case 1
        TestSize(tree, 1);
        TestDelete(tree, 15, -1, "[1,20,1]"); // Delete a node that does not exists.
        TestSize(tree, 1);
        TestDelete(tree, 20, 0, "[]"); // Delete last node.
        TestSize(tree, 0);
        TestDelete(tree, 20, -1, "[]"); // Delete when root is null;
        TestSize(tree, 0);
        EndTestSuite();

        StartTestSuite("Insert 2");
        tree = new WAVLTree();
        TestInsert(tree, 50, 0, "[1,50,1]");
        TestSize(tree, 1);
        TestInsert(tree, 40, 1, "[[1,40,1]1,50,2]"); // Case 1
        TestInsert(tree, 30, 3, "[[1,30,1]1,40,1[1,50,1]]"); // Case 2
        TestInsert(tree, 20, 2, "[[[1,20,1]1,30,2]1,40,2[1,50,1]]"); // Case 1
        TestInsert(tree, 10, 3, "[[[1,10,1]1,20,1[1,30,1]]1,40,2[1,50,1]]"); // Case 2
        TestInsert(tree, 35, 5, "[[[1,10,1]1,20,2]1,30,1[[1,35,1]1,40,1[1,50,1]]]"); // Case 3
        TestInsert(tree, 55, 3, "[[[1,10,1]1,20,2]2,30,1[[1,35,1]2,40,1[2,50,1[1,55,1]]]]"); // Case 1
        TestInsert(tree, 45, 0, "[[[1,10,1]1,20,2]2,30,1[[1,35,1]2,40,1[[1,45,1]1,50,1[1,55,1]]]]");
        TestInsert(tree, 32, 1, "[[[1,10,1]1,20,2]2,30,1[[[1,32,1]1,35,2]1,40,1[[1,45,1]1,50,1[1,55,1]]]]"); // Case 1
        TestSize(tree, 9);
        EndTestSuite();

        StartTestSuite("Delete 2");
        TestDelete(tree, 35, 0, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,1[[1,45,1]1,50,1[1,55,1]]]]");
        TestSize(tree, 8);
        TestDelete(tree, 50, 0, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,1[[1,45,1]1,55,2]]]");
        TestDelete(tree, 45, 1, "[[[1,10,1]1,20,2]2,30,1[[1,32,1]2,40,2[1,55,1]]]");
        TestDelete(tree, 10, 3, "[[1,20,1]2,30,1[[1,32,1]1,40,1[1,55,1]]]"); // Case 2
        TestDelete(tree, 32, 0, "[[1,20,1]2,30,1[2,40,1[1,55,1]]]");
        TestDelete(tree, 20, 2, "[[1,30,1]2,40,2[1,55,1]]"); // Case 3
        TestSize(tree, 3);
        TestInsert(tree, 47, 1, "[[1,30,1]2,40,1[[1,47,1]1,55,2]]");
        TestDelete(tree, 30, 3, "[[1,40,1]2,47,2[1,55,1]]"); // Case 4
        TestDelete(tree, 55, 1, "[[1,40,1]1,47,2]"); // Case 1
        TestDelete(tree, 47, 1, "[1,40,1]"); // Case 1);
        TestSize(tree, 1);
        EndTestSuite();

        System.out.println("Done!");
    }

    static void StartTestSuite(String name) {
        testSuiteName = name;
        testSuiteFailed = false;
        testSuiteAborted = false;
        testNum = 1;
    }

    static void EndTestSuite() {
        if (testSuiteFailed) {
            System.out.println("End test suite: " + testSuiteName + "\n");
        }
        else {
            System.out.println("Test suite " + testSuiteName + " finished with no errors\n");
        }
    }

    static void TestSize(WAVLTree tree, int expected) {
        if (testSuiteAborted) {
            return;
        }
        StringBuilder message = new StringBuilder();
        if (!isValueCorrect(Level.WARNING, "Tree size", "size()", expected, tree.size(), message)) {
            if (!testSuiteFailed) {
                System.out.print(testSuiteName + ":");
                testSuiteFailed = true;
            }
            System.out.println(message);
        }
    }

    static void TestInsert(WAVLTree tree, int key, int rebalancingOperations, String expected) {
        TestInsert(tree, key, rebalancingOperations, expected, false);
    }

    static void TestInsert(WAVLTree tree, int key, int rebalancingOperations, String expected, boolean verbose) {
        if (testSuiteAborted) {
            return;
        }
        StringBuilder message = new StringBuilder();
        boolean failed = false;
        int numOperations;
        try {
            numOperations = tree.insert(key, String.valueOf(key));
        }
        catch (RuntimeException rte) {
            if (!testSuiteFailed) {
                System.out.print(testSuiteName + ":");
                testSuiteFailed = true;
            }
            System.out.println(errorWasThrown(Level.ERROR, rte, String.format("insert(%d)", key)));
            testSuiteAborted = true;
            return;
        }
        if ((VERBOSE || verbose) &&
                !isValueCorrect(Level.WARNING, "Number of rebalancing operations", String.format("insert(%d)", key),
                rebalancingOperations, numOperations, message)) {
            failed = true;
            message.append(String.format("%n"));
        }
        if (!isTreeCorrect(Level.ERROR, String.format("insert(%d)", key), expected, tree.toString(), message)) {
            failed = true;
            if (ERROR_ON_MISTAKE) {
                testSuiteAborted = true;
            }
            message.append(String.format("%n"));
        }
        if (failed) {
            if (!testSuiteFailed) {
                message.insert(0, String.format("%s:%n", testSuiteName));
                testSuiteFailed = true;
            }
            System.out.print(message);
        }
        testNum++;
    }

    static void TestDelete(WAVLTree tree, int key, int rebalancingOperations, String expected) {
        TestDelete(tree, key, rebalancingOperations, expected, false);
    }

    static void TestDelete(WAVLTree tree, int key, int rebalancingOperations, String expected, boolean verbose) {
        if (testSuiteAborted) {
            return;
        }
        StringBuilder message = new StringBuilder();
        boolean failed = false;
        int numOperations;
        try {
            numOperations = tree.delete(key);
        }
        catch (RuntimeException rte) {
            if (!testSuiteFailed) {
                System.out.println(testSuiteName + ":");
                testSuiteFailed = true;
            }
            System.out.println(errorWasThrown(Level.ERROR, rte, String.format("delete(%d)", key)));
            testSuiteAborted = true;
            return;
        }
        if ((VERBOSE || verbose) &&
                !isValueCorrect(Level.WARNING, "Number of rebalancing operations", String.format("delete(%d)", key),
                        rebalancingOperations, numOperations, message)) {
            failed = true;
            message.append(String.format("%n"));
        }
        if (!isTreeCorrect(Level.ERROR, String.format("delete(%d)", key), expected, tree.toString(), message)) {
            failed = true;
            if (ERROR_ON_MISTAKE) {
                testSuiteAborted = true;
            }
            message.append(String.format("%n"));
        }
        if (failed) {
            if (!testSuiteFailed) {
                message.insert(0, String.format("%s:%n", testSuiteName));
                testSuiteFailed = true;
            }
            System.out.print(message);
        }
        testNum++;
    }

    static StringBuilder errorWasThrown(Level level, RuntimeException rte, String runOf) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%s %d]: Exception was thrown during run of '%s'.%n",
                level, testNum, runOf));
        builder.append(rte);
        return builder;
    }
    static <T> boolean isValueCorrect(Level level, String valueName, String in, T expected, T result,
                                                       StringBuilder message) {
        if (!expected.equals(result)) {
            message.append(String.format("[%s %d]: %s in %s does not match! expected %s, got %s",
                    level, testNum, valueName, in, expected, result));
            return false;
        }
        return true;
    }
    static boolean isTreeCorrect(Level level, String after, String expected, String result, StringBuilder message) {
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
            message.append(String.format("[%s %d]: ", level, testNum));
            message.append(String.format("Resulting tree after %s is incorrect%n\t\t", after));
            message.append(String.format("%s%n\t\t%s%n\t\t%s", result, String.valueOf(diff), expected));
            return false;
        }
        return true;
    }

    private enum Level {
        //INFO("Info"),
        WARNING("Warning"),
        ERROR("Error");
        private String text;
        private Level(String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}

