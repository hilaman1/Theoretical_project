import java.util.ArrayDeque;//todo-delete this
import java.util.Arrays;
import java.util.Queue;//todo-delete this

public class AVLTree {
    private IAVLNode root;
    private final IAVLNode EXTERNALNODE = new AVLNode(-1, null, null, null, -1,0);
    private IAVLNode maxNode; // pointer to the node with the maxNode key
    private IAVLNode minNode;

    /**
     * public AVLTree()
     * creates an empty AVLTree
     * complexity - O(1)
     */
    public AVLTree() {
        this.root=null;
        this.minNode=null;
        this.maxNode=null;
    }
    /**
     * public AVLTree(IAVLNode root)
     * creates a AVLTree with only one node
     * complexity - O(1)
     */
    public AVLTree(IAVLNode root) { // build a leaf
        this.root = root;
        root.setParent(null);
        this.maxNode = findMax();
        this.minNode = findMin();
    }

    /**
     * public boolean empty()
     * <p>
     * Returns true if and only if the tree is empty.
     */
    public boolean empty() {
        return this.root==null;
    }

    /**
     * private IAVLNode treePosition(IAVLNode nodeX, int k)
     * searches the node with key of k in the tree.
     * if exists, return -1
     * else, return the last node the methode encountered
     * complexity - O(logn)
     */
    private IAVLNode treePosition(IAVLNode nodeX, int k) {
        //needs to be implemented
        IAVLNode posNode = null;
        while (nodeX.isRealNode() && nodeX!=null){
            posNode=nodeX;
            if (k==nodeX.getKey()){
                return nodeX;
            }
            if(k<nodeX.getKey()){
                nodeX=nodeX.getLeft();
            }
            else {
                nodeX=nodeX.getRight();
            }
        }
        return posNode;
    }


    /**
     * public String search(int k)
     * <p>
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        if (this.root != null){
            IAVLNode x = treePosition(this.root,k);
            if (x != null && x.getKey() == k){
                return x.getValue();
            }
        }
        return null;
    }

    /*
     * returns an array of rank differences with parent of node
     */
    public static int[] rankDifference(IAVLNode node){
        return new int[] {node.getHeight() - node.getLeft().getHeight(),
                node.getHeight() - node.getRight().getHeight()};
    }

