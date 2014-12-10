/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs5001project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Kelsey
 */
public class Apriori {
    int numAttributes, numInstances;
    String file, line = "";
    BufferedReader br = null;
    ArffLoader.ArffReader arff;
    Instances data;
    
    public Apriori(String filePath){
        file = filePath;
        
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
}