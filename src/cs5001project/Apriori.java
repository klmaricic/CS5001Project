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
    int numAttributes, numInstances, minCount;
    String file, line = "";
    BufferedReader br = null;
    ArffLoader.ArffReader arff;
    Instances data;
    private ArrayList<Entry> initialSet = new ArrayList<>();
    private ArrayList<ArrayList<Entry>> dataSet = new ArrayList<>();
    
    public Apriori(String filePath, int minCoverage){
        file = filePath;
        minCount = minCoverage;
        
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
        
        System.out.println((data.toString()));
    }
    
    public void apriori(){
        for(int setSize = 0; setSize < numAttributes; setSize++) { 
            for(int instance = 0; instance < numInstances; instance++) {
		for(int value = 0; value < setSize; value++) {
			//arrList.add((data.instance(instance)).toString(value));
		}

            }
        }
    }
    
    public void createInitialSet() {
        //Loops through the dataset and creates an array containing every value and counts how often that value appears
        for(int i = 0; i < numInstances; i++) {
            for(int j = 0; j < numAttributes; j++) {
                for(int k = 0; k < initialSet.size(); k++) {
                    //if the frequentSet contains the current value, then stop looking and increment count
                    if((initialSet.get(k).getValue()).equals(data.instance(i).stringValue(j)) && (frequentSet.get(k).getAttNum() != j) ) {
                        int index = initialSet.indexOf(new Entry(data.instance(i).stringValue(j), j));
                        initialSet.get(index).countIncrement();
                        break;
                    }
                    //If it searched through the entire set and did not find the value, then add it
                    else if(k == initialSet.size()-1)
                        initialSet.add(new Entry(data.instance(i).stringValue(j), j));
                }
            }
        }
        
        //removes all entries that don't meet the minimum coverage
        for(int i = initialSet.size()-1; i >= 0; i--) {
            if(initialSet.get(i).getCount() < minCount) {
                initialSet.remove(i);
            }
        }
    }
}