/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Kelsey
 */
public class Apriori {
    int numAttributes, numInstances, minCount, maxSetSize, numRules;
    double minAccuracy;
    String file, line = "";
    BufferedReader br = null;
    ArffLoader.ArffReader arff;
    Instances data;
    private ArrayList<Entry> initialSet = new ArrayList<>();
    private ArrayList<ItemSet> dataSet = new ArrayList<>();
    private ArrayList<ItemSet> consequents = new ArrayList<>();
    private ArrayList<ItemSet> antecedents = new ArrayList<>();
    private ArrayList<RuleSet> rules = new ArrayList<>();
    
    public Apriori(String filePath, int minCoverage, int maxSize, double a, int rules){
        file = filePath;
        minCount = minCoverage;
        maxSetSize = maxSize;
        minAccuracy = a;
        numRules = rules;
        
        try{
            br = new BufferedReader(new FileReader(file));  
        } catch(FileNotFoundException e) {}
        try {
            arff = new ArffLoader.ArffReader(br);
        } catch (IOException ex) {
            Logger.getLogger(Apriori.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        data = arff.getData();
        data.setClassIndex(data.numAttributes() - 1);
        
        numAttributes = data.numAttributes();
        numInstances = data.numInstances();
    }
    
    
    public void createSet() {
        if(maxSetSize == 0) {
            System.out.println("Max set size was 0, so no sets can be built");
            return;
        }
        
        //Add value so initialSet isn't empty
        initialSet.add(new Entry(data.instance(0).stringValue(0), 0));
        
        //Loops through the dataset and creates an array containing every value and counts how often that value appears
        for(int col = 0; col < numAttributes; col++) {
            for(int row = 0; row < numInstances; row++) { 
                for(int k = 0; k < initialSet.size(); k++) {
                    //if the initialSet contains the current value, then stop looking and increment count
                    if((initialSet.get(k).getValue()).equals(data.instance(row).stringValue(col)) && (initialSet.get(k).getAttNum() == col) ) {
                        initialSet.get(k).countIncrement();
                        break;
                    }
                    //If it searched through the entire set and did not find the value, then add it
                    else if(k == initialSet.size()-1)
                        initialSet.add(new Entry(data.instance(row).stringValue(col), col));
                }
            }
        }

        //removes all entries that don't meet the minimum coverage
        for(int i = initialSet.size()-1; i >= 0; i--) {
            if(initialSet.get(i).getCount() < minCount)
                initialSet.remove(i);
            //If the 1-item set meets the min coverage, add to dataset
            else 
                dataSet.add(0, new ItemSet(initialSet.get(i).getValue(), initialSet.get(i).getAttNum()));   
        } 

        if(dataSet.isEmpty()) 
            System.out.println("None of the item sets met the min coverage");
        else 
            recursiveSetBuild(0);
    }
    
    public void recursiveSetBuild(int startIndex) {
        int start = startIndex;
        int startSize = dataSet.size();
        int startSetSize = dataSet.get(startSize-1).size();
       
        if(dataSet.get(startSize-1).size() >= numAttributes) 
            return;
        
        if(dataSet.get(dataSet.size()-1).size() >= maxSetSize)
            return;
        
        for(int row = start; row < startSize; row++) {
            for(int j = 0; j < initialSet.size(); j++) {
                if(initialSet.get(j).getAttNum() > dataSet.get(row).get(dataSet.get(row).size()-1).getAttNum()) {
                    dataSet.add(new ItemSet(dataSet.get(row)));
                    dataSet.get(dataSet.size()-1).add(initialSet.get(j).getValue(), initialSet.get(j).getAttNum());
                }
            }
        }
       
        for(int row = startSize; row < dataSet.size(); row++) {
            for(int inst = 0; inst < numInstances; inst++) {
                if(dataSet.get(row).isIn(data.instance(inst)))
                    dataSet.get(row).countInc();
            }
        }
        
        for(int row = dataSet.size()-1; row >= startSize; row--) {
            if(dataSet.get(row).getCount() < minCount)
                dataSet.remove(row);
        }
        
        //If none of the new sets met the min coverage, then the set builder is finished
        if(dataSet.get(dataSet.size()-1).size() == startSetSize) 
            return;
       
        
        recursiveSetBuild(startSize);
    }
    
    public void createRules() {
        
        comb(dataSet.get(dataSet.size()-1).getArr());
        sortConsequents();
        
        for(int i = consequents.size()-2; i >= 0; i--){
            antecedents.add(consequents.get(i));
        }
        System.out.println("ANTECEDENTS");
        for(int i = 0; i < antecedents.size(); i++){
            antecedents.get(i).print();
            System.out.println();
        }
        System.out.println("/ANTECEDENTS");
            
        
        //Finds the accuracy of each rule
        for(int ant =0; ant < antecedents.size(); ant++) {
            for(int row = 0; row < data.numInstances(); row++) {
                for(int e = 0; e < antecedents.get(ant).size(); e++) {
                    int num = antecedents.get(ant).get(e).getAttNum();
                    //If the antecedent condition does not match the instance
                    if(!(antecedents.get(ant).get(e).getValue().equals(data.instance(row).toString(num))))
                        break;
                    //If all of the antecedent conditions match the instance
                    if(e >= (antecedents.get(ant).size()-1)) {
                        antecedents.get(ant).incDenom();
                        
                        for(int c = 0; c < consequents.get(ant).size(); c++) {
                            num = consequents.get(ant).get(c).getAttNum();
                            //If the consequents condition does not match the instance
                            if(!(consequents.get(ant).get(c).getValue().equals(data.instance(row).toString(num))))
                                break;
                            //If all of the consequents conditions match the instance
                            if(c >= (consequents.get(ant).size()-1)) {
                                antecedents.get(ant).incNum();
                            }
                        }
                    }
                }
            }
            
            if((antecedents.get(ant).getDenom() != 0) && (antecedents.get(ant).getAccuracy() >= minAccuracy)) 
                    rules.add(new RuleSet(antecedents.get(ant).getArr(), consequents.get(ant).getArr(), antecedents.get(ant).getAccuracy()));
            else {
                for(int i= (antecedents.size()-1); i > ant; i--) {
                    if(antecedents.get(ant).contains(antecedents.get(i)))
                        antecedents.remove(i);
                }
            }
        }
        System.out.println("CUT ANTECEDENTS");
        for(int i = 0; i < antecedents.size(); i++){
            antecedents.get(i).print();
            System.out.println();
        }
        System.out.println("RULES");
        for(int i = 0; i < rules.size(); i++)
            rules.get(i).print();
        
        sortRules();
        
        System.out.println("SORTED RULES");
        for(int i = 0; i < rules.size(); i++)
            rules.get(i).print();
        
        System.out.println("The top "+numRules+" are:");
        for(int i = 0; i < numRules; i++) {
            if(i >= rules.size()) {
                System.out.println("There are no more rules matching the accuracy requirement");
                break;
            }
            rules.get(i).print();
        }
            
    }
    
    public void comb(ArrayList<Entry> e) { 
        comb(new ArrayList<Entry>(), e); 
    }

    
    public  void comb(ArrayList<Entry> prefix, ArrayList<Entry> e) {
        if (e.size() > 0) {
            consequents.add(new ItemSet());
            for(int i = 0; i < prefix.size(); i++) {
                //System.out.print(prefix.get(i).getValue()+", ");
                consequents.get(consequents.size()-1).add(prefix.get(i).getValue(), prefix.get(i).getAttNum());
            }
            consequents.get(consequents.size()-1).add(e.get(0).getValue(), e.get(0).getAttNum());
            //System.out.println(e.get(0).getValue());
            
            
            ArrayList<Entry> temp = new ArrayList<>();
            
            for(int j = 1; j < e.size(); j++)
                temp.add(e.get(j));
            
            prefix.add(e.get(0));
            comb(prefix, temp);
            
            prefix.remove(prefix.size()-1);
            comb(prefix, temp);
            
        }
    }  
    
    public void sortConsequents() {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        ItemSet temp;   //holding variable

        while (flag) {
            flag= false;    //set flag to false awaiting a possible swap
            for(j=0;  j < consequents.size()-1;  j++) {
                if(consequents.get(j).size() > consequents.get(j+1).size()) {
                    temp = consequents.get(j);                //swap elements
                    consequents.set(j, consequents.get(j+1));
                    consequents.set(j+1, temp);
                    flag = true;              //shows a swap occurred  
                } 
            } 
        } 
    }
    
    public void sortRules() {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        RuleSet temp;   //holding variable

        while (flag) {
            flag= false;    //set flag to false awaiting a possible swap
            for(j=0;  j < rules.size()-1;  j++) {
                if(rules.get(j).getAccuracy() < consequents.get(j+1).getAccuracy()) {
                    temp = rules.get(j);                //swap elements
                    rules.set(j, rules.get(j+1));
                    rules.set(j+1, temp);
                    flag = true;              //shows a swap occurred  
                } 
            } 
        } 
    }
}