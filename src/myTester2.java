import java.util.ArrayList;
import java.util.Collections;

public class myTester2 {
    public static void main(String[] args) {
        int[] contarr = new int[]{};
        AVLTree avlTreerand = new AVLTree();
        AVLTree avlTreeMax = new AVLTree();
        AVLTree getMax = new AVLTree();
        ArrayList<Integer> random = new ArrayList<>();
        for (int j = 1; j <= 10; j++) {
            int num = 1000 * (int) (Math.pow(2, j));
            contarr = new int[num];
            random = new ArrayList<>();
            for (int i = 0; i < contarr.length; i++) {
                int k = contarr.length - i;
                contarr[i] = k;
                random.add(i + 1);
            }
            Collections.shuffle(random);
            avlTreerand = new AVLTree();
            avlTreeMax = new AVLTree();
            getMax = new AVLTree();
            for (int val : random) {
//                avlTreerand.insertFingerTree(val, "" + val);
//                avlTreeMax.insertFingerTree(val, "" + val);
//                getMax.insertFingerTree(val, "" + val);
                avlTreerand.insert(val, "" + val);
                avlTreeMax.insert(val, "" + val);
                getMax.insert(val, "" + val);
            }
            Collections.shuffle(random);
            int rand = random.get(0);
//            split in random key
            avlTreerand.split(rand);
//                System.out.println("random tree cost of join :"+avlTreerand.joinCounter);
//                System.out.println("number of join calls so far :"+avlTreerand.numOfJoins);
//                System.out.println("");
            AVLTree[] TreeArr = getMax.split(getMax.root.getKey());
            AVLTree.IAVLNode maxnode = TreeArr[0].maxNode;
//            split in .left maxNode
            avlTreeMax.split(maxnode.getKey());
//                System.out.println("maxNode tree cost of join :"+avlTreeMax.joinCounter);
//                System.out.println("number of join calls so far :"+avlTreeMax.numOfJoins);
//                System.out.println("");
            System.out.println("max Random tree cost of join for j="+j +" is "+ avlTreerand.MAxjoinCounter);
            System.out.println("");

            System.out.println("max left tree cost of join for j="+j+" is "+avlTreeMax.MAxjoinCounter);
            System.out.println("");
//
        }



    }
}

