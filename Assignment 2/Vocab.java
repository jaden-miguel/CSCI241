// Author: Jaden Miguel
// A2 CSCI241 - Spring 2020
// efficiently O(n log n) count the number of unique words
// in a txt document.

package avl;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Vocab {

  public static void main(String[] args) {
    //switch to standard input if no args
    if (args.length == 0) {
      Count c = wordCount(new Scanner(System.in));
      System.out.println(c);
      return;
    }
    else {
      //allows for multiple inputs
      for (int i = 0; i < args.length; i++) { 
        try {
          File f = new File(args[i]);
          Scanner sc = new Scanner(f);
          Count c = wordCount(sc);
          System.out.println(c);
        } 
        catch (FileNotFoundException exc) {
          System.out.println("Could not find file " + args[i]);
        }
        
      }
    }
  }
  

  /*
   * count the total and unique words in a document being read by the given
   * Scanner. return the two values in a Count object.
   */
  private static Count wordCount(Scanner sc) {
    AVL tree = new AVL();
    Count c = new Count();

    // fill in the unique and total fields of c
    // before c is returned

    while (sc.hasNext()) {
      // read and parse each word
      String word = sc.next();
      c.total++;
      // remove non-letter characters, convert to lower case:
      word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
      tree.avlInsert(word);
    }
    c.unique = tree.getSize();
    return c;
  }

}