    /*
     * Supposed to be used like enums, without creating new classes
     * return which case are we at currently, to ease on dealing with symmetrical cases while inserting node
     */
    private String insertionCase(IAVLNode currNode){
        if (currNode != null && currNode.getParent() != null) {
            int[] rankDifCurrNode = rankDifference(currNode);
            int[] rankDiffParent = rankDifference(currNode.getParent());
            if (((Arrays.equals(rankDifCurrNode, new int[]{1, 1}) ||
                    (Arrays.equals(rankDifCurrNode, new int[]{2, 1})) ||
                    (Arrays.equals(rankDifCurrNode, new int[]{1, 2})))
                    && (Arrays.equals(rankDiffParent, new int[]{1, 0}) ||
                    (Arrays.equals(rankDiffParent, new int[]{0, 1}))))) {
                return "case1";
            }
            //single left rotation & demote(z)--> re-balancing completed return +2
            else if (Arrays.equals(rankDiffParent, new int[]{0, 2})
                    && Arrays.equals(rankDifCurrNode, new int[]{1, 2})) {
                return "case2left";
            }
            //double rotation: first right rotation (a-x) then left rotation (a-z)
            // & demote(x) ,demote(z), promote(a) & return +5 re-balancing completed
            else if (Arrays.equals(rankDifCurrNode, new int[]{1, 2})
                    && Arrays.equals(rankDiffParent, new int[]{2, 0})) {
                return "case2right";
            }
            //double rotation: first left rotation (x-b) then left rotation (b-z)
            // & demote(x) ,demote(z), promote(b) & return +5 re-balancing completed
            else if (Arrays.equals(rankDifCurrNode, new int[]{2, 1})
                    && Arrays.equals(rankDiffParent, new int[]{0, 2})) {
                return "case3left";
            }

            //single left rotation & demote(z)--> re-balancing completed return +2
            else if (Arrays.equals(rankDifCurrNode, new int[]{2, 1})
                    && Arrays.equals(rankDiffParent, new int[]{2, 0})) {
                return "case3right";
            }
            // special cases for join method
            else if (Arrays.equals(rankDifCurrNode, new int[]{1, 1})
                    && Arrays.equals(rankDiffParent, new int[]{0, 2})) {
                return "caseJoinLeft"; // special case for join
                // when the subtree of x (after join) is a left subtree to nodeC
                // sol: right rotation x-c & promote(x)
            }
            else if (Arrays.equals(rankDifCurrNode, new int[]{1, 1})
                    && Arrays.equals(rankDiffParent, new int[]{2, 0})) {
                return "caseJoinRight"; // special case for join
                // when the subtree of x (after join) is a right subtree to nodeC
                // sol: left rotation x-c & promote(x)
            }

        }
        return "";
    }
    /**
     * public int insert(int k, String i)
     * <p>
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        if(this.empty()){
            // if this is the first insert we should update its fields
            IAVLNode firstNode = new AVLNode(k, i, EXTERNALNODE, EXTERNALNODE, 0,1);
            this.root = firstNode;
            this.minNode=firstNode;
            this.maxNode=firstNode;
            return 0;
        }
        IAVLNode whereToInsertNode = treePosition(this.root, k); // comment that O(logn)
        int keyLastPos=whereToInsertNode.getKey();
        IAVLNode currNode = new AVLNode(k, i, EXTERNALNODE, EXTERNALNODE, 0,1);
        if (keyLastPos == k) // if the node exists in the tree
            return -1;
        else { // if the node doesnt exists in the tree
            currNode.setParent(whereToInsertNode);
            // check properties of the node we need to insert after.
            if (whereToInsertNode.isLeaf()) {
                if (keyLastPos > currNode.getKey()) {
                    whereToInsertNode.setLeft(currNode);
                } else {
                    whereToInsertNode.setRight(currNode);
                }
//                promote(whereToInsertNode);
            }
            else{ // whereToInsertNode is an Unary node
                if (whereToInsertNode.getRight().isRealNode() && !whereToInsertNode.getLeft().isRealNode()){
                    // only right son
                    whereToInsertNode.setLeft(currNode);}
                else {
                    whereToInsertNode.setRight(currNode);
                }
            }
            //maintaining fields

            reSize(whereToInsertNode,1);// recalculate size from parent to root
            if(currNode.getKey() < this.minNode.getKey()){
                this.minNode=currNode;
            }
            if(currNode.getKey()>this.maxNode.getKey()){
                this.maxNode=currNode;
            }
        }

        return rebalanceInsert(currNode);
    }

    private int rebalanceInsert(IAVLNode currNode) {
        // this is where the rebalancing proccess is done
        // return the number of operation needed in order to maintain the AVL invariants.
        int counter=0;

        String caseName = insertionCase(currNode);
        if (currNode.getParent() != null) {
            // in order to check rank diff between node and his parent we need to make sure its not null
            //}
            // check insertion cases

            switch (caseName) {
                case "case1":
                    // problem might moved up if so fix that- moving upward recursively
                    sizeCalc(currNode);
                    promote(currNode.getParent());
                    counter++;
                    counter+= rebalanceInsert(currNode.getParent());
                    break;
                case "case2left":
                    //single right rotation & demote(z)--> re-balancing completed
                    IAVLNode parent = currNode.getParent();
                    singleRightRotation(currNode);
                    sizeCalc(parent); //z- right child now
                    updateSize(currNode);//x-parent now
                    demote(parent);
                    counter+=2;
                    break;
                case "case3right":
                    //single left rotation & demote(z)--> re-balancing completed
                    IAVLNode parentx = currNode.getParent(); //cur parent
                    singleLeftRotation(currNode); //cur
//                this.bfs_print();
                    sizeCalc(parentx); //z- left child now
                    updateSize(currNode);//x-parent now
                    demote(parentx);
                    counter+=2;
                    break;
                case "case2right":
                    //double rotation: first right rotation (a-x) then left rotation (a-z)
                    // & demote(x) ,demote(z), promote(a) & return +5
                    IAVLNode parentxx = currNode.getParent(); // z
                    IAVLNode leftSonA = currNode.getLeft(); //a
                    singleRightRotation(leftSonA); //a
                    //after right rotation leftSonA become right son of parent
                    singleLeftRotation(leftSonA); //a
                    sizeCalc(parentxx);//z
                    sizeCalc(currNode);//x
                    updateSize(leftSonA);//a
                    demote(parentxx);
                    demote(currNode);
                    promote(leftSonA);
                    counter+=5;
                    break;
                case "case3left":
                    //double rotation: first left rotation (x-b) then left rotation (b-z)
                    // & demote(x) ,demote(z), promote(b) & return +5
                    IAVLNode parentxxx = currNode.getParent(); // z
                    IAVLNode rightSonB = currNode.getRight(); //b
                    singleLeftRotation(rightSonB);
                    // after left rotation leftSonB become left son of parent
                    singleRightRotation(rightSonB);
                    sizeCalc(parentxxx);//z
                    sizeCalc(currNode);//x
                    updateSize(rightSonB);//b
                    demote(parentxxx);
                    demote(currNode);
                    promote(rightSonB);
                    counter+= 5;
                    break;
                case "caseJoinLeft":
                    // when the subtree of x (after join) is a left subtree to nodeC
                    // sol: right rotation x-c & promote(x)
                    IAVLNode parentxxxx = currNode.getParent(); // c
                    singleRightRotation(currNode); // node is the root of joined subtree of x
                    sizeCalc(parentxxxx);//c
                    updateSize(currNode);//x
                    promote(currNode);
                    counter+=2;
                    counter += rebalanceInsert(currNode.getParent());
                    break;
                case "caseJoinRight":
                    // special case for join
                    // when the subtree of x (after join) is a right subtree to nodeC
                    // sol: left rotation x-c & promote(x)
                    IAVLNode parentxxxxx = currNode.getParent(); // c
                    singleLeftRotation(currNode);// node is the root of joined subtree of x
                    sizeCalc(parentxxxxx);//c
                    updateSize(currNode);//x
                    promote(currNode);
                    counter+=2;
                    counter += rebalanceInsert(currNode.getParent());
                    break;
            }
        }
        return counter;
    }

    private void demote(IAVLNode node) {
        node.setHeight(node.getHeight() - 1);
    }

    private void promote(IAVLNode node) {
        node.setHeight(node.getHeight() + 1);
    }

    /*
     * gets the node we want to delete
     * return which case are we at currently, to ease on dealing with symmetrical cases
     * Supposed to be used like enums, without creating new classes
     */
    private String deletetionCase(IAVLNode parent) {
        if (parent == null){
            return "";
        }
        // check cases of right / left child outside
        // TODO: 04/12/2021 create arrays one time in order to save in space complexity
        int[] rankDiffParent = rankDifference(parent);
        if (Arrays.equals(rankDiffParent, new int[]{2, 2})) {
            return "case1";
        }

        if (parent.getLeft().isRealNode()){
            int[] rankDiffLChild = rankDifference(parent.getLeft());
//      rotation right on z-y, promote(y) demote(z)
            //      return 3 + rebalance (parent)
            if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
//                    (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffLChild, new int[]{1, 1}))))) {
                return "case2left";
            }
            //      rotation right on z-y, 2xdemote(z)
            //      return 3 + rebalance (parent)
            if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
//                    (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffLChild, new int[]{1, 2}))))) {
                return "case3left";
            }
            //      rotation left on y-a, rotation right on a-z, 2xdemote(z), demote(y), promote(a)
            //      return 6 + rebalance (parent)
            if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
//                    (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffLChild, new int[]{2, 1}))))) {
                return "case4left";
            }
        }
        if (parent.getRight().isRealNode()){
            int[] rankDiffRChild = rankDifference(parent.getRight());
            //      rotation left on z-y, promote(y) demote(z)
            //      return 3 + rebalance (parent)
            //      parent is Unary
            if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
//                    (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffRChild, new int[]{1, 1}))))) {
                return "case2right";
            }

            //      rotation left on z-y, 2xdemote(z)
            //      return 3 + rebalance (parent)
            if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
