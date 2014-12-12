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
    private int count, attributeNum;
    private String value;
    
    public Entry(String val, int attNum) {
        count = 0;
        value = val;
        attributeNum = attNum;
    }
    
    public void countIncrement() {
        count++;
    }
    
    public void countDec() {
        count--;
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
