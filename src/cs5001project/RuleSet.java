/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.util.ArrayList;
import weka.core.Instances;

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
    
    public void print(Instances data) {
        for(int i = 0; i < antecedents.size(); i++) {
            int attNum = antecedents.get(i).getAttNum();
            
            if(i == 0 && antecedents.size() > 1)
                System.out.print("If "+data.attribute(attNum).name()+" = "+antecedents.get(i).getValue()+" and ");
            else if(i == 0)
                System.out.print("If "+data.attribute(attNum).name()+" = "+antecedents.get(i).getValue());
            else if(i < antecedents.size()-1) 
                System.out.print(data.attribute(attNum).name()+" = "+antecedents.get(i).getValue()+" and ");
            else
                System.out.print(data.attribute(attNum).name()+" = "+antecedents.get(i).getValue());
        }
        System.out.print("\n    then ");
        for(int i = 0; i < consequents.size(); i++) {
            int attNum = consequents.get(i).getAttNum();
            if(i < consequents.size()-1)
                System.out.print(data.attribute(attNum).name()+" = "+consequents.get(i).getValue()+" and ");
            else
                System.out.print(data.attribute(attNum).name()+" = "+consequents.get(i).getValue());
        }
        System.out.println(" ("+accuracy+"%)");
    }
    
    public double getAccuracy() {
        return accuracy;
    }
}
