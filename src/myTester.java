public class myTester {
    ActualAVLTree actualTree;
    AVLTree avlTree;

    int actualOperations;
    int avlOperations;

    int[] valuesTemp;
    int[] values;
    int[] values3;
    int[] values4;

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
        if (!tree.min().equals("16")) {
            return false;
        }
        System.out.println("passed this 1");
        if (!tree.max().equals("93")) {
            return false;
        }
        System.out.println("passed this 2_"+tree.maxNode.getValue());
        tree.bfs_print();
        if (!checkBalanceOfTree(tree.getRoot())) {
            System.out.println("check here 3");
            return false;
        }
        System.out.println("passed this 3");
        if (!checkOrderingOfTree(tree.getRoot())) {
            System.out.println("check here");
            return false;
        }
        System.out.println("passed this 4");
        tree.delete(88);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 5");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 6");
        if (tree.search(88) != null) {
            return false;
        }
        System.out.println("passed this 7");

        tree.delete(19);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 8");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 9");
        if (tree.search(19) != null) {
            return false;
        }
        System.out.println("passed this 10");
        tree.delete(16);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 11");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 12");
        if (tree.search(16) != null) {
            return false;
        }
        System.out.println("passed this 13");
        tree.delete(28);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 14");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 15");
        if (tree.search(16) != null) {
            return false;
        }
        System.out.println("passed this 16");
        tree.delete(24);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 17");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 18");
        if (tree.search(24) != null) {
            return false;
        }
        System.out.println("passed this 19");
        tree.delete(36);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 20");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 21");
        if (tree.search(36) != null) {
            return false;
        }
        System.out.println("passed this 22");
        tree.delete(52);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 23");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 24");
        if (tree.search(52) != null) {
            return false;
        }
        System.out.println("passed this 25");
        tree.delete(93);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 26");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 27");
        if (tree.search(93) != null) {
            return false;
        }
        System.out.println("passed this 28");
        tree.delete(86);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 29");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 30");
        if (tree.search(86) != null) {
            return false;
        }
        System.out.println("passed this 31");

        tree.delete(83);
        if (!checkBalanceOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 32");
        if (!checkOrderingOfTree(tree.getRoot())) {
            return false;
        }
        System.out.println("passed this 33");
        if (tree.search(83) != null) {
            return false;
        }
        System.out.println("passed this 34");
        return true;
    }
    private boolean checkOrderingOfTree(AVLTree.IAVLNode current) {
        if (current.getLeft().isRealNode()) {
            if (Integer.parseInt(current.getLeft().getValue()) > Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getLeft());
        } else if (current.getRight().isRealNode()) {
            if (Integer.parseInt(current.getRight().getValue()) < Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getRight());
        } else if (!current.getLeft().isRealNode() && !current.getRight().isRealNode())
            return true;

        return true;
    }
    public boolean checkBalanceOfTree(AVLTree.IAVLNode current) {
        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;
        if (current.getRight() != null) {
            balancedRight = checkBalanceOfTree(current.getRight());
            rightHeight = getDepth(current.getRight());
        }
        if (current.getLeft() != null) {
            balancedLeft = checkBalanceOfTree(current.getLeft());
            leftHeight = getDepth(current.getLeft());
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }
    private int getDepth(AVLTree.IAVLNode n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight() != null)
            rightHeight = getDepth(n.getRight());
        if (n.getLeft() != null)
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight) + 1;
    }


    public static void main(String[] args) {
        myTester tester = new myTester();
        tester.testRemove();
    }
}
