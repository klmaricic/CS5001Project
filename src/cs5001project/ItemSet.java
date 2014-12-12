/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.util.ArrayList;
import weka.core.Instance;

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
    
    //clones the old item set into the new one
    public ItemSet(ItemSet set) {
        instance = new ArrayList<>(set.size()+1);
        count = 0;
        for(int i =0; i <set.size(); i++) {
            instance.add(set.get(i));
        }
    }
    
    public ItemSet(String val, int attNum) {
        instance = new ArrayList<>();
        instance.add(new Entry(val, attNum));
        count = 0;
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
    public void countInc() {
        count++;
    }
    
    public int size() {
        return instance.size();
    }
    
    public boolean containsAtt(int attNum) {
        for(int i = 0; i < instance.size(); i++) {
            if(instance.get(i).getAttNum() == attNum){
                System.out.println("TRUE");
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isIn(Instance inst) {
        for(int col = 0; col < instance.size(); col++) {
            int att = instance.get(col).getAttNum();
            if(!inst.stringValue(att).equals(instance.get(col).getValue()))
                return false;
        }
        return true;
    }
    
    public void print() {
        for(int i = 0; i < instance.size(); i++)
            System.out.print(instance.get(i).getValue()+"("+instance.get(i).getAttNum()+")"+", ");
        System.out.println();
    }
}
