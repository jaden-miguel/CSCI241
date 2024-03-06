//Author: Jaden Miguel
//Date: Spring 2020
//A1 SortsDriver main method

package sort;


import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class SortsDriver {

	//create objects 	
  static Sorts compSorts = new Sorts();
  static Sorts allSorts = new Sorts();
  public static int compCount;

  public static void main(String[] args) {

    Scanner input = new Scanner(System.in);
	System.out.println();
    //prompt user for which sort they would like
    System.out.print("Which sort: [m]erge, [i]nsertion, [q]uick, [r]adix, or [a]ll? ");
	String sortChoice = input.nextLine();
	

   
	System.out.print("Enter n (size of array to sort): ");
	int size = input.nextInt();
	int[] nums = randomArray(size);
	int[] nums2;
	
    
	switch (sortChoice) {
		case "m": 
		nums2 = deepCopy(nums);
		compSorts.mergeSort(nums, 0, nums.length);
		compCount = compSorts.getComparisonCount();
		display(nums2, nums, nums.length, compCount);
		break;
		

		case "i":
		nums2 = deepCopy(nums);
		compSorts.insertionSort(nums, 0, nums.length);
		compCount = compSorts.getComparisonCount();
		display(nums2, nums, nums.length, compCount);
		break;


		case "q":
		nums2 = deepCopy(nums);
		compSorts.quickSort(nums, 0, nums.length);
		compCount = compSorts.getComparisonCount();
		display(nums2, nums, nums.length, compCount);
		break;

		
		case "r":
		nums2 = deepCopy(nums);
		compSorts.radixSort(nums);
		display(nums2, nums, nums.length, 0);
		break;


		case "a":
		//print and calculate insertion, quick, and mergesort.
		nums2 = deepCopy(nums);
		int[] insertion = deepCopy(nums);
		int[] quick = deepCopy(nums);
		int[] merge = deepCopy(nums);

		if(nums.length <= 20) {
			//print unsorted array
			System.out.print("Unsorted Array: [ ");
			for (int i = 0; i < nums2.length; i++) {
				System.out.print(nums2[i] + " ");
			}
			System.out.println("]\n");
		}

		//calculate insertion & print 
		allSorts.insertionSort(insertion, 0, insertion.length);
		System.out.println("Insertion: " + allSorts.getComparisonCount() + " comparisons!");
		allSorts.resetComparisonCount();

		if (nums.length <= 20) {
			System.out.print("Sorted: [ ");
			for (int i = 0; i < insertion.length; i++) {
				System.out.print(insertion[i] + " ");
			}
			System.out.println("]\n");
		}

		//calculate quicksort & print
		allSorts.quickSort(quick, 0, quick.length);
		System.out.println("Quick: " + allSorts.getComparisonCount() + " comparisons!");
		allSorts.resetComparisonCount();

		if (nums.length <= 20) {
			System.out.print("Sorted: [ ");
			for (int i = 0; i < quick.length; i++) {
				System.out.print(quick[i] + " ");
			}
			System.out.println("]\n");
		}


		//calculate mergesort& print
		allSorts.mergeSort(merge, 0, merge.length);
		System.out.println("Merge: " + allSorts.getComparisonCount() + " comparisons!");
		allSorts.resetComparisonCount();

		if(nums.length <= 20) {
			System.out.print("Sorted: [ ");
			for (int i = 0; i < merge.length; i++) {
				System.out.print(merge[i] + " ");
			}
			System.out.println("]\n");
		}
		break;
		
		//secret function countSort :0
		case "c":
		nums2 = deepCopy(nums);
		compSorts.countSort(nums);
		display(nums2, nums, nums.length, 0);
		break;
	
	}

	//close scanner
	input.close();

}




	//helper methods

  // randomArray method creates a randomly generated array 
  // specified range of -n and n+1 
	private static int[] randomArray(int n) {
		int[] numArray = new int[n];
		for (int i = 0; i < numArray.length; i++) {
			numArray[i] = ThreadLocalRandom.current().nextInt(-n, n+1);
		}
		return numArray;
	}



  //create a deep copy of the array nums
  private static int[] deepCopy(int[] nums) {
		int[] numArray = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			numArray[i] = nums[i];
		}
		return numArray;
  }
  

  private static void display(int[] unSorted, int[] sorted, int arraySize, int numCompares) {
	//if array is greater than twenty, DON't print each sort
	if (arraySize <= 20) {
		System.out.print("Unsorted Array: ");
		System.out.print("[ ");
		for (int i = 0; i < unSorted.length; i++) {
			System.out.print(unSorted[i] + " ");
		}
		System.out.println("]");

		System.out.print("Sorted Array: ");
		System.out.print("[ ");
		for (int j = 0; j < sorted.length; j++) {
			System.out.print(sorted[j] + " ");
		}
		System.out.print("]");
		System.out.println("\nComparisons: " + numCompares);
    } 
    else {
		System.out.println("Comparisons: " + numCompares + "\n");
    }
    //reset comparisons
	compSorts.resetComparisonCount();
	System.out.println();
}





}

