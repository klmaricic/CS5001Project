/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.util.ArrayList;

/**
 *
 * @author Kelsey
 */
public class ItemSet {

    private int count;
    private ArrayList<Entry> instance;

    public ItemSet() {
        count = 0;
        instance = new ArrayList<>();
    }

    public Entry get(int i) {
        return instance.get(i);
    }
    
    public void add(String val, int attNum) {
        instance.add(new Entry(val, attNum));
    }
    
    public int getCount() {
        return count;
    }
    
    public int size() {
        return instance.size();
    }
}
