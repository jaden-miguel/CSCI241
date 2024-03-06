// Author: Jaden Miguel
// A2 CSCI241 - Spring 2020
// AVL class
// comparing strings in alphabetical order

package avl;

public class AVL {

  public Node root;

  private int size;

  public int getSize() {
    return size;
  }

  /**
   * find w in the tree. return the node containing w or null if not found
   */
  public Node search(String w) {
    return search(root, w);
  }

  private Node search(Node n, String w) {
    if (n == null) {
      return null;
    }
    if (w.equals(n.word)) {
      return n;
    } else if (w.compareTo(n.word) < 0) {
      return search(n.left, w);
    } else {
      return search(n.right, w);
    }
  }

  /** insert w into the tree as a standard BST, ignoring balance */
  public void bstInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    bstInsert(root, w);
  }

  /*
   * insert w into the tree rooted at n, ignoring balance. pre: n is not null
   * deals with duplicates
   */
  private void bstInsert(Node n, String w) {
    // base case, if duplicate: nothing is performed
    if (n.word.equals(w)) {
      return;
    }

    // alphabeticlly insert in left subtree, update size pointer
    if (w.compareTo(n.word) < 0) {
      if (n.left == null) {
        size++;
        n.left = new Node(w, n);
      } else {
        bstInsert(n.left, w);
      }

      // deal with right subtree
    } else {
      if (n.right == null) {
        size++;
        n.right = new Node(w, n);
      } else {
        bstInsert(n.right, w);
      }

    }

  }

  
  //insert w into the tree, maintaining AVL balance 
  //precondition: the tree is AVL balanced
  public void avlInsert(String w) {
    //same as bst method for recursion
    //we are calling the actual method
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    avlInsert(root, w);
  }

  /*
   * insert w into the tree, maintaining AVL balance 
   * precondition: the tree is AVL balanced and n is not null
   */
  private void avlInsert(Node n, String w) {
    //base case, do nothing
    if (n.word.equals(w)) {return;}

    //if w is < n.word, insert in left subtree
    if (w.compareTo(n.word) < 0) {
      if (n.left == null) {
        size++;
        n.left = new Node(w, n);
        if (n.right == null) {n.height++;}
      }
      //we know to insert in left child
      else {
        avlInsert(n.left, w);
        newHeight(n);
      }
    }
    else { // if w is > than n.word, insert in right subtree
      if (n.right == null) {
        size++;
        n.right = new Node(w, n);
        if(n.left == null) {n.height++;}
      }
      //we know to insert into right child 
      else {
        avlInsert(n.right, w);
        newHeight(n);
      }
    }

    rebalance(n);
  }

    


  /** inverse of rightRotate
   * do a left rotation: rotate on the edge from x to its right child.
   * precondition: x has a non-null right child
   */
  public void leftRotate(Node x) {
    Node y = x.right;

    // y's left child -> x's right child
    x.right = y.left;
    if (y.left != null) {y.left.parent = x;}

    // parent swap - from lecture
    y.parent = x.parent;
    if (x.parent != null) {
      if (x.parent.right == x) {
        x.parent.right = y;
      } else {
        x.parent.left = y;
      }
    }

    // final update
    y.left = x;
    x.parent = y;

    // update heights of x & y!
    newHeight(x);
    newHeight(y);

    if (x == root) {root = y;}
  }

  /** inverse of leftRotate
   * do a right rotation: rotate on the edge from x to its left child.
   * precondition: y has a non-null left child
   */
  public void rightRotate(Node y) {
    Node x = y.left;
    
    // x right child -> y left child
    y.left = x.right;
    if (x.right != null) {x.right.parent = y;}

    // parent swap - from lecture
    x.parent = y.parent;
    if (y.parent != null) {
      if (y.parent.right == y) {
        y.parent.right = x;
      } else {
        y.parent.left = x;
      }
    }

    // final update
    x.right = y;
    y.parent = x;

    // update heights of y & x!
    newHeight(y);
    newHeight(x);

    if (y == root) {root = x;}
  }



  // rebalance a node N after a potentially AVL-violating insertion. 
  // height needs to be updated any time the tree’s structure changes
  // precondition: none of n's descendants violate the AVL property
  public void rebalance(Node n) {
    if (factor(n) < -1) {
      //node's left balance is less than zero
      if (factor(n.left) < 0) {
        rightRotate(n);
      }
      else { 
        leftRotate(n.left);
        rightRotate(n);
      }
    } 
    //non-balanced outcome
    else if (factor(n) > 1) {
      if (factor(n.right) < 0) {
        rightRotate(n.right);
        leftRotate(n);
      }
      else {leftRotate(n);}
    }
  }




  // helper method for rebalance
  // returns the balance factor of a given node
  // pre: height must be correct, AVL only
  private static int factor(Node n) {
    //base cases
    if (n == null) {return -1;}
    if (n.right == null && n.left == null) {return 0;}


    //calculations
    if (n.right == null) {return (-1 - n.left.height);}

    if (n.left == null) {return (n.right.height + 1);}

    return (n.right.height - n.left.height);
  }




  /** remove the word w from the tree */
  public void remove(String w) {
    remove(root, w);
  }

  /* remove v from the tree rooted at n */
  private void remove(Node n, String w) {
    return; // (enhancement TODO - do the base assignment first)
  }

  /**
   * print a sideways representation of the tree - root at left, right is up, left
   * is down.
   */
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
    System.out.println(n);
    printSubtree(n.left, level + 1);
  }

  // helper method for AVL trees, not used for BST
  // precondition: height is correct for n
  private void newHeight(Node n) {

    // base case: return if null
    if (n == null) {return;}

    // both null, height is zero -> other cases
    if (n.left == null && n.right == null) {
      n.height = 0;
    } else if (n.left == null) {
      n.height = (1 + n.right.height);
    } else if (n.right == null) {
      n.height = (1 + n.left.height);
    }
    // max of two heights
    else {
      n.height = Math.max(n.right.height, n.left.height) + 1;
    }

  }

  /** inner class representing a node in the tree. */
  public class Node {
    public String word;
    public Node parent;
    public Node left;
    public Node right;
    public int height;

    public String toString() {
      return word + "(" + height + ")";
    }

    /** constructor: gives default values to all fields */
    public Node() {
    }

    /** constructor: sets only word */
    public Node(String w) {
      word = w;
    }

    /** constructor: sets word and parent fields */
    public Node(String w, Node p) {
      word = w;
      parent = p;
    }

    /** constructor: sets all fields */
    public Node(String w, Node p, Node l, Node r) {
      word = w;
      parent = p;
      left = l;
      right = r;
    }
  }
}
