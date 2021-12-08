import java.util.*;

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
        int[] contarr = new int[]{};
        ArrayList<Integer> random = new ArrayList<>();
//        int[] random = new int[]{};
        for (int j=1;j<=5;j++){
//            int num= 1000 * (int)(Math.pow(2,j));
            int num = 5;
            contarr = new int[num];
            for (int i=0;i<contarr.length;i++){
                int k= contarr.length-i;
                contarr[i]=k;
                random.add(i+1);
            }
            Collections.shuffle(random);
            AVLTree avlTree = new AVLTree();
            for (int val : contarr) {
                avlTree.insertFingerTree(val, "" + val);
//                avlTree.bfs_print();
            }
            System.out.println("reversed array" + avlTree.counter);
            AVLTree avlTreerand = new AVLTree();
            for (int val : random) {
                avlTreerand.insertFingerTree(val, "" + val);
                avlTreerand.bfs_print();
            }
            System.out.println("random array" + avlTreerand.counter);

//            int randNum;
//            Random rand = new Random();
//            random = new int[10];
//            for (int i=0;i<10;i++){
//                randNum=rand.nextInt(1,10+1);
//                while (Arrays.asList(random).contains(randNum)){
//                    randNum=rand.nextInt(1,10+1);
//                }
//                random[i]=randNum;
//            }
//            AVLTree avlTreerand = new AVLTree();
//            for (int val : random) {
//                avlTreerand.insertFingerTree(val, "" + val);
//                avlTreerand.bfs_print();
//            }
//

        }

//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61};

//        InsertionSort ob = new InsertionSort();
//        System.out.println(ob.sort(contarr));
//        myTester tester = new myTester();
//        System.out.println(tester.search().getKey());
    }
}
