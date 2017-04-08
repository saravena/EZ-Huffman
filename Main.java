import java.util.*;
import java.io.*;

public class Main {
  public static void main (String[] args) throws IOException {
    String fileIn = args[0];  // assigns text file to string var
    File file = new File(fileIn);

    // checks to see if file exists
    if (!file.exists()) {
		  System.out.println("\nThis file does not exist.");
		} // end if

    // opens file
    Scanner inputFile = new Scanner(file);
    // creates root of tree (empty)
    BinaryNode<Character> root = new BinaryNode<Character>('\0');
    // reads next line in file
    inputFile.nextLine();

    // build tree on both sides
    root.setLeftChild(buildTree(inputFile, root));
    root.setRightChild(buildTree(inputFile, root));

    // creates a String array for each letter in the alphabet for encoding
    String[] encode = new String[26];
    // StringBuilder object used to hold direction to find the leaves
    StringBuilder direction = new StringBuilder(1);

    // create table
    buildTable(encode, direction, root);

    // stores user input
    Scanner input = new Scanner(System.in);
    // stores input into an integer variables
    int choice = 0;
    // used to store characters of binary leafs
    ArrayList<Character> store = new ArrayList<Character>();

    System.out.println("The Huffman Tree has been restored");
    // infinite loop unless user specifes to quit
    while(true) {
      System.out.println();
      System.out.println("Please choose from the following:");
      System.out.println("1) Encode a text string");
      System.out.println("2) Decode a Huffman string");
      System.out.println("3) Quit");
      System.out.println();

      // checks if input is not a integer
      if (input.hasNextInt()) {
        choice = input.nextInt();
        // Encode a text string
        if (choice == 1) {
          System.out.println("\nEnter a String from the following characters:");
          // print leaf characters
          printLeafNodes(store, encode);
          // user input
          String encodeIn = input.next();

          // Initialize
          int j = 0;
          boolean keepGoing = true;

          // check for valid input
          OuterLoop:
          for (int i = 0; i < encodeIn.length(); i++) {
            keepGoing = true; // reset
            while (keepGoing) {
              // success with character
              if (store.get(j) == encodeIn.charAt(i)) {
                // success with string
                if(i == encodeIn.length() - 1) {
                  System.out.println("Huffman String:");
                  // print huffman string of input
                  for (int m = 0; m < encodeIn.length(); m++) {
                      System.out.println(encode[(int)encodeIn.charAt(m)-65]);
                  } // end for
                } // end if
                keepGoing = false;
                j = 0;  // reset
              // invalid character
              } else if (j >= store.size() - 1) {
                keepGoing = false;
                System.out.println("There was an error in your text string");
                break OuterLoop;  // jump out of label
              } else {
                j++;  // continue
              }
            } // end while
          } // end for

        // Decode a Huffman string
        } else if (choice == 2) {
          System.out.println("\nHere is the encoding table:");
          // print table
          printTable(store, encode);
          System.out.println("Please enter a Huffman string (one line, no spaces)");
          String encodeIn = input.next();  // user input

          // intialize
          int j = 0;
          boolean keepGoing = true;
          ArrayList<Character> temp = new ArrayList<Character>();
          ArrayList<Character> output = new ArrayList<Character>();

          // check for valid Input
          Outerloop:
          for (int i = 0; i < encodeIn.length(); i++) {
            // add character in string to ArrayList
            temp.add(encodeIn.charAt(i));
            keepGoing = true; // reset
            while (keepGoing) {
                // if chars match
                if (toString(temp).equals(encode[j])) {
                  keepGoing = false;
                  output.add(store.get(j));
                  // if input is complete
                  if (i == (encodeIn.length() - 1)) {
                    System.out.println(toString(output));
                  }
                j = 0;  //reset
                temp.clear(); //reset
                break;
              // invalid input
              } else if (i >= (encodeIn.length() -1) && j >= (encode.length - 1)) {
                System.out.println("There was an error in your Huffman string");
                break Outerloop;
              // continue
              } else if (j >= encode.length -1) {
                j = 0;
                keepGoing = false;
              } else {
                j++;
              }
            } // end while
          } // end for

        // Quit
        } else if (choice == 3) {
          System.out.println("\nGood-bye!");
          System.exit(0);

        // Invalid Input
        } else {
          System.out.println("Input Invalid: Please enter a valid integer.");
        }
      // Ivalid Input
      } else {
        input.next();
        System.out.println("Input Invalid: Please enter a valid integer.");
      }
    } // end while
  } // end main