//                    (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffRChild, new int[]{2, 1}))))) {
                return "case3right";
            }

            //      rotation right on y-a, rotation left on a-z, 2xdemote(z), demote(y), promote(a)
            //      return 6 + rebalance (parent)
            if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
//                    (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                    (Arrays.equals(rankDiffRChild, new int[]{1, 2}))))) {
                return "case4right";
            }
        }
        return "";
    }

    private void deleteLeaf(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        if (parent.getRight() == nodeToDelete) { // delete right child
            parent.setRight(EXTERNALNODE);
        } else { // delete left child
            parent.setLeft(EXTERNALNODE);
        }
    }

    /**
     * public int delete(int k)
     * <p>
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        int counter = 0;
        if(this.empty()){
            return -1; // the item wasnt found in tree because its empty
        }
        IAVLNode nodeToDelete = treePosition(this.root, k); // comment that O(logn)
        IAVLNode parent = nodeToDelete.getParent();
        if (nodeToDelete.getKey() != k){ // doesn't exist
            return -1;
        }else {
            if(this.root == nodeToDelete &&
                    !nodeToDelete.getRight().isRealNode() &&
                    !nodeToDelete.getLeft().isRealNode()){ // tree is just a leaf, no need to rebalance
                this.root = null;
                return 0;
            } else {
                if(nodeToDelete.isLeaf()){
                    deleteLeaf(nodeToDelete);
                    if (parent!=null){
                        reSize(parent,-1);
                    }
                    counter = rebalanceDelete(nodeToDelete.getParent());
                }
                else {
                    if (isUnary(nodeToDelete)){
                        if (nodeToDelete == this.root){
                            this.root = this.findSuccessor(nodeToDelete);
                        }
                        deleteUnary(nodeToDelete);
                        if (parent!=null){
                            reSize(parent,-1);
                        }
                        if(parent==null){ // nodeToDelete is the root so parent is null
                            //since we change the root to be the only son of the original root than
                            // size in new root is updated
                            counter = 0;// no rebalancing was needed (before delete the tree was balanced)
                        }
                        counter = rebalanceDelete(nodeToDelete.getParent());
                    }else {
                        IAVLNode replace = replaceWithSuccessor(nodeToDelete);
                        if (replace!=null){
                            reSize(replace, -1);
                        }
                        counter = rebalanceDelete(replace);
                    }
                }
            }
        }
        if (nodeToDelete.getKey() == this.minNode.getKey()){
            this.minNode = findSuccessor(this.minNode);
        }
        if (nodeToDelete.getKey() == this.maxNode.getKey()){
            this.maxNode =findMax();
        }
        return counter;
    }

    private IAVLNode findSuccessor(IAVLNode nodeToDelete) {
        if (nodeToDelete.getRight().isRealNode()) {
            IAVLNode successor = nodeToDelete.getRight();
            while (successor.getLeft().isRealNode()) {
                successor = successor.getLeft();
            }
            return successor;
        }else{
            IAVLNode successor = nodeToDelete.getParent();
            while (nodeToDelete.isRealNode() && nodeToDelete == successor.getRight()){
                nodeToDelete = successor;
                successor = nodeToDelete.getParent();
            }
            return successor;
        }
    }

    private boolean isUnary(IAVLNode nodeToDelete) {
        return (((!nodeToDelete.getRight().isRealNode()) && nodeToDelete.getLeft().isRealNode())
                || (nodeToDelete.getRight().isRealNode() && !nodeToDelete.getLeft().isRealNode()));
    }

    /**
     * resize from node to root
     * i = -1 if we delete
     * i = 1 if we insert
     */
    private void reSize(IAVLNode parent, int i) {
        while(parent != null) {
            parent.setSize(parent.getSize() + i);
            parent = parent.getParent();
        }
    }

    /**
     * recalculate Height for node
     * */
    public static void updateHeight(IAVLNode parent) {
        int maxHeight = Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight());
        parent.setHeight(maxHeight+1);
    }

    private void deleteUnary(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        if(parent == null){  // lace tree with 2 nodes
            if(nodeToDelete.getRight().isRealNode()){
                this.root = nodeToDelete.getRight();
                nodeToDelete.getRight().setParent(null);
            }
            if(nodeToDelete.getLeft().isRealNode()){
                this.root = nodeToDelete.getLeft();
                nodeToDelete.getLeft().setParent(null);
            }
        }
        else {
            if (parent.getRight() == nodeToDelete){
                if(nodeToDelete.getLeft().isRealNode()){
                    parent.setRight(nodeToDelete.getLeft());
                    nodeToDelete.getLeft().setParent(parent);
                }else {
                    parent.setRight(nodeToDelete.getRight());
                    nodeToDelete.getRight().setParent(parent);
                }
            }
            else {
                if(nodeToDelete.getRight().isRealNode()){
                    parent.setLeft(nodeToDelete.getRight());
                    nodeToDelete.getRight().setParent(parent);
                }else {
                    parent.setLeft(nodeToDelete.getLeft());
                    nodeToDelete.getLeft().setParent(parent);
                }
            }
        }
    }

    private IAVLNode replaceWithSuccessor(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        IAVLNode successor = findSuccessor(nodeToDelete);
        IAVLNode ParentSuccessor = successor.getParent();

        if (nodeToDelete != ParentSuccessor) {
            //successor is not right child of nodeToDelete
            nodeToDelete.getRight().setParent(successor);
            if (successor.getRight().isRealNode()) {
                ParentSuccessor.setLeft(successor.getRight());
                successor.getRight().setParent(ParentSuccessor);
            }else {
                ParentSuccessor.setLeft(EXTERNALNODE);
            }
            successor.setRight(nodeToDelete.getRight());
        }

        nodeToDelete.getLeft().setParent(successor);
        successor.setLeft(nodeToDelete.getLeft());
        successor.setHeight(nodeToDelete.getHeight());
        successor.setSize(nodeToDelete.getSize());
        successor.setParent(parent);

        if (parent == null) { //nodeToDelete is root
            this.root = successor;
        } else {
            if (parent.getRight() == nodeToDelete) {
                parent.setRight(successor);
            } else {
                parent.setLeft(successor);
            }
        }
        if (nodeToDelete == ParentSuccessor) { //successor is right child
            return successor;
        }
        return ParentSuccessor;
    }

    private int rebalanceDelete(IAVLNode parent) {
        int counter=0;
        String caseNum = deletetionCase(parent);
        if (!caseNum.equals("")) { // not "case0" = no need to rebalance
            switch (caseNum) {
                case ("case1"):
                    demote(parent);
                    counter++;
                    counter += rebalanceDelete(parent.getParent());
                    break;
                case ("case2right"):
                    IAVLNode RChild = parent.getRight();
                    singleLeftRotation(parent.getRight());
                    promote(RChild); //y
                    demote(parent); //z
                    sizeCalc(parent);
                    updateSize(RChild);
                    counter+=3;
                    break;
                case ("case2left"):
                    IAVLNode LChild = parent.getLeft();
                    singleRightRotation(parent.getLeft());
                    promote(LChild); //y
                    demote(parent); //z
                    sizeCalc(parent);
                    updateSize(LChild);
                    counter+=3;
                    break;
                case ("case3right"):
                    IAVLNode rchild = parent.getRight();
                    singleLeftRotation(parent.getRight());
                    demote(parent); //z
                    demote(parent); //z
                    sizeCalc(parent); //z
                    updateSize(rchild); //y
                    counter+=3;
                    counter += rebalanceDelete(parent.getParent().getParent()); // parent of z is parent of y
                    break;
                case ("case3left"):
                    IAVLNode LeftSon = parent.getLeft();
                    singleRightRotation(parent.getLeft());
                    demote(parent); //z
                    demote(parent); //z
                    sizeCalc(parent); //z
                    updateSize(LeftSon); //y
                    counter+=3;
                    counter += rebalanceDelete(parent.getParent().getParent()); // parent of z is parent of y
                    break;
                case ("case4right"):
                    IAVLNode RightSon = parent.getRight(); //y
                    IAVLNode RightLeftSon = RightSon.getLeft(); //a
                    singleRightRotation(parent.getRight().getLeft()); //y-a
                    this.bfs_print();
                    singleLeftRotation(RightLeftSon); //a-z
                    this.bfs_print();
                    demote(parent); //z
                    demote(parent); //z
                    demote(RightSon); //y
                    promote(RightLeftSon); //a
                    sizeCalc(parent); //z
                    sizeCalc(RightSon); //y
                    updateSize(RightLeftSon); //a
                    counter+=6;
                    counter += rebalanceDelete(parent.getParent().getParent());
                    break;
                case ("case4left"):
                    IAVLNode Lson = parent.getLeft(); //y
                    IAVLNode LRSon = Lson.getRight(); //a
                    singleLeftRotation(parent.getLeft().getRight()); //y-a
                    singleRightRotation(LRSon); //a-z
                    demote(parent); //z
                    demote(parent); //z
                    demote(Lson); //y
                    promote(LRSon); //a
                    sizeCalc(parent); //z
                    sizeCalc(Lson); //y
                    updateSize(LRSon); //a
                    counter+=6;
                    counter += rebalanceDelete(parent.getParent().getParent());
                    break;
            }
        }
        return counter;
    }

    private void updateSize(IAVLNode parent) {
        sizeCalc(parent.getRight());
        sizeCalc(parent.getLeft());
        sizeCalc(parent);
    }

    private void sizeCalc(IAVLNode node) {
        int size = node.getLeft().getSize() + node.getRight().getSize();
        node.setSize(size+1);
    }


    /**
     * public String minNode()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min()
    {
        if (this.empty()){
            return null;
        }
        else {
            return this.minNode.getValue();
        }
    }
    /**
     * public IAVLNode findMin()
     *
     * @return the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public IAVLNode findMin()
    {
        if (this.empty()){
            return null;
        }
        IAVLNode currNode = this.root;
        while (currNode.getLeft()!=null){
            currNode=currNode.getLeft();
        }
        return currNode;
    }


    /**
     * public String maxNode()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max()
    {
        if (this.empty()){
            return null;
        }
        else {
            return this.maxNode.getValue();
        }
    }
    /**
     * public String findMax()
     * <p>
     * Returns the item with the largest key in the tree,
     * or null if the tree is empty.
     * @return the node with minimum key
     */
    public IAVLNode findMax() {
        //todo-check viability
//        using a pointer to maxNode node for O(1)
        if (this.empty()){
            return null;
        }
        IAVLNode currNode = this.root;
        while (currNode.getRight()!=null){
            currNode=currNode.getRight();
        }
        return currNode;
    }

    private int KeysToArrayRecHelper(IAVLNode node,int[] keysArray, int pos) {
        if(!node.isRealNode()) {
            return pos;
        }else {
            pos = KeysToArrayRecHelper(node.getLeft(),keysArray,pos);
            keysArray[pos++]=node.getKey();
            pos = KeysToArrayRecHelper(node.getRight(),keysArray,pos);
        }
        return pos;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        if (this.empty()){
            return new int[]{};// return empty array
        }
        // this tree isnt empty
        int[] keysArray=new int[this.size()];
        KeysToArrayRecHelper(this.root,keysArray,0);
        return keysArray;
    }
    private int infoToArrayRecHelper(IAVLNode node,String[] infoArray, int pos) {
        if(!node.isRealNode()) {
            return pos;
        }else {
            pos = infoToArrayRecHelper(node.getLeft(),infoArray,pos);
            infoArray[pos++]=node.getValue();
            pos = infoToArrayRecHelper(node.getRight(),infoArray,pos);
        }
        return pos;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        if(this.empty()){
            return new String[]{};
        }
        String[] infoArray = new String[this.size()];
        infoToArrayRecHelper(this.root, infoArray,0);
        return infoArray;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        if (!empty()){
            return root.getSize();
        }
        return -1;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * <p>
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        AVLTree biggerSubT = new AVLTree(); // holds nodes with keys bigger than x
        AVLTree smallerSubT = new AVLTree(); // hold nodes keys smaller than x
        IAVLNode whereTosplit = treePosition(this.root, x);
        IAVLNode parent = whereTosplit.getParent();
        AVLTree treeToJoin;
        IAVLNode node = whereTosplit; // pointer for the joins

        if (whereTosplit.getKey() == minNode.getKey()){
            biggerSubT.minNode = findSuccessor(minNode);
        }
        if (whereTosplit.getKey() == maxNode.getKey()){
            smallerSubT.maxNode = smallerSubT.findMax();
        }
        if (node.getRight().isRealNode()){
            biggerSubT = new AVLTree(node.getRight());
        }
        if (node.getLeft().isRealNode()) {
            smallerSubT = new AVLTree(node.getLeft());
        }

        while(parent != null) {
            if (node == parent.getRight()) { // node is where to split
                // parent and left tree is smaller than x
                node = parent;
                parent = parent.getParent();
                if (node.getLeft().isRealNode()) {
                    treeToJoin = new AVLTree(node.getLeft());
                } else {
                    treeToJoin = new AVLTree();
                }
                node.setParent(null);
                node.setLeft(this.EXTERNALNODE);
                node.setRight(this.EXTERNALNODE);
                node.setHeight(0);
                node.setSize(1);
                smallerSubT.join(node, treeToJoin);
            } else {
                node = parent;
                parent = parent.getParent();
                if (node.getRight().isRealNode()) {
                    treeToJoin = new AVLTree(node.getRight());
                } else {
                    treeToJoin = new AVLTree();
                }
                node.setParent(null);
                node.setLeft(this.EXTERNALNODE);
                node.setRight(this.EXTERNALNODE);
                node.setHeight(0);
                node.setSize(1);
                biggerSubT.join(node, treeToJoin);
            }
        }
        return new AVLTree[] {smallerSubT,biggerSubT}; // Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * <p>
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        // take care of empty trees (both,one)
        if (t.empty()) {
            if (this.empty()) {
                // both this and t are empty- we will connect to this as needed
                this.root = new AVLNode(x.getKey(), x.getValue(), EXTERNALNODE, EXTERNALNODE, 0,1);
                this.minNode=this.root;
                this.maxNode=this.root;
                return 1;// rank diff: 0-0+1
            }
            //only t is empty - this isnt empty and t is empty
            //we should insert x as regular insert to this
            this.insert(x.getKey(), x.getValue());// t is empty we insert to this as regular
            //during insert size,min,max is updated
            return this.getRoot().getHeight() + 2; //1-(-1)=2

        } else if (this.empty()) {
            //only this is empty -this is empty and t isnt
            // we should insert x as regular insert to t and then set this as the new tree
            t.insert(x.getKey(), x.getValue());
            this.root = t.getRoot();
            //during insert size is updated and min &max of t
            this.minNode=t.minNode;
            this.maxNode=t.maxNode;
            return this.getRoot().getHeight() + 2; //1-(-1)=2
        }
        //both of them are not empty
        int rankThisRoot = this.getRoot().getHeight();
        int rankTRoot = t.getRoot().getHeight();
        int checkRanksDiff = rankThisRoot - rankTRoot; // if 0 same rank, if >0 this is higher, if <0 this is less high
        boolean isThisbiggerKeys = t.root.getKey() < this.root.getKey();
        IAVLNode currNode;//b
        if (checkRanksDiff > 0) {
            // if >0 this is higher
            if (isThisbiggerKeys) {
                currNode = findInsertPos(this.root, 'L', rankThisRoot, rankTRoot); //b
                connect(currNode, x, t.root, 'L');
                sizeCalc(x);//x
                sizeCalc(currNode.getParent());//c
            } else { //  keys(this)< x < keys(t)
                currNode = findInsertPos(this.root, 'R', rankThisRoot, rankTRoot);
                connect(currNode, x, t.root, 'R');
                sizeCalc(x);//x
                sizeCalc(currNode.getParent());//c
            }
        }
        if (checkRanksDiff == 0) { //same ranks for both trees-
            //same ranks for both trees- x should be the root and connection to its sons will be based on
            // the order of the keys.
            IAVLNode min;
            IAVLNode max;

            if (isThisbiggerKeys) {
                // keys(this) > x > keys(t)
                min=t.minNode;
                max=this.maxNode;
                connect(x, t.root, this.root);// this should be the right son since his keys are bigger
            } else {//  keys(this)< x < keys(t)
                min=this.minNode;
                max=t.maxNode;
                connect(x, this.root, t.root);// t should be the right son since his keys are bigger
            }
            sizeCalc(x);//x
            this.root = x;// we join to this then we should update its root
            this.minNode=min;
            this.maxNode=max;
        }
        else {  // if <0 this is lower
            if (isThisbiggerKeys){
                // keys(this) > x > keys(t)
                currNode=findInsertPos(t.root,'R',rankTRoot,rankThisRoot);
                connect(currNode,x,this.root,'R');
                sizeCalc(x);//x
                sizeCalc(currNode.getParent());//c
            }
            if (!isThisbiggerKeys){
                // keys(this) < x < keys(t)
                currNode= findInsertPos(t.root,'L',rankTRoot,rankThisRoot);
                connect(currNode,x,this.root,'L');
                sizeCalc(x);//x
                sizeCalc(currNode.getParent());//c
            }
        }
        //determine which tree should be sent to rebalance
        if (checkRanksDiff>0){
            //this tree is higher
            this.rebalanceInsert(x);
        }
        else { //t is higher
            t.rebalanceInsert(x);
            this.root=t.root; // we want to join x and t to this tree;
        }
        // if the tree-this is higher than rankDiff are rankThisRoot-rankTRoot+1
        // otherwise, rankDiff are rankTRoot-rankThisRoot+1
        return checkRanksDiff>0 ? rankThisRoot-rankTRoot+1: rankTRoot-rankThisRoot+1;
    }

    private void connect(IAVLNode currNodeInHigher, IAVLNode x, IAVLNode rootLower,char side) {
        IAVLNode parentC=currNodeInHigher.getParent();
        switch (side) {
            case 'R':
                // if we moved right through the higher tree
                currNodeInHigher.setParent(x);
                x.setLeft(currNodeInHigher);
                x.setRight(rootLower);
                parentC.setRight(x);
                break;
            case 'L':
                // if we moved left through the higher tree
                currNodeInHigher.setParent(x);
                x.setLeft(rootLower);
                x.setRight(currNodeInHigher);
                parentC.setLeft(x);
                break;
            default:
                break;
        }
    }

    private void connect(IAVLNode root, IAVLNode leftSon, IAVLNode rightSon) {
        // connection between same rank trees: leftSon,rightSon, X should be thier parent
        root.setLeft(leftSon);
        root.setRight(rightSon);
        leftSon.setParent(root);
        rightSon.setParent(root);
        updateHeight(root);
    }

    private IAVLNode findInsertPos(IAVLNode root, char side, int rankHigher,int rankLowerRoot) {
        IAVLNode currNode = root;
        switch (side) {
            case 'R':
                while (currNode.getHeight() > rankLowerRoot) {
                    // we will go left until we find currNode.getHeight<=rankt
                    currNode = currNode.getRight();
                }
                return currNode;

            case 'L':
                while (currNode.getHeight() > rankLowerRoot) {
                    // we will go left until we find currNode.getHeight<=rankt
                    currNode = currNode.getLeft();
                }
                return currNode;

            default:
                break;
        }
        return currNode;
    }

    private void singleRightRotation(IAVLNode currNode) {
        IAVLNode RChild = currNode.getRight();
        IAVLNode currNodeParent = currNode.getParent();

        replacePointersOfParent(currNode);
        // currNode becomes root instead of currNodeParent
//        begins rotations
        currNode.setParent(currNodeParent.getParent()); // W is parent of X
        currNode.setRight(currNodeParent);
        currNodeParent.setParent(currNode); // X is parent of Z
        currNodeParent.setLeft(RChild); // b is left child of Z
        RChild.setParent(currNodeParent); // z is father of b
    }

    private void singleLeftRotation(IAVLNode currNode) {
        IAVLNode Lchild=currNode.getLeft();
        IAVLNode currNodeParent = currNode.getParent();
        replacePointersOfParent(currNode);
//        // currNode becomes root instead of currNodeParent
//        // the actual rotation here:
        currNode.setParent(currNodeParent.getParent()); // W is the parent of X
        currNode.setLeft(currNodeParent);
        currNodeParent.setParent(currNode); // X is parent of Z
        currNodeParent.setRight(Lchild); // a is a right chilf of Z
        Lchild.setParent(currNodeParent); // z is parent of b
    }

    private void replacePointersOfParent(IAVLNode currNode) {
        IAVLNode currNodeParent = currNode.getParent();
        if (currNodeParent.getParent()==null){ // currNodeParent is the root
            this.root=currNode; //currNode becomes root instead of currNodeParent
        }else{ // currNodeParent is not the root
            if (currNodeParent.getParent().getLeft() == currNodeParent){ //currNodeParent is a left child
                currNodeParent.getParent().setLeft(currNode); // X is now left child of Z's father instead of Z
            }else {
                currNodeParent.getParent().setRight(currNode); // X is now right child of Z's father instead of Z
            }
        }
    }
    //todo-added print funcionality is here -delete later

    public void bfs_print(){
        IAVLNode v = this.getRoot();
        int height = v.getHeight();
        IAVLNode[][] table = new IAVLNode[height+1][(int) Math.pow(2,height)];

        Queue<IAVLNode> q = new ArrayDeque<>();


        q.add(v);

        for (int h=0; h <= height; h++){
            int levelsize = q.size();
            for (int i=0; i<levelsize; i++){
                v = q.remove();
                table[h][i] = v;


                if (v.isRealNode() && v.getLeft().isRealNode())
                    q.add(v.getLeft());
                else{
                    q.add(EXTERNALNODE);
                }
                if (v.isRealNode() && v.getRight().isRealNode())
                    q.add(v.getRight());
                else{
                    q.add(EXTERNALNODE);
                }

            }
        }
        IAVLNode[][] alignedtable = this.aligningPrintTable(table);
        String[][] treetable = this.makeTreeAlike(alignedtable);
        printtreetable(treetable);
    }


    private IAVLNode[][] aligningPrintTable (IAVLNode[][] table){
        int height = this.root.getHeight();
        IAVLNode[][] alignedtable = new IAVLNode[height+1][2*((int) Math.pow(2,height))-1];
        for (int i=0; i<alignedtable.length; i++)
            for (int j=0; j<alignedtable[0].length; j++)
                alignedtable[i][j] = null;


        for (int r=height; r>=0; r--){
            if (r == height){
                for (int i=0; i<table[0].length; i++)
                    alignedtable[r][i*2] = table[r][i];
            } else {

                int firstloc = 0;
                int secondloc = 0;
                boolean firstNodeSeen = false;
                int currnode = 0;

                for (int j=0; j<alignedtable[0].length; j++){
                    if (alignedtable[r+1][j] != null){
                        if (firstNodeSeen){
                            secondloc = j;
                            alignedtable[r][(firstloc+secondloc)/2] = table[r][currnode++];
                            firstNodeSeen = false;
                        } else {
                            firstloc = j;
                            firstNodeSeen = true;
                        }
                    }
                }
            }
        }

        return alignedtable;
    }

    private String[][] makeTreeAlike (IAVLNode[][] alignedtable){
        int height = this.root.getHeight();
        String[][] treetable = new String[(height+1)*3-2][2*((int) Math.pow(2,height))-1];

        for (int r=0; r<treetable.length; r++){
            if (r%3 == 0){
                for (int j=0; j<treetable[0].length; j++) {
                    IAVLNode v = alignedtable[r/3][j];
                    if (v != null && v.isRealNode()) {
                        String k = "" + v.getKey();
                        if (k.length() == 1)
                            k = k + " ";
                        treetable[r][j] = k;
                    } else{
                        if (v != null)
                            treetable[r][j] = "x ";
                        else
                            treetable[r][j] = "  ";
                    }
                }
            }

            else {
                if (r%3 == 1) {
                    for (int j=0; j<treetable[0].length; j++){
                        if (!treetable[r-1][j].equals("  "))
                            treetable[r][j] = "| ";
                        else
                            treetable[r][j] = "  ";
                    }
                } else { //r%3 == 2
                    continue;
                }
            }
        }
        for (int r=0; r<treetable.length; r++){
            if (r%3 == 2){
                boolean write = false;
                for (int j=0; j<treetable[0].length; j++){
                    if (!treetable[r+1][j].equals("  ")){
                        if (write)
                            treetable[r][j] = "__";
                        write = !write;
                    }
                    if (write)
                        treetable[r][j] = "__";
                    else
                        treetable[r][j] = "  ";
                }
            }
        }



        return treetable;
    }

    private void printtreetable (String[][] treetable){
        for (int i=0; i< treetable.length; i++){
            for (int j=0; j< treetable[0].length; j++){
                System.out.print(treetable[i][j]);
                if (j == treetable[0].length-1)
                    System.out.print("\n");
            }
        }
    }
