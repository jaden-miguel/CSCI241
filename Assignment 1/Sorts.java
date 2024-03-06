//Author: Jaden Miguel
//Date: Spring 2020
//Algorithms, this is the sorts class

package sort;

import org.checkerframework.checker.units.qual.A;
import java.util.*;

public class Sorts {

  // maintains a count of comparisons performed by this Sorts object
  private int comparisonCount;

  private final static int BITS_PER_BYTE = 8;

  public int getComparisonCount() {
    return comparisonCount;
  }

  public void resetComparisonCount() {
    comparisonCount = 0;
  }

  /**
   * Sorts A[start..end] in place using insertion sort Precondition: 0 <= start <=
   * end <= A.length
   */
  public void insertionSort(int[] A, int start, int end) {
    // create key for comparison
    int key;
    for (int i = 1; i < end; i++) {
      // set key to the looping index, starting at the 2nd element
      key = A[i];
      // define and set comparer value to the index before the looping index
      int j = (i - 1);

      //compare with key
      while (j >= 0 && A[j] > key) {
        comparisonCount++;
        A[j + 1] = A[j];
        j--;
      }

      comparisonCount++;
      A[j + 1] = key;
    }
  }

  /**
   * Partitions A[start..end] around the pivot A[pivIndex]; returns the pivot's
   * new index. Precondition: start <= pivIndex < end Postcondition: If partition
   * returns i, then A[start..i] <= A[i] <= A[i+1..end]
   **/
  public int partition(int[] A, int start, int end, int pivIndex) {
    // find pivot value, and index of end
    int pivot = A[pivIndex];
    int endIdx = (end - 1);
    // swap pivot to back of array
    swap(A, pivIndex, endIdx);
    int index = start;

    // start looping over array, check if i is less than or equal
    // to the pivot.
    for (int i = start; i < endIdx; i++) {
      if ((A[i]) <= (pivot)) {
        comparisonCount++;
        swap(A, i, index);
        index++;
      }
      comparisonCount++;
    }
      swap(A, endIdx, index);
      return index;
  }

  // Recursive quicksort, uses partition with random pivot
  /** use quicksort to sort the subarray A[start..end] */
  public void quickSort(int[] A, int start, int end) {
    if (start < end) {
      // assign random pivot value
      int pivIndex = (int)(Math.random() * (end - start) + start);

      int mid = partition(A, start, end, pivIndex);

      quickSort(A, start, mid);
      quickSort(A, mid + 1, end);
    }
  }


  /**
   * merge the sorted subarrays A[start..mid] and A[mid..end] into a single sorted
   * array in A.
   */
  public void merge(int[] A, int start, int mid, int end) {
    if (A[mid - 1] <= A[mid]) {
      return;
    }

    int i = start;
    int j = mid;
    int tempIndex = 0;

    int[] temp = new int[end - start];
    while (i < mid && j < end) {
      temp[tempIndex++] = A[i] <= A[j] ? A[i++] : A[j++];
      comparisonCount++;
    }
    comparisonCount++;

    System.arraycopy(A, i, A, start + tempIndex, mid - i);
    System.arraycopy(temp, 0, A, start, tempIndex);
  }


  // Precondition: A is not null
  /** use mergesort to sort the subarray A[start..end] recursively */
  public void mergeSort(int[] A, int start, int end) {
    if (end - start < 2) {
      return;
    }

    // split array 
    int mid = (start + end) / 2;

    mergeSort(A, start, mid);
    mergeSort(A, mid, end);
    merge(A, start, mid, end); //where most of algorithm is done
  }

  

  // Sedgewick counting sort algorithm, 4 passes 
  // Sort A using LSD radix sort. 
  // wow! sorting without comparing :)
  public void radixSort(int[] A) {

    int BITS = 32;                 // each int is 32 bits 
    int W = BITS / BITS_PER_BYTE;  // each int is 4 bytes
    int R = 1 << BITS_PER_BYTE;    // each byte is 0-255
    int MASK = R - 1;              // mask value

    int N = A.length;
    int[] aux = new int[N];

    for (int d = 0; d < W; d++) {         

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++) {           
            int c = (A[i] >> BITS_PER_BYTE*d) & MASK;
            count[c + 1]++;
        }

        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
        if (d == W-1) {
            //calculate shifts
            int shift1 = count[R] - count[R/2];
            int shift2 = count[R/2];
            for (int r = 0; r < R/2; r++)
                count[r] += shift1;
            for (int r = R/2; r < R; r++)
                count[r] -= shift2;
        }

        // move data
        for (int i = 0; i < N; i++) {
            int c = (A[i] >> BITS_PER_BYTE*d) & MASK;
            aux[count[c]++] = A[i];
        }

        // copy back to original array
        for (int i = 0; i < N; i++)
            A[i] = aux[i];
    }
  }


  // ENHANCEMENT method :)
  // Not comparison based, O(n) w/ proportion to range.
  // Precondition: A is not null, may contain negative values.
  // A counting sort algorithm which uses max & min.  

  public void countSort(int[] A) {
    //get max & min + range - java util

    int max = Arrays.stream(A).max().getAsInt(); 
    int min = Arrays.stream(A).min().getAsInt(); 
    int range = (max - min + 1); 
    int count[] = new int[range]; 
    int output[] = new int[A.length]; 

    for (int i = 0; i < A.length; i++) { 
      count[A[i] - min]++; 
    } 
  
    for (int i = 1; i < count.length; i++) { 
      count[i] += count[i - 1]; 
    } 
  
    for (int i = (A.length - 1); i >= 0; i--) { 
      output[count[A[i] - min] - 1] = A[i]; 
      count[A[i] - min]--; 
    } 
    
    //copy array back to A
    for (int i = 0; i < A.length; i++) { 
      A[i] = output[i]; 
    } 
  }


 


  /**
   * swap a[i] and a[j] pre: 0 <= i, j < a.size post: values in a[i] and a[j] are
   * swapped
   */
  public void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }



  
  



}