  // Recursively reads in each character of the file and builds tree
  private static BinaryNode<Character> buildTree(Scanner file, BinaryNode<Character> node) {
    String line = file.nextLine();  // sets next line to String
    BinaryNode<Character> treeRoot = null;  // creates a binary node object that is null
    // proceed if EOF is not reached
    if (line != null) {
      // populate with null charcter if 'I' is read in
      if (line.charAt(0) == 'I') {
        // populate with null character
        treeRoot = new BinaryNode<Character>('\0');
        // checks if left side is null
        if (node.getLeftChild() == null) {
          node.setLeftChild(treeRoot);
        // checks if right side is null
        } else if (node.getRightChild() == null) {
          node.setRightChild(treeRoot);
        }
        // recall method to proceed through file
        treeRoot.setLeftChild(buildTree(file, treeRoot));
        treeRoot.setRightChild(buildTree(file, treeRoot));

      // populate read in character to a leaf (base case)
      } else if (line.charAt(0) == 'L') {
        // populate root with leaf character
        BinaryNode<Character> treeLeaf = new BinaryNode<Character>(line.charAt(2)); // because line.charAt(1) is a space
        // checks if left side is null
        if (node.getLeftChild() == null) {
          node.setLeftChild(treeLeaf);
          return treeLeaf;
        // checks if right side is null
        } else if (node.getRightChild() == null) {
          node.setRightChild(treeLeaf);
          return treeLeaf;
        }
      }
    } // end if
    // return root of tree
    return treeRoot;
  } // end buildTree(Scanner, BinaryNode<Character>)

  // Recursive method used to traverse through tree and populate array with huff code
  // '0' is considered left, '1' is considered right
  public static void buildTable(String[] encode, StringBuilder direction, BinaryNode<Character> node) {
    // checks if a left child exists
    if (node.getLeftChild() != null) {
      direction.append("0");  // add 0 to code
      // recall method to proceed
      buildTable(encode, direction, node.getLeftChild());
      // BACKTRACKING OCCURRED!!!
      // removes last character in the code (WRONG DIRECTION)
      direction.deleteCharAt(direction.length() - 1);
    } // end if

    // checks if current node is leaf
    // Needs to be checked before a right child is checked
    if (node.isLeaf()) {
      // gets character at leaf
      char leafChar = node.getData();
      // subtracts ASCII value of leaf charcater by A(65) to get index for array
      int idx = (int)leafChar - 65;
      // add code to specifed index
      encode[idx] = direction.toString();
    } // end if

    // checks if a right child exists
    if (node.getRightChild() != null) {
      direction.append("1");  // add 0 to code
      // recall method to proceed
      buildTable(encode, direction, node.getRightChild());
      // BACKTRACKING OCCURRED!!!
      // removes last character in the code (WRONG DIRECTION)
      direction.deleteCharAt(direction.length() - 1);
    } // end if
  } // end buildTable(String[], StringBuilder, BinaryNode<Character>)

  // prints leaves of binary tree
  public static void printLeafNodes(ArrayList<Character> store, String[] encode) {
    for (int i = 0; i < encode.length; i++) {
      if (encode[i] != null) {
        // print specific ASCII character
        int temp = i + 65;
        char letter = (char)temp;
        // add to ArrayList
        store.add(letter);
        System.out.print(letter);
      } // end if
    } // end for
    System.out.println();
  } // end printLeafNodes

  // prints encoding table
  public static void printTable(ArrayList<Character> store, String[] encode) {
    for (int i = 0; i < encode.length; i++) {
      if (encode[i] != null) {
        // assigns specific ASCII character
        int temp = i + 65;
        char letter = (char)temp;
        // add to ArrayList
        store.add(letter);
        System.out.println(letter + ": " + encode[i]);
      } // end if
    } // end for
    System.out.println();
  } // end printTable(ArrayList<Character>, String[])

  // turns arraylist of characters to a string
  public static String toString(ArrayList<Character> temp) {
    String string = "";
    for (int i = 0; i < temp.size(); i++) {
       string += temp.get(i);
    } // end for
    return string;
  } // end toString(ArrayList<Character>)
} // end class Main
