package lab4;


// Author: Jaden Miguel, CSCI241
/** a binary search tree class for Lab 3, part 2: Recursion on Trees*/

public class BST {
    public Node root;
    public String traversal;

    /** constructor: create an empty BST */
    public BST() {
        root = null;
    }

    /* return true if Node n is a leaf node. a null node is not considered
     * a leaf. */
    public boolean isLeaf(Node n) {
        if (n == null) {
            return false;
        }
        if (n.left == null && n.right == null) {
            return true;
        }
        return false;
    }

    /** return the number of nodes in the tree */
    public int size() {
        return size(root);
    }

    /* number of nodes in the tree rooted at n
     * = 0 if n is null
     * = 1 + number of nodes in left + number of nodes in right */
    private int size(Node n) {
        //base case, null 
        if (n == null) {
            return 0;
        }
        else {
            return (size(n.left) + 1 + size(n.right));
        }
    }




    /** appends the values in the tree to String traversal using an in-order traversal */
    // inorder: left, root, right
    public void inOrder() {
        traversal = "";
        inOrder(root);
    }
    private void inOrder(Node n) {
        //base case
        if (n == null) {
            return;
        }
        inOrder(n.left);
        inOrder(n.parent);
        traversal += n.value;
        inOrder(n.right);
        
    }


    /** appends the values in the tree to String traversal using a pre-order traversal */
    // preorder: root, left, right
    public void preOrder() {
        traversal = "";
        preOrder(root);
    }
    private void preOrder(Node n) {
        if (n == null) {
            return;
        }
        traversal += n.value;
        preOrder(n.parent);
        preOrder(n.left);
        preOrder(n.right);
        
    }

    /** appends the values in the tree to String traversal using a post-order traversal */
    // postorder: left, right, root
    public void postOrder() {
        traversal = "";
        postOrder(root);
    }
    private void postOrder(Node n) {
        if (n == null) {
            return;
        }
        postOrder(n.left);
        postOrder(n.right);
        postOrder(n.parent);
        traversal += n.value;
        
    }

    /** return the height of the tree.
     *  recall that the height is the path length from the deepest leaf to the
     *  root. special case: an empty tree (root == null) is defined to have
     *  height = -1 */
    public int height() {
        return height(root);
    }

    /* return the height of the tree rooted at n */
    private int height(Node n) {
        if (n == null) {
            return -1;
        }
        return 1 + Math.max(height(n.left), height(n.right));
    }





    /** inner class representing a node in the tree. */
    public class Node {
        public int value;
        public Node parent;
        public Node left;
        public Node right;

        /** constructor: gives default values to all fields */
        public Node() { }

        /** constructor: sets only value */
        public Node(int v) {
            value = v;
        }

        /** constructor: sets value and parent fields */
        public Node(int v, Node p) {
            value = v;
            parent = p;
        }

        /** constructor: sets all fields */
        public Node(int v, Node p, Node l, Node r) {
            value = v;
            parent = p;
            left = l;
            right = r;
        }
    }


    /* methods for testing */

    /** constructor: constructs various BST instances for testing */
    public BST(int testCase) {
        switch(testCase) {
            case 0:
                root = null;
                break;
            case 1:
                root = new Node(4);
                break;
            case 2:
                root = new Node(4);
                root.left = new Node(2);
                root.right = new Node(6);
                break;
            case 3:
                root = new Node(4);
                root.left = new Node(2);
                root.right = new Node(6);
                root.right.right = new Node(7);
                root.right.right.right = new Node(8);
                root.right.right.right.right = new Node(9);
                break;
        }
    }

    /** print a sideways representation of the tree - root at left,
     * right is up, left is down. */
    public void printTree() {
        printSubtree(root, 0);
    }
    private void printSubtree(Node n, int level) {
        if (n == null) {
            return;
        }
        printSubtree(n.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("        ");
        }
        System.out.println(n.value);
        printSubtree(n.left, level + 1);
    }
}
