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
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Kelsey
 */
public class Apriori {
    int numAttributes, numInstances, minCount, maxSetSize;
    String file, line = "";
    BufferedReader br = null;
    ArffLoader.ArffReader arff;
    Instances data;
    private ArrayList<Entry> initialSet = new ArrayList<>();
    private ArrayList<ItemSet> dataSet = new ArrayList<>();
    
    public Apriori(String filePath, int minCoverage, int maxSize){
        file = filePath;
        minCount = minCoverage;
        maxSetSize = maxSize;
        
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
            else {
                dataSet.add(0, new ItemSet(initialSet.get(i).getValue(), initialSet.get(i).getAttNum()));
            }     
        } 


        if(dataSet.isEmpty()) 
            System.out.println("None of the item sets met the min coverage");
        else 
            recursiveSetBuild(0);
       
        for(int i = 0; i <dataSet.size(); i++) {
            System.out.println();
            for(int j = 0; j < dataSet.get(i).size(); j++)
                System.out.print(dataSet.get(i).get(j).getValue()+", ");
        }
    }
    
    public void recursiveSetBuild(int startIndex) {
        int start = startIndex;
        int startSize = dataSet.size();
        int startSetSize = dataSet.get(startSize-1).size();
       
        if(dataSet.get(startSize-1).size() >= numAttributes) {
            return;
        }
        
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
                if(dataSet.get(row).isIn(data.instance(inst))) {
                    dataSet.get(row).countInc();
                }
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
        
    }
}