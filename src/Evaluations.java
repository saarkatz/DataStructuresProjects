import java.util.Random;

/**
 * Created by Oren on 20/12/2016.
 */
public class evaluations {

    public static void main(String[] args) {
        WAVLTree tree = new WAVLTree();
        for (int i=1 ; i < 11 ; i++) {
            int rebalancingOps = 0;
            int n = i*10000;
            Random rand = new Random(100000);
            int j = 0;
            while (j < n) {
                int num = rand.nextInt();
                if (tree.search(num) == null) {
                    rebalancingOps += tree.insert(num, "");
                    j++;
                }
            }
            System.out.println("Number of rebalancing operations for " + n + " random unique insertions is:      " + rebalancingOps);
            int[] keysArr = tree.keysToArray();
            rebalancingOps = 0;
            for (int k=0 ; k < n ; k++) {
                rebalancingOps += tree.delete(keysArr[k]);
            }
            System.out.println("Number of rebalancing operations for " + n + " deletions from small to large is: " + rebalancingOps);
        }
    }
}
