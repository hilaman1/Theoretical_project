public class myTester {
    ActualAVLTree actualTree;
    AVLTree avlTree;

    int actualOperations;
    int avlOperations;

    int[] valuesTemp;
    int[] values;
    int[] values3;
    int[] values4;

    private boolean caseDelAll() {
        avlTree = new AVLTree();
        for (int aValues4: values4){
            avlTree.insert(aValues4, ""+aValues4);
        }
        int n = 0;
        for (int aValues4 : values4) {
            avlOperations += avlTree.delete(values4[aValues4 - 1]);
            if (avlTree.size() > 0) {
                // while avlTree is not empty, checking the min & max values
                if ((!avlTree.max().equals(avlTree.max())) ||
                        (!avlTree.min().equals(avlTree.min()))) {
                    n++;
                }
            } else {
                // if all items were deleted from avlTree, check if RBTree is empty as well
                if (!avlTree.empty()) {
                    n++;
                }
            }
        }
        for (int val : values4) {
            // checking that all the values that were supposed to be deleted are not in the RBTree
            if (!(avlTree.search(val) == null)) {
                n++;
            }
        }
        return (n == 0);
    }
//    public boolean testRemove() {
//        AVLTree tree = new AVLTree();
//        if (!tree.empty()) {
//            return false;
//        }
//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88};
//        for (int val : values) {
//            tree.insert(val, "" + val);
//            tree.bfs_print();
//        }
//        return true;
//    }


    public static void main(String[] args) {
        myTester tester = new myTester();
        tester.caseDelAll();
    }
}
