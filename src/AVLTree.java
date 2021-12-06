import java.util.ArrayDeque;//todo-delete this
import java.util.Arrays;
import java.util.Queue;//todo-delete this
import java.util.Arrays;
//feras baransi, 211757133, ferasbaransi
//mohammed qaiss, 208196857, mohammedq
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

    protected IAVLNode root; // pointer to the root of AVLTree

    private static int ind = 0; // counter

    private int size = 0; // sizeof the tree

    private final IAVLNode ExternalNode = new AVLNode(-1, null, -1, null, null, null, 0); // externalNode

    private IAVLNode minNode = null; // pointer to the node with the min key

    protected IAVLNode maxNode = null; // pointer to the node with the max key

    /**
     * initializes an empty AVLTree - O(1)
     */
    public AVLTree() {
        this.root = null;
    }

    /**
     * initializes an AVLTree with root that his key and value is key and info - O(1)
     */
    public AVLTree(int key, String info) {
        this.root = new AVLNode(key, info, ExternalNode, null, ExternalNode);
    }

    /**
     * initializes an AVLTree with root that eqauls to node - O(1)
     */
    public AVLTree(IAVLNode node) {
        this.root = node;
        node.setParent(null);
        this.size = this.root.getSize();
        this.maxNode = findMax(node);
        this.minNode = findMin(node);
    }

    /**
     * public boolean empty()
     *
     * returns true if and only if the tree is empty
     *
     */
    public boolean empty() { return this.root == null; } // true if root pointer is null, that means we have no nodes in the tree and therefore its empty else false

    /**
     * public String search(int k)
     *
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        if (this.root != null) { // tree is not empty
            IAVLNode node = Tree_Position(this.root, k); // if we found the node
            if (node != null && node.getKey() == k) {
                return node.getValue();
            }
        }
        return null;
    }

    /**
     * public int insert(int k, String i)
     *
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        IAVLNode inserted_node;
        if (this.root == null) { // if the tree is null
            IAVLNode m = new AVLNode(k, i, ExternalNode, null, ExternalNode,1); // create an AVLNode
            MinMaxSetterforInsert(m); // update the min and max node
            this.root = m; // change the pointer of the root
            this.size++; // promote the size by one
            return 0; // no rebalancing

        } else {
            inserted_node = insert_Help(this.root, k, i); // where to insert
            if (inserted_node == null) { // if this node in the tree before
                return -1;
            }

            this.size++; // promote the size by one
            MinMaxSetterforInsert(inserted_node); // update the min and max node after insert
            fixSizeTree(inserted_node, 1); // ו
            //in the route from the inserted_node to the rootNode
            return ReBalance(inserted_node); // sequences of rebalancing that returns number of rebalancing is needed
        }
    }

    /**
     * public int delete(int k)
     *
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        int x ; // number of rebalancing is needed
        IAVLNode node_to_delete = Tree_Position(this.root, k); // find the node-to-delet
        if (node_to_delete == null) { // if this k is not in the tree
            return -1;

        } else {
            if(this.root == node_to_delete && (!node_to_delete.getRight().isRealNode())
                    && (!node_to_delete.getLeft().isRealNode())){ // size of the tree is one an we delet this one
                this.root = null;
                return 0; // no rebalancing!

            } else {
                if (isLeaf(node_to_delete)) { // if the node_to_delete inLeaf
                    LeafDel(node_to_delete); // delete leaf node
                    if (node_to_delete.getParent() != null) {
                        fixSizeTree(node_to_delete.getParent(), -1); // promote the size by one for all the nodes
                        //in the route from the inserted_node to the rootNode
                    }
                    x = ReBalance_after_Deletion(node_to_delete.getParent()); // sequences of rebalancing after delete that returns number of rebalancing is needed

                } else {
                    if (isUnary(node_to_delete)) { // if the node_to_delete inLeaf isUnary
                        IAVLNode parent_node = node_to_delete.getParent();
                        UnaryDel(node_to_delete); // delete Unary node
                        if (parent_node != null) {
                            fixSizeTree(parent_node, -1);// promote the size by one for all the nodes
                            //in the route from the inserted_node to the rootNode
                        }
                        x = ReBalance_after_Deletion(parent_node); // sequences of rebalancing after delete that returns number of rebalancing is needed

                    } else { //  not a leaf
                        IAVLNode successor_parent = TCSetter(node_to_delete); // return IAVLNode "successor_parent"
                        // swith the current node(the node we want to delete) with his successor

                        if (successor_parent != null) {
                            fixSizeTree(successor_parent, -1);// promote the size by one for all the nodes
                            //in the route from the inserted_node to the rootNode
                        }
                        x = ReBalance_after_Deletion(successor_parent); // sequences of rebalancing after delete that returns number of rebalancing is needed

                    }

                }
            }
        }
        MinMaxSetterforDelete(node_to_delete); // update the min and max node after delete
        this.size--; // demote the size by one
        return x; // number of rebalancing is needed to get Ok AVLTrre
    }

    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if(this.minNode == null){ // if the tree is null
            return null;
        }
        return this.minNode.getValue(); // return the Value of the minNode
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if(this.maxNode == null){ // if the tree is null
            return null;
        }
        return this.maxNode.getValue(); // return the Value of the minNode
    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray()
    {
        if(this.empty()){return new int[]{}; } // null AVLTree

        int[] arr = new int[this.size]; // sized of the AVLTree
        ind = 0;
        fillArray(this.root, arr); // fills the array from the smallet key to the bigger one Recursively
        ind = 0;

        return arr;
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        if(this.empty()){return new String[]{};} // if the tree is null

        String[] arr = new String[this.size]; // sized of the AVLTree
        ind  = 0;
        fillArrayString(this.root, arr); // fills the array from the smallet key to the bigger one by his Value Recursively
        ind = 0;

        return arr;
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     *
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return this.size; // returns the amount of nodes in the tree
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     *
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() { return this.root;} // returns the root of the tree

    /*
     * public string split(int x)
     *
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        AVLTree bigger = new AVLTree(); // tree with node that have keys bigger than x
        AVLTree smaller = new AVLTree(); // tree with node that have keys smaller than x

        IAVLNode node_for_split = Tree_Position(this.root, x); // fount the splited node

        /*
         * start to biuld the two trees
         * one tree(smaller) - with keys that smaller than x
         * second tree(bigger) - with keys that bigger than x
         * started from the splited node to th� root of the OriginaLTree
         */
        if (node_for_split.getLeft().getHeight() != -1) {
            smaller = new AVLTree(node_for_split.getLeft());
        }

        if (node_for_split.getRight().getHeight() != -1){
            bigger = new AVLTree(node_for_split.getRight());
        }

        IAVLNode parent = node_for_split.getParent();
        AVLTree TreetoJoin = new AVLTree();

        while(parent != null) { // while we not pass the root
            if (parent.getRight() == node_for_split) { // join with the smaller tree
                node_for_split = parent;
                parent = parent.getParent();

                if (node_for_split.getLeft().getHeight() != -1) {
                    TreetoJoin = new AVLTree(node_for_split.getLeft()); // tree to join with the smaller tree

                } else {
                    TreetoJoin = new AVLTree(); // in case no left node - null tree
                }

                SplitSetter(node_for_split); // node with externalNode -left&&right- height&&size = 0
                smaller.join(node_for_split, TreetoJoin); // join - smaller with TreetoJoin

            } else { // join with the bigger tree
                node_for_split = parent;
                parent = parent.getParent();

                if (node_for_split.getRight().getHeight() != -1) {
                    TreetoJoin = new AVLTree(node_for_split.getRight()); // tree to join with the smaller tree

                } else {
                    TreetoJoin = new AVLTree(); // in case no right node - null tree
                }

                SplitSetter(node_for_split); // node with externalNode -left&&right- height&&size = 0&&1 Respectively
                bigger.join(node_for_split, TreetoJoin); // join - bigger with TreetoJoin


            }

        }
        return new AVLTree[] {smaller,bigger}; // Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    }
    /**
     * public join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        if (t.empty() && this.empty()) { // to trees is empty
            EmptyTreesSetter(x); // create tree with one node
        }
        if (t.empty()) { // t is empty
            int height = this.root.getHeight() + 1;
            insert(x.getKey(), x.getValue());
            return height;
        }
        if (this.empty()) { // this is empty
            this.root = t.getRoot();
            this.minNode = t.minNode;
            this.maxNode = t.maxNode;
            this.size = t.size;
            int height = t.root.getHeight() + 1;
            insert(x.getKey(), x.getValue());
            return height;
        }

        AVLTree bigger = t;  // if the keys in "t" are bigger then "x"
        AVLTree smaller = this; // if the keys in "t" are bigger then "x"
        if (this.root.getKey() > x.getKey()) { //if the keys in "this" are bigger than "x"
            bigger = this;
            smaller = t;
        }
        int diff; // == |bigger.height - smaller.height|
        boolean biggerIsHeigher = true;
        IAVLNode where;

        if (bigger.root.getHeight() > smaller.root.getHeight()) { // bigger is heighter than smaller tree
            diff = bigger.root.getHeight() - smaller.root.getHeight();

            if (diff == 0) { //the trees have the same height
                joinforEqualTress(smaller, x, bigger);
                return diff + 1; // |tree.rank - t.rank| + 1
            }

            where = searchOnLeftNode(bigger, smaller.root.getHeight()); //searches the joining node
            if (where == bigger.root) { //the trees have the same height
                joinforEqualTress(smaller, x, bigger);
                return diff + 1; // |tree.rank - t.rank| + 1
            }

            joinSetters(x, where, smaller.root); // to link where and smaller.root with x like a father

        } else { // smaller is heighter than bigger tree
            diff = smaller.root.getHeight() - bigger.root.getHeight();

            if (diff == 0) { //the trees have the same height
                joinforEqualTress(smaller, x, bigger);
                return diff + 1; // |tree.rank - t.rank| + 1
            }

            biggerIsHeigher = false;
            where = searchOnRightNode(smaller, bigger.root.getHeight());  //searches the joining node

            if (where == smaller.root) { //the trees have the same height
                joinforEqualTress(smaller, x, bigger);
                return diff + 1; // |tree.rank - t.rank| + 1
            }

            joinSetters(x, where, bigger.root); // to link where and bigger.root with x like a father
        }

        // updates "this" fields
        int sizeToAdd = 0;
        if (biggerIsHeigher) {
            this.root = bigger.root;
            sizeToAdd = smaller.size + 1;
        } else {
            this.root = smaller.root;
            sizeToAdd = bigger.size + 1;
        }

        fixSizeTree(x.getParent(), sizeToAdd);
        this.minNode = smaller.minNode;
        this.maxNode = bigger.maxNode;

        ReBalance(x); // sequences of rebalancing after join that returns number of rebalancing is needed and return to Ok AVLTree

        return diff + 1; // |tree.rank - t.rank| + 1
    }

    // O(1)
    private void joinSetters(IAVLNode x, IAVLNode where_to_insert, IAVLNode smallerTree_root) {
        // added x as a parent to where_to_insert and smallerTree_root
        IAVLNode parent = where_to_insert.getParent();
        if (parent.getRight() == where_to_insert) {
            x.setLeft(where_to_insert);
            x.setRight(smallerTree_root);
            parent.setRight(x);
        } else {
            x.setRight(where_to_insert);
            x.setLeft(smallerTree_root);
            parent.setLeft(x);
        }
        where_to_insert.setParent(x);
        smallerTree_root.setParent(x);
        x.setParent(parent);
        IAVLNode.fixHeight(x);
        IAVLNode.fixSizeNode(x);
        IAVLNode.fixSizeNode(parent);
    }

    // O(1)
    private void EmptyTreesSetter(IAVLNode x){
        // create tree with one node
        // epdate all the attribute if x and this

        this.root = x;
        this.minNode = x;
        this.maxNode = x;
        this.size ++;
        x.setParent(null);
        x.setLeft(this.ExternalNode);
        x.setRight(this.ExternalNode);
        x.setSize(1);
        x.setHeight(0);
    }

    // O(1)
    private void joinforEqualTress(AVLTree smaller, IAVLNode x, AVLTree bigger) {
        // join the smaller tree as a left-subtree and the bigger tree as a right-subtree for x node
        x.setLeft(smaller.root);
        x.setRight(bigger.root);
        smaller.root.setParent(x);
        bigger.root.setParent(x);
        IAVLNode.fixHeight(x);
        IAVLNode.fixSizeNode(x);
        this.root = x;
        this.size = x.getSize();
        this.minNode = smaller.minNode;
        this.maxNode = bigger.maxNode;
    }

    // O(1)
    private static IAVLNode searchOnRightNode(AVLTree tree, int a) {
        // find the first node in the tree in the right route that have height that ix <= a
        IAVLNode curr = tree.root;
        while (curr.getHeight() > a) {
            curr = curr.getRight();
        }
        return curr;
    }

    // O(1)
    private static IAVLNode searchOnLeftNode(AVLTree tree, int a) {
        // find the first node in the tree in the left route that have height that ix <= a
        IAVLNode curr = tree.root;
        while (curr.getHeight() > a) {
            curr = curr.getLeft();
        }
        return curr;
    }

    // O(1)
    void SplitSetter(IAVLNode node_to_split){
        // update the pointers in the node_to_split parent-left-right node to null
        // update the height and size attribute of node_to_split to 0 and 1 Respectively
        node_to_split.setParent(null);
        node_to_split.setLeft(this.ExternalNode);
        node_to_split.setRight(this.ExternalNode);
        node_to_split.setHeight(0);
        node_to_split.setSize(1);
    }

    /*
     *  return the current case in tree after we insert a new node - cases that we learn in the class
     *  choose the case is depent on the Situation of the tree
     *  if the tree is Ok AVLTree we return "all good"
     *  in other case we return the proper case that appropriate to the Situation of the tree
     *  O(1)
     */
    private String insert_cases(IAVLNode node){
        if (node != null && node.getParent() != null) {
            int[] rank_difference_for_node = rankDifference(node);
            int[] rank_difference_for_parent_node = rankDifference(node.getParent());
            //{1, 1} me and my parent
            if (((Arrays.equals(rank_difference_for_node, new int[]{1, 1}) ||
                    (Arrays.equals(rank_difference_for_node, new int[]{2, 1})) ||
                    (Arrays.equals(rank_difference_for_node, new int[]{1, 2})))
                    && (Arrays.equals(rank_difference_for_parent_node, new int[]{1, 0}) ||
                    (Arrays.equals(rank_difference_for_parent_node, new int[]{0, 1}))))) {
                return "case1";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{2, 1}) // single-rotation case left
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{2, 0})) {
                return "case2left";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{1, 2}) // single rotation case right
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{0, 2})) {
                return "case2right";
            }

            if (Arrays.equals(rank_difference_for_node, new int[]{2, 1}) // double rotation case right
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{0, 2})) {
                return "case3left";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{1, 2}) // double rotation case right
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{2, 0})) {
                return "case3right";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{1, 1}) // double rotation case right
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{2, 0})) {
                return "case4left";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{1, 1}) // double rotation case right
                    && Arrays.equals(rank_difference_for_parent_node, new int[]{0, 2})) {
                return "case4right";
            }
        }

        return "all good";
    }

    /*
     * find where to insert the new node with the key and Value that we reiceve as an input
     * return the new node or null if the new node we want to insert is Previously in the tree
     * insert it as a left or right child in accordance to the key of his parent
     * O(logn)
     */
    private IAVLNode insert_Help(IAVLNode root, int key, String info) {//assumption that we don't insert same key
        IAVLNode leaf = Tree_Position(root, key);
        if(leaf.getKey() == key){
            return null;
        }
        IAVLNode to_insert = new AVLNode(key,info, ExternalNode, leaf, ExternalNode);
        if(leaf.getKey() < key){
            leaf.setRight(to_insert);
        }else {
            leaf.setLeft(to_insert);
        }
        return to_insert;
    }

    /*
     * find where to insert the new node with the key and Value that we reiceve as an input
     * return the appropriate node or null if the new node we want to insert is Previously in the tree
     * O(logn)
     */
    IAVLNode Tree_Position(IAVLNode node, int k){
        IAVLNode y = null;
        while (node != ExternalNode && node != null && node.isRealNode()){
            y = node;
            if(node.getKey() == k){
                return node;
            }
            if (k < node.getKey()){
                node = node.getLeft();
            }
            else {
                node = node.getRight();
            }
        }
        return y;
    }


    /*
     * Rotates the tree right
     * O(1)
     */
    public void rightRotate(IAVLNode node) {
        IAVLNode right_node = node.getRight();
        IAVLNode parent_node = node.getParent();

        ParentNodeSetter(node);

        node.setRight(node.getParent());
        node.setParent(parent_node.getParent());

        parent_node.setParent(node);
        parent_node.setLeft(right_node);
        right_node.setParent(parent_node);
    }

    /*
     * Rotates the tree left
     * O(1)
     */
    public void leftRotate(IAVLNode node) {
        IAVLNode left_node = node.getLeft();
        IAVLNode parent_node = node.getParent();

        ParentNodeSetter(node);

        node.setLeft(node.getParent());
        node.setParent(parent_node.getParent());
        parent_node.setParent(node);
        parent_node.setRight(left_node);
        left_node.setParent(parent_node);

    }


    /*
     * Rotates the tree twice
     * O(1)
     */
    public void leftrightRotate(IAVLNode node){
        IAVLNode nodee = node.getRight();
        leftRotate(nodee);
        rightRotate(nodee);

    }


    /*
     * Rotates the tree twice
     * O(1)
     */
    public void rightleftRotate(IAVLNode node){
        IAVLNode nodee = node.getLeft();
        rightRotate(nodee);
        leftRotate(nodee);

    }


    /*
     * computes the rank difference for node and his parent
     * O(1)
     */
    public static int[] rankDifference(IAVLNode node){
        if (node != null) return new int[] {node.getHeight() - node.getLeft().getHeight(),
                node.getHeight() - node.getRight().getHeight()};
        return new int[] {};
    }


    /*
     * finds the successor for a given node
     * O(logn)
     */
    private IAVLNode successor(IAVLNode node){
        if (node.getRight().isRealNode()){
            IAVLNode nodee1 = node.getRight();
            while(nodee1.getLeft().isRealNode()){
                nodee1 = nodee1.getLeft();
            }
            return nodee1;
        }
        else {
            IAVLNode nodee = node.getParent();
            while (nodee != null) {
                if (nodee.getKey() > node.getKey()) {
                    return nodee;
                }
                nodee = node.getParent();
            }
        }

        return null;
    }


    /*
     * sets the pointers for a parent node in rotations
     * O(1)
     */
    private void ParentNodeSetter(IAVLNode node){
        IAVLNode parent_node = node.getParent();
        if(parent_node.getParent() == null){
            this.root = node;
        }
        if(parent_node.getParent() != null){
            if(parent_node.getParent().getRight() == parent_node){
                parent_node.getParent().setRight(node);
            }
            else {
                parent_node.getParent().setLeft(node);
            }
        }
    }


    /*
     * updates the sizes for every node on route from node to root
     * O(logn)
     */

    private void fixSizeTree(IAVLNode curr, int i) {
        while(curr != null) {
            curr.setSize(curr.getSize() + i);
            curr = curr.getParent();
        }
    }

    /*rebalances the tree after insertion
     * returns the number of rabalance operations needed
     * * O(logn)
     */
    public int ReBalance(IAVLNode node) {
        int cnt = 0;
        String insert_case = insert_cases(node);
        if (node != null && node.getParent() != null) {
            if (insert_case.equals("case1")) {
                IAVLNode.fixHeight(node.getParent());
                IAVLNode.fixSizeNode(node);
                cnt++;
                cnt += ReBalance(node.getParent());
            }
            if (insert_case.equals("case2left")) {
                IAVLNode nod = node.getParent();
                leftRotate(node);
                IAVLNode.fixHeight(nod);
                sizeSetters(node);
                cnt+=2;
            }
            if (insert_case.equals("case2right")) {
                IAVLNode nod = node.getParent();
                rightRotate(node);
                IAVLNode.fixHeight(nod);
                sizeSetters(node);
                cnt+=2;
            }

            if (insert_case.equals("case3left")) {
                IAVLNode parent = node.getParent(); // z
                IAVLNode right_child = node.getRight();
                leftrightRotate(node);
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(parent);
                IAVLNode.fixHeight(right_child);
                IAVLNode.fixSizeNode(parent);
                IAVLNode.fixSizeNode(node);
                IAVLNode.fixSizeNode(right_child);
                cnt += 5;
            }
            if (insert_case.equals("case3right")) {
                IAVLNode parent = node.getParent(); // z
                IAVLNode left_child = node.getLeft();
                rightleftRotate(node);
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(parent);
                IAVLNode.fixHeight(left_child);
                IAVLNode.fixSizeNode(parent);
                IAVLNode.fixSizeNode(node);
                IAVLNode.fixSizeNode(left_child);
                cnt += 5;
            }
            if (insert_case.equals("case4left")){
                IAVLNode nod = node.getParent();
                leftRotate(node);
                IAVLNode.fixHeight(nod);
                IAVLNode.fixHeight(node);
                IAVLNode.fixSizeNode(node.getLeft());
                IAVLNode.fixSizeNode(node.getRight());
                IAVLNode.fixSizeNode(nod);
                cnt+=2;
                cnt += ReBalance(node.getParent());
            }
            if (insert_case.equals("case4right")){
                IAVLNode nod = node.getParent();
                rightRotate(node);
                IAVLNode.fixHeight(nod);
                IAVLNode.fixHeight(node);
                IAVLNode.fixSizeNode(node.getLeft());
                IAVLNode.fixSizeNode(node.getRight());
                IAVLNode.fixSizeNode(nod);
                cnt+=2;
                cnt += ReBalance(node.getParent());
            }

        }
        if (node != null) IAVLNode.fixSizeNode(node);
        return cnt;
    }

    /* rebalnces the tree after deletion
     * returns the number of rebalance operations needed
     * O(logn)
     */
    public int ReBalance_after_Deletion(IAVLNode node) {
        int cnt = 0;
        int sum = 0;
        String st = Delete_cases(node);
        if (!st.equals("all good")){
            if(st.equals("case1")){
                IAVLNode.fixHeight(node);
                IAVLNode.fixSizeNode(node);
                sum =  ReBalance_after_Deletion(node.getParent());
                cnt++;
            }

            if(st.equals("case2left")){
                IAVLNode nodee = node.getRight();
                leftRotate(node.getRight());
                cnt+=3;
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(nodee);
                sizeSetters(nodee);
                //sum = ReBalance_after_Deletion(node.getParent().getParent());
            }

            if(st.equals("case2right")){
                IAVLNode nodee = node.getLeft();
                rightRotate(node.getLeft());
                cnt+=3;
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(nodee);
                sizeSetters(nodee);
                //sum = ReBalance_after_Deletion(node.getParent().getParent());

            }

            if(st.equals("case3left")){
                IAVLNode nodee = node.getRight();
                leftRotate(node.getRight());
                cnt+=3;
                IAVLNode.fixHeight(node);
                sizeSetters(nodee);
                sum = ReBalance_after_Deletion(node.getParent().getParent());

            }
            if(st.equals("case3right")){
                IAVLNode nodee = node.getLeft();
                rightRotate(node.getLeft());
                cnt+=3;
                IAVLNode.fixHeight(node);
                sizeSetters(nodee);
                sum = ReBalance_after_Deletion(node.getParent().getParent());
            }

            if(st.equals("case4left")){
                IAVLNode no = node.getRight().getLeft();
                IAVLNode no1 = node.getRight();
                rightleftRotate(node.getRight());
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(no1);
                IAVLNode.fixHeight(no);
                sizeSetters(no);
                cnt+=6;
                sum = ReBalance_after_Deletion(node.getParent().getParent());
            }

            if(st.equals("case4right")){
                IAVLNode no = node.getLeft().getRight();
                IAVLNode no1 = node.getLeft();
                leftrightRotate(node.getLeft());
                IAVLNode.fixHeight(node);
                IAVLNode.fixHeight(no1);
                IAVLNode.fixHeight(no);
                sizeSetters(no);
                cnt+=6;
                sum = ReBalance_after_Deletion(node.getParent().getParent());
            }
        }
        return sum + cnt;
    }

    /*
     * update the size attribute to a node and his children
     * start with the children size attribute and after that to the node size attribute
     * O(1)
     */
    private void sizeSetters(IAVLNode nodee){
        IAVLNode.fixSizeNode(nodee.getRight());
        IAVLNode.fixSizeNode(nodee.getLeft());
        IAVLNode.fixSizeNode(nodee);
    }

    // O(n)
    public void fillArray(IAVLNode node, int [] arr){
        if(node != null && node.isRealNode() && node != ExternalNode && ind < arr.length){ // legal situation
            fillArray(node.getLeft(), arr); // Recursively put in the array the keys in left-subtree

            arr[ind] = node.getKey(); // put in the array in place ind the key of the current node
            ind++; // this paramater is an attribute for the tree -static attribute-

            fillArray(node.getRight(), arr); // Recursively put in the array the keys in right-subtree
        }
    }

    // O(n)
    public void fillArrayString(IAVLNode node, String[] arr){
        if(node != null && node.isRealNode() && node != ExternalNode && ind < arr.length){ // legal situation
            fillArrayString(node.getLeft(), arr); // Recursively put in the array the Values in left-subtree

            arr[ind] = node.getValue(); // put in the array in place ind the Value of the current node
            ind++; // this paramater is an attribute for the tree -static attribute-

            fillArrayString(node.getRight(), arr); // Recursively put in the array the Values in right-subtree
        }
    }


    /*
     * delete an UnaryNode
     * O(1)
     */
    private void UnaryDel(IAVLNode node_to_delete){
        IAVLNode node_to_delete_parent = node_to_delete.getParent();
        if(node_to_delete_parent == null){  // the tree has only two nodes
            if(node_to_delete.getRight().isRealNode()){
                this.root = node_to_delete.getRight();
                node_to_delete.getRight().setParent(null);
            }
            if(node_to_delete.getLeft().isRealNode()){
                this.root = node_to_delete.getLeft();
                node_to_delete.getLeft().setParent(null);
            }
        }
        else {
            if (node_to_delete_parent.getRight() == node_to_delete){
                if(node_to_delete.getLeft().isRealNode()){
                    node_to_delete_parent.setRight(node_to_delete.getLeft());
                    node_to_delete.getLeft().setParent(node_to_delete_parent);
                }else {
                    node_to_delete_parent.setRight(node_to_delete.getRight());
                    node_to_delete.getRight().setParent(node_to_delete_parent);
                }
            }
            else {
                if(node_to_delete.getRight().isRealNode()){
                    node_to_delete_parent.setLeft(node_to_delete.getRight());
                    node_to_delete.getRight().setParent(node_to_delete_parent);
                }else {
                    node_to_delete_parent.setLeft(node_to_delete.getLeft());
                    node_to_delete.getLeft().setParent(node_to_delete_parent);
                }
            }
        }

    }

    /*
     * delete a leaf node
     * O(1)
     */
    public void LeafDel(IAVLNode node_to_delete){
        IAVLNode node_to_delete_parent = node_to_delete.getParent();
        if(node_to_delete_parent == null){ // size of the tree is 1
            this.root = null;

        }else{
            if(node_to_delete_parent.getRight() == node_to_delete){ // right child to his parent
                node_to_delete_parent.setRight(ExternalNode);
            }

            else { // left child to his parent
                node_to_delete_parent.setLeft(ExternalNode);
            }
        }
    }

    // O(1)
    private IAVLNode TCSetter(IAVLNode node_to_delete){ // swith the current node(the node we want to delete) with his successor

        IAVLNode parent = node_to_delete.getParent();
        IAVLNode successor = successor(node_to_delete); //where has two children
        IAVLNode successorParent = successor.getParent();

        if (node_to_delete != successorParent) { //successor is not the right child of the node_to_delete
            node_to_delete.getRight().setParent(successor);
            successorParent.setLeft(successor.getRight());
            successor.getRight().setParent(successorParent);
            successor.setRight(node_to_delete.getRight());
        }

        node_to_delete.getLeft().setParent(successor);
        successor.setLeft(node_to_delete.getLeft());
        successor.setHeight(node_to_delete.getHeight());
        successor.setSize(node_to_delete.getSize());
        successor.setParent(parent);

        if (parent == null) { //deleting the root
            this.root = successor;
        } else {
            if (parent.getRight() == node_to_delete) {
                parent.setRight(successor);
            } else {
                parent.setLeft(successor);
            }
        }
        if (node_to_delete == successorParent) { //successor is where's right child
            return successor;
        }
        return successorParent;
    }

    // O(1)
    private boolean isUnary(IAVLNode node){ // return true if the node is a UnaryNode otherwise false
        return ((!node.getRight().isRealNode()) && node.getLeft().isRealNode()) || (!node.getRight().isRealNode() && node.getLeft().isRealNode());
    }

    // O(1)
    private boolean isLeaf(IAVLNode node){ // return true if the node is a leaf otherwise false
        return (!node.getRight().isRealNode()) && !(node.getLeft().isRealNode());
    }

    /*
     *  return the current case in tree after we delete the node - cases that we learn in the class
     *  choose the case in depent in the Situation of the tree
     *  if the tree is Ok AVLTree we return "all good"
     *  in other case we return the proper case that appropriate to the Situation of the tree
     *  O(1)
     */
    public String Delete_cases(IAVLNode node){
        if (node == null){
            return "all good";
        }
        int[] rank_difference_for_node = rankDifference(node);
        if (Arrays.equals(rank_difference_for_node, new int[]{2, 2})){
            return "case1";
        }
        boolean a = node.getRight().isRealNode();
        boolean b = node.getLeft().isRealNode();
        if (a){
            int[] rank_difference_for_node_right = rankDifference(node.getRight());
            if (Arrays.equals(rank_difference_for_node, new int[]{3, 1}) &&
                    Arrays.equals(rank_difference_for_node_right, new int[]{1, 1})) { //case 2
                return "case2left";
            }
            if (Arrays.equals(rank_difference_for_node, new int[]{3, 1}) &&
                    Arrays.equals(rank_difference_for_node_right, new int[]{2, 1})) { // case 3
                return "case3left";
            }

            if (Arrays.equals(rank_difference_for_node, new int[]{3, 1}) &&
                    Arrays.equals(rank_difference_for_node_right, new int[]{1, 2})) { //case 4
                return "case4left";
            }
        }
        if(b){
            int[] rank_difference_for_node_left = rankDifference(node.getLeft());

            if (Arrays.equals(rank_difference_for_node, new int[]{1, 3}) &&
                    Arrays.equals(rank_difference_for_node_left, new int[]{1, 1})) { // case 2 symmetric
                return "case2right";

            }

            if (Arrays.equals(rank_difference_for_node, new int[]{1, 3}) &&
                    Arrays.equals(rank_difference_for_node_left, new int[]{1, 2})) { //case 3 symmetric
                return "case3right";
            }


            if (Arrays.equals(rank_difference_for_node, new int[]{1, 3}) &&
                    Arrays.equals(rank_difference_for_node_left, new int[]{2, 1})) { // case 4 symmetric
                return "case4right";
            }
        }

        return "all good";
    }

    /*
     *  update the minNode and maxNode in the tree if this is required after delete a node in the tree
     *  O(1)
     */
    private void MinMaxSetterforDelete(IAVLNode node_to_delete){
        if(node_to_delete.getKey() == this.minNode.getKey()){
            this.minNode = successor(node_to_delete);
        }
        if(node_to_delete.getKey() == this.maxNode.getKey()){
            this.maxNode = predecessor(node_to_delete);
        }
    }

    /*
     *  update the minNode and maxNode in the tree if this is required after insert a new node to the tree
     *  O(1)
     */
    private void MinMaxSetterforInsert(IAVLNode node_to_insert){
        if(this.root == null){
            this.maxNode = node_to_insert;
            this.minNode = node_to_insert;
        }
        if(node_to_insert.getKey() < this.minNode.getKey()){
            this.minNode = node_to_insert; }
        if(node_to_insert.getKey() > this.maxNode.getKey()){
            this.maxNode = node_to_insert;
        }

    }

    /*
     *  returns the predecessor for the node in the tree
     *  O(logn)
     */
    private IAVLNode predecessor(IAVLNode node){
        if (node.getLeft().isRealNode()){
            IAVLNode nodee1 = node.getLeft();
            while(nodee1.getRight().isRealNode()){
                nodee1 = nodee1.getRight();
            }
            return nodee1;
        }
        else {
            IAVLNode nodee = node.getParent();
            while (nodee != null) {
                if (nodee.getKey() < node.getKey()) {
                    return nodee;
                }
                nodee = node.getParent();
            }
        }
        return null;
    }

    /*
     *  returns the node with the minimal key in the tree
     *  O(logn)
     */
    public IAVLNode findMin(IAVLNode node) {
        IAVLNode curr = node;
        while(curr.getLeft().getHeight() != -1) {
            curr = curr.getLeft();
        }
        return curr;
    }

    /*
     *  returns the node with the maximal key in the tree
     *  O(logn)
     */
    public IAVLNode findMax(IAVLNode node) {
        IAVLNode curr =  node;
        while(curr.getRight().getHeight() != -1) {
            curr =  curr.getRight();
        }
        return curr;
    }

    // O(1)
    public IAVLNode createNode(int key){ // create an AVLNode
        return new AVLNode(key,"s",null,null,null);
    }


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); //returns node's key (for virtuval node return -1)
        public String getValue(); //returns node's value [info] (for virtuval node return null)
        public void setLeft(IAVLNode node); //sets left child
        public IAVLNode getLeft(); //returns left child (if there is no left child return null)
        public void setRight(IAVLNode node); //sets right child
        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

        public int getSize();

        public void setSize(int size);


        public static void fixHeight(IAVLNode curr) {
            int max = Math.max(curr.getLeft().getHeight(), curr.getRight().getHeight());
            curr.setHeight(max+1);
        }

        public static void fixSizeNode(IAVLNode curr) {
            int size = curr.getLeft().getSize() + curr.getRight().getSize();
            curr.setSize(size+1);
        }
    }

    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     */
    public class AVLNode implements IAVLNode{
        private IAVLNode left;
        private IAVLNode parent;
        private IAVLNode right;
        private int height; // also we can use height
        private int key;
        private String value;
        private int size;

        public AVLNode(int key, String value, int height, IAVLNode left, IAVLNode parent, IAVLNode right) {
            this.left = left;
            this.parent = parent;
            this.right = right;
            this.height = height;
            this.key = key;
            this.value = value;
        }

        public AVLNode(int key, String value, int height, IAVLNode left, IAVLNode parent, IAVLNode right, int size) {
            this.left = left;
            this.parent = parent;
            this.right = right;
            this.height = height;
            this.key = key;
            this.value = value;
            this.size = size;
        }

        public AVLNode(int key, String value, IAVLNode left, IAVLNode parent, IAVLNode right) {
            this.left = left;
            this.parent = parent;
            this.right = right;
            this.key = key;
            this.value = value;
        }

        public AVLNode(int key, String value, IAVLNode left, IAVLNode parent, IAVLNode right, int size) {
            this.left = left;
            this.parent = parent;
            this.right = right;
            this.key = key;
            this.value = value;
            this.size = size;
        }



        public int getKey()
        {
            return this.key; // to be replaced by student code
        }

        public String getValue()
        {
            return this.value; // to be replaced by student code
        }

        public void setLeft(IAVLNode node)
        {
            this.left = node;
        }

        public IAVLNode getLeft()
        {
            return this.left; // to be replaced by student code
        }

        public void setRight(IAVLNode node)
        {
            this.right = node;
        }
        public IAVLNode getRight()
        {
            return this.right;
        }

        public void setParent(IAVLNode node)
        {
            this.parent = node;
        }

        public IAVLNode getParent()
        {
            return this.parent; // to be replaced by student code
        }
        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            if(this.key == -1 && this.height == -1 && this.value == null){
                return false;
            } // to be replaced by student code
            return true;
        }

        public void setHeight(int height)
        {
            this.height = height;
        }

        public int getHeight() { return this.height; }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            if (this.left != null && this.right != null &&  this.parent != null) {
                return "Node{" +
                        "left=" + left.getKey() +
                        ", parent=" + parent.getKey() +
                        ", key=" + key +
                        ", right=" + right.getKey() +
                        ", rank=" + height+
                        ", size=" + size+
                        ", info='" + value + '\'' +
                        '}';
            }
            else
            {
                if (this.left != null && this.right != null) {
                    return "Node{" +
                            "left=" + left.getKey() +
                            ", key=" + key +
                            ", right=" + right.getKey() +
                            ", rank=" + height +
                            ", size=" + size +
                            ", info='" + getValue() + '\'' +
                            '}';
                }
                return "Node{" +
                        ", rank=" + getHeight() +
                        ", key=" + key +
                        ", info='" + getValue()+ '\'' +
                        ", size=" + size +
                        '}';
            }
        }
    }

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
                    q.add(ExternalNode);
                }
                if (v.isRealNode() && v.getRight().isRealNode())
                    q.add(v.getRight());
                else{
                    q.add(ExternalNode);
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

}