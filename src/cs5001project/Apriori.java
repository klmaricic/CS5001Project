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
    
    
    public void createInitialSet() {
        //Add value so initialSet isn't empty
        initialSet.add(new Entry(data.instance(0).stringValue(0), 0));
        
        //Loops through the dataset and creates an array containing every value and counts how often that value appears
        for(int row = 0; row < numInstances; row++) {
            for(int col = 0; col < numAttributes; col++) {
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
                dataSet.add(new ArrayList<Entry>());
                dataSet.get(dataSet.size()-1).add(new Entry(initialSet.get(i).getValue(), initialSet.get(i).getAttNum()));
            }     
        }
        
        for(int i = 0; i <initialSet.size(); i++) {
            System.out.println(initialSet.get(i).getAttNum()+", "+initialSet.get(i).getValue()+", "+initialSet.get(i).getCount());
        }
        System.out.println();
        for(int i = 0; i < dataSet.size(); i++) {
            for(int j = 0; j < dataSet.get(i).size(); j++)
                System.out.println(dataSet.get(i).get(j).getAttNum()+", "+initialSet.get(i).getValue()+", "+initialSet.get(i).getCount());
        }
            
    }
    /*
    public void recursiveSetBuild() {
        for(int row = 0; row < dataSet.size(); row++) {
            for(int j = 0; j < initialSet.size(); j++) {
                for(int col = 0; col < dataSet.get(row).size(); col++) {
                    if(dataSet.get(row).get(col).getAttNum() == initialSet.get(j).getAttNum()); {
                        if(dataSet.get(row).get(col).getValue().equals(initialSet.get(j).getValue()))
                            
                    }
                }
            }
        }
    }*/
}