public class myTester {
    ActualAVLTree actualTree;
    AVLTree avlTree;

    int actualOperations;
    int avlOperations;

    int[] valuesTemp;
    int[] values;
    int[] values3;
    int[] values4;

    public AVLTree.IAVLNode search(){
        AVLTree avlTree = new AVLTree();
        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88};
//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61};
        for (int val : values) {
            avlTree.insertFingerTree(val, "" + val);
            avlTree.bfs_print();
        }
//        AVLTree.IAVLNode max = avlTree.findMax();
        return avlTree.treePositionMax(avlTree.findMax(), 27);
    }

    public static void main(String[] args) {
        int[] contarr = new int[0];
        for (int j=1;j<=5;j++){
            int num= 1000 * (int)(Math.pow(2,j));
            contarr = new int[num];
            for (int i=0;i<contarr.length;i++){
                int k= contarr.length-i;
                contarr[i]=k;
            }
        }
        AVLTree avlTree = new AVLTree();
//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61};
        for (int val : contarr) {
            avlTree.insertFingerTree(val, "" + val);
            avlTree.bfs_print();
        }
//        InsertionSort ob = new InsertionSort();
//        System.out.println(ob.sort(contarr));
//        myTester tester = new myTester();
//        System.out.println(tester.search().getKey());
    }
}
