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
public class FPGrowth {
    private int numAttributes, numInstances, minCount, maxSetSize, numRules, maxEntries;
    double minAccuracy;
    private String file, line = "";
    private BufferedReader br = null;
    private ArffLoader.ArffReader arff;
    private Instances data;
    private ArrayList<Entry> frequentSet = new ArrayList<>();
    private ArrayList<ItemSet> dataSet = new ArrayList<>();
    private ArrayList<ItemSet> consequents = new ArrayList<>();
    private ArrayList<ItemSet> antecedents = new ArrayList<>();
    private ArrayList<RuleSet> rules = new ArrayList<>();
    
    public FPGrowth(String filePath, int minCoverage, int maxSize, double a, int rules){
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
    
    public void createFrequentSet() {
        //Add value so initialSet isn't empty
        frequentSet.add(new Entry(data.instance(0).stringValue(0), 0));
        
        //Loops through the dataset and creates an array containing every value and counts how often that value appears
        for(int col = 0; col < numAttributes; col++) {
            for(int row = 0; row < numInstances; row++) { 
                for(int k = 0; k < frequentSet.size(); k++) {
                    //if the initialSet contains the current value, then stop looking and increment count
                    if((frequentSet.get(k).getValue()).equals(data.instance(row).stringValue(col)) && (frequentSet.get(k).getAttNum() == col) ) {
                        frequentSet.get(k).countIncrement();
                        break;
                    }
                    //If it searched through the entire set and did not find the value, then add it
                    else if(k == frequentSet.size()-1)
                        frequentSet.add(new Entry(data.instance(row).stringValue(col), col));
                }
            }
        }

        //removes all entries that don't meet the minimum coverage
        for(int i = frequentSet.size()-1; i >= 0; i--) {
            if(frequentSet.get(i).getCount() < minCount)
                frequentSet.remove(i);  
        } 
        
        bubbleSort();
        System.out.println("\nFrequent Set:");
        for(int i = 0; i < frequentSet.size(); i++) {
            int attNum = frequentSet.get(i).getAttNum();
            System.out.println(data.attribute(attNum).name()+" = "+frequentSet.get(i).getValue()+" (Coverage = "+frequentSet.get(i).getCount()+")");
        }
        System.out.println();
    }
    
    //Sorts the values of each instance in descending order based on frequency
    public void sortInstances() {
        for(int i = 0; i < numInstances; i++) {
            //Creates an ArrayList for each instance and stores it in the dataSet ArrayList
            dataSet.add(new ItemSet());
            
            //Adds the values that meet the coverage minimum to the corresponding instance ArrayList in order of descending coverage
            for(int j = 0; j < frequentSet.size(); j++) {
                for(int k = 0; k < numAttributes; k++) {
                    //if the value in the arff dataSet equals the value in the frequentSet and has same attribute number
                    if(data.instance(i).stringValue(k).equals(frequentSet.get(j).getValue()) && k == frequentSet.get(j).getAttNum()) {
                        dataSet.get(dataSet.size()-1).add(data.instance(i).stringValue(k), k);
                    }
                }
            }
        }
        
        System.out.println("Datasets:");
        for(int j = 0; j < dataSet.size(); j++) {
            for(int i = 0; i < dataSet.get(j).size(); i++) {
            int attNum = dataSet.get(j).get(i).getAttNum();
            if(i < dataSet.size()-1)
                System.out.print(data.attribute(attNum).name()+" = "+dataSet.get(j).get(i).getValue()+", ");
            else
                System.out.print(data.attribute(attNum).name()+" = "+dataSet.get(j).get(i).getValue());
            }
            System.out.println();
        }

    }
    
   
	
    public void createFPTree() {
        //Max possible number of entries in an instance after sort instances
        maxEntries = frequentSet.size();
        Tree fpTree = new Tree();
        //fpTree.buildTree(dataSet, maxEntries);
        
        for(int set = 0; set < dataSet.size()-1; set++) {
            comb(dataSet.get(set).getArr());
            sortConsequents();
        
            for(int i = consequents.size()-2; i >= 0; i--)
                antecedents.add(consequents.get(i));
        
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

            antecedents.clear();
            consequents.clear();       
        
        }
        
        sortRules();
        
        System.out.println();
        if(numRules == -1) {
            System.out.println("All of the rules meeting your requirements are:");
            for(int i = 0; i < rules.size(); i++)
            rules.get(i).print(data);
        }
        else {
            System.out.println("The top "+numRules+" rules matching your requirements are:");
            for(int i = 0; i < numRules; i++) {
                if(i >= rules.size()) {
                    System.out.println("There are no more rules matching the accuracy requirement");
                    break;
                }
                rules.get(i).print(data);
            }
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
                if(rules.get(j).getAccuracy() < rules.get(j+1).getAccuracy()) {
                    temp = rules.get(j);                //swap elements
                    rules.set(j, rules.get(j+1));
                    rules.set(j+1, temp);
                    flag = true;              //shows a swap occurred  
                } 
            } 
        } 
    }
    
    public void bubbleSort() {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        Entry temp;   //holding variable

        while (flag) {
            flag= false;    //set flag to false awaiting a possible swap
            for(j=0;  j < frequentSet.size()-1;  j++) {
                if(frequentSet.get(j).getCount() < frequentSet.get(j+1).getCount()) {
                    temp = frequentSet.get(j);                //swap elements
                    frequentSet.set(j, frequentSet.get(j+1));
                    frequentSet.set(j+1, temp);
                    flag = true;              //shows a swap occurred  
                } 
            } 
        } 
    } 
}

