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

    public AVLTree.IAVLNode search() {
        AVLTree avlTree = new AVLTree();
        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88};
//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61};
        for (int val : values) {
            avlTree.insertFingerTree(val, "" + val);
            //avlTree.bfs_print();
        }
//        AVLTree.IAVLNode max = avlTree.findMax();
        return avlTree.treePositionMax(avlTree.findMax(), 27);
    }

    public static void main(String[] args) {
//        AVLTree avlTree = new AVLTree();
//        int[] arrDiff = new int[6];
////        int[] arr = new int[]{4, 3, 2, 1};
//        int[] arr = new int[]{5, 2, 1, 3,4,0};
//        for (int i = 0; i < arr.length; i++) {
//            avlTree.insertFingerTree(arr[i], "" + i);
//            arrDiff[i] = avlTree.counter;
//            avlTree.bfs_print();
//        }
//
//        System.out.println("number of searches is:" + avlTree.counter);
//    }
//}
        int[] contarr = new int[]{};
        ArrayList<Integer> random = new ArrayList<>();
//        int[] random = new int[]{};
        for (int j = 1; j <= 5; j++) {
            int num = 1000 * (int) (Math.pow(2, j));
//            int num = 10000 * j;
//            int num = 5;
//            int num = 2*j;
            contarr = new int[num];
            random = new ArrayList<>();
            for (int i = 0; i < contarr.length; i++) {
                int k = contarr.length - i;
                contarr[i] = k;
                random.add(i + 1);
            }
            Collections.shuffle(random);

            AVLTree avlTree = new AVLTree();
            for (int val : contarr) {
                avlTree.insertFingerTree(val, "" + val);
            }
            System.out.println("reversed array" + avlTree.counter);

            AVLTree avlTreerand = new AVLTree();
            for (int val : random) {
                avlTreerand.insertFingerTree(val, "" + val);
            }
            System.out.println("random array" + avlTreerand.counter);

//            creating an insertion sort object in order to sort the arrays without a tree
            InsertionSort ob = new InsertionSort();
            ob.sort(contarr);
            System.out.println("number of flips of inversed array is:" + ob.counter);

            InsertionSort ob2 = new InsertionSort();
            int[] array = new int[random.size()];
            for(int i = 0; i < random.size(); i++){
                array[i] = random.get(i);
            }
            ob2.sort(array);
            System.out.println("number of flips of random array is:" + ob2.counter);
//        System.out.println(ob.sort(arr));
        }
    }
}
//            avlTreerand.bfs_print();


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

//    }

//        int[] values = new int[]{16, 24, 36, 19, 44, 28, 61};

//        InsertionSort ob = new InsertionSort();
//        System.out.println(ob.sort(contarr));
//        myTester tester = new myTester();
//        System.out.println(tester.search().getKey());
//    }

