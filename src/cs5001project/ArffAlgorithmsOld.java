package cs5001project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Kelsey Maricic
 */
public class ArffAlgorithmsOld {
    String file, line = "";
    BufferedReader br = null;
    
    public ArffAlgorithmsOld(String filePath){
        file = filePath;
        
        try{
            br = new BufferedReader(new FileReader(file));
            
        } catch(FileNotFoundException e) {}
    }
    
    public void apriori(){
        try {
            while(!(line.toLowerCase().contains("@data")) && line != null) {
                line = br.readLine();
            }
            
            
        } catch(IOException e) {}
    }
}
