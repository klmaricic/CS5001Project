/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

/**
 *
 * @author Kelsey
 */
public class Entry {
    int count, attributeNum;
    String value;
    
    public Entry(String val, int attNum) {
        count = 1;
        value = val;
        attributeNum = attNum;
    }
    
    public void countIncrement() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
    
    public String getValue() {
        return value;
    }
    
    public int getAttNum() {
        return attributeNum;
    }
}
