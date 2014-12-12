/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Kelsey
 */
public class ItemSet {

    private int count;
    private double denominator, numerator;
    private ArrayList<Entry> instance;

    public ItemSet() {
        count = 0;
        denominator = 0;
        numerator = 0;
        instance = new ArrayList<>();
    }
    
    //clones the old item set into the new one
    public ItemSet(ItemSet set) {
        instance = new ArrayList<>(set.size()+1);
        count = 0;
        denominator = 0;
        numerator= 0;
        for(int i =0; i <set.size(); i++) {
            instance.add(set.get(i));
        }
    }
    
    public ItemSet(String val, int attNum) {
        instance = new ArrayList<>();
        instance.add(new Entry(val, attNum));
        count = 0;
        denominator = 0;numerator = 0;
    }
    
    public ItemSet(String val, int attNum, int c) {
        instance = new ArrayList<>();
        instance.add(new Entry(val, attNum));
        count = c;
        denominator = 0;numerator = 0;
    }

    public Entry get(int i) {
        return instance.get(i);
    }
    
    public ArrayList<Entry> getArr() {
        return instance;
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
    
    public void print(Instances data) {
        
        for(int i = 0; i < instance.size(); i++) {
            int attNum = instance.get(i).getAttNum();
            if(i < instance.size()-1)
                System.out.print(data.attribute(attNum).name()+" = "+instance.get(i).getValue()+", ");
            else
                System.out.print(data.attribute(attNum).name()+" = "+instance.get(i).getValue());
        }
                
        System.out.println(" (Coverage = "+count+")");
    }
    
    public void incDenom() {
        denominator++;
    }
    
    public void incNum() {
        numerator++;
    }
    
    public double getAccuracy() {
        return numerator/denominator;
    }
    
    public double getDenom() {
        return denominator;
    }
    
    public boolean contains(ItemSet a) {
        if(this.size() == 0)
            return false;
        
        if(a.size() == 0)
            return true;
        
        for(int i = 0; i < a.size(); i++) {
            for(int j = 0; j < this.size(); j++) {
                if(a.get(i).getValue().equals(this.get(j).getValue()) && a.get(i).getAttNum() == this.get(j).getAttNum())
                    break;
                else if(j >= this.size()-1)
                    return false;
            }
            
        }
        
        return true;
    }
}
