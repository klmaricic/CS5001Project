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
public class RuleSet {
    private ArrayList<Entry> consequents = new ArrayList<>();
    private ArrayList<Entry> antecedents = new ArrayList<>();
    double accuracy;
    
    public RuleSet(ArrayList<Entry> ant, ArrayList<Entry> con, double acc) {
        accuracy = acc;
        consequents.addAll(con);
        antecedents.addAll(ant);
    }
    
    public void print() {
        for(int i = 0; i < antecedents.size(); i++)
            System.out.print(antecedents.get(i).getValue()+", ");
        System.out.print("| ");
        for(int i = 0; i < consequents.size(); i++)
            System.out.print(consequents.get(i).getValue()+", ");
        System.out.println(accuracy+"%");
    }
    
    public double getAccuracy() {
        return accuracy;
    }
}
