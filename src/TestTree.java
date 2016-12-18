public class TestTree {
    public static void main(String[] args) {
        WAVLTree tree = new WAVLTree();
        System.out.println(tree);
        System.out.println(tree.insert(5, "")); // [1,5,1]
        System.out.println(tree);
        System.out.println(tree.insert(3, "")); // [[1,3,1]1,5,2]
        System.out.println(tree);
        System.out.println(tree.insert(13, "")); // [[1,3,1]1,5,1[1,13,1]]
        System.out.println(tree);
        System.out.println(tree.insert(27, "")); // [[1,3,1]2,5,1[2,13,1[1,27,1]]]
        System.out.println(tree);
        System.out.println(tree.insert(50, "")); // [[1,3,1]2,5,1[[1,13,1]1,27,1[1,50,1]]]]
        System.out.println(tree);
        System.out.println(tree.delete(27)); // [[1,3,1]2,5,1[2,13,1[1,50,1]]]
        System.out.println(tree);
        System.out.println(tree.delete(13)); // [[1,3,1]2,5,1[2,13,1[1,50,1]]]
        System.out.println(tree);
    }
}