//todo-print funcionality is ended here-delete later.


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
        public boolean isLeaf();
        public int getSize();
        public void setSize(int size);
    }
    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     *
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public class AVLNode implements IAVLNode{
        private int key;
        private String info;
        private IAVLNode parent;
        private IAVLNode left;
        private IAVLNode right;
        private int height;
        private int size;

        public AVLNode(int key, String value, IAVLNode right, IAVLNode left, int height, int size){
            this.key=key;
            this.info=value;
            this.left=left;
            this.right=right;
            this.height=height;
            this.size = size;
        }

        public int getKey() {
            return this.key;
        }
        public String getValue() {
            return this.info;
        }
        public void setLeft(IAVLNode node)
        {
            this.left=node;
        }
        public IAVLNode getLeft() {
            return this.left;
        }
        public void setRight(IAVLNode node)
        {
            this.right=node;
        }
        public IAVLNode getRight()
        {
            return this.right;
        }
        public void setParent(IAVLNode node)
        {
            this.parent=node;
        }
        public IAVLNode getParent()
        {
            return this.parent;
        }
        public boolean isRealNode()
        {
            return (this.height != -1 && this.key != -1 && this.info != null);
        }
        public void setHeight(int height)
        {
            this.height=height;
        }
        public int getHeight()
        {
            return this.height;
        }
        public int getSize() {
            return size;
        }
        public void setSize(int size) {
            this.size = size;
        }
        public boolean isLeaf() {
            return this.getRight().getHeight() == -1 && this.getLeft().getHeight() == -1;
        }
    }
}