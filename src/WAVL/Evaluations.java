package WAVL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Evaluations {
    private final static int BASE_SIZE = 10000;
    private final static String HEADER_FORMAT = "%-4s\t%-8s\t%-10s\t%-10s\t%-10s\t%-10s%n";
    private final static String BODY_FORMAT = "%-4d\t%-8d\t%-10f\t%-10f\t%-10d\t%-10d%n";
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("evaluations.txt");
        writer.write(String.format(HEADER_FORMAT, "No.", "No. op.",
                "Avg. insert", "Avg. delete", "Max insert", "Max delete"));
        WAVLTree tree = new WAVLTree();
        for (int i = 1 ; i <= 10 ; i++) {
            Random rand = new Random(234567876534543L);
            int size = i * BASE_SIZE;
            Integer[] itemsOrder = new Integer[size];
            // Fill the list with values
            for (int j = 0; j < size; j++) {
                itemsOrder[j] = j;
            }
            // Shuffle the list
            Collections.shuffle(Arrays.asList(itemsOrder), rand);
            // Fill tree while counting number of re-balance operations and maximum.
            int currentOperaiotns;
            int iSumOperatoins = 0;
            int iMaxOperations = 0;
            for (int j = 0; j < size; j++) {
                currentOperaiotns = tree.insert(itemsOrder[j], "");
                iSumOperatoins += currentOperaiotns;
                if (currentOperaiotns > iMaxOperations) {
                    iMaxOperations = currentOperaiotns;
                }
            }
            int dSumOperatoins = 0;
            int dMaxOperations = 0;
            for (int j = 0; j < size; j++) {
                currentOperaiotns = tree.delete(j);
                dSumOperatoins += currentOperaiotns;
                if (currentOperaiotns > dMaxOperations) {
                    dMaxOperations = currentOperaiotns;
                }
            }
            writer.write(String.format(BODY_FORMAT, i, size,
                    (float)iSumOperatoins/size, (float)dSumOperatoins/size, iMaxOperations, dMaxOperations));
        }
        writer.flush();
        writer.close();
    }
}
