public class myTester {
    public boolean testRemove() {
        AVLTree tree = new AVLTree();
        if (!tree.empty()) {
            return false;
        }
        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88};
        for (int val : values) {
            tree.insert(val, "" + val);
            tree.bfs_print();
        }
        return true;
    }


    public static void main(String[] args) {
        myTester tester = new myTester();
        tester.testRemove();
    }
}
