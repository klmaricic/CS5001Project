/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kelsey
 */
public class MergeSort {
    private List<Entry> entryArr;
    private List<Entry> helper;
    private int length;

    public void sort(List<Entry> arr) {
        this.entryArr = arr;
        length = entryArr.size();
        this.helper = new ArrayList<>(length);
        mergesort(0, length - 1);
    }

    private void mergesort(int low, int high) {
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            
            // Sort the left side of the array
            mergesort(low, middle);
            
            // Sort the right side of the array
            mergesort(middle + 1, high);
            
            // Combine them both
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {
        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            helper.set(i, entryArr.get(i));
        }

        int i = low;
        int j = middle + 1;
        int k = low;
        
        // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high) {
            if (helper.get(i).getCount() >= helper.get(j).getCount()) {
                entryArr.set(k, helper.get(i));
                i++;
            } 
            else {
                entryArr.set(k, helper.get(j));
                j++;
            }
            
            k++;
        }
        
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            entryArr.set(k, helper.get(i));
            k++;
            i++;
        }
    }
} 
