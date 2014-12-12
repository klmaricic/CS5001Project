package cs5001project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.SparseInstance;

/**
 * data.instance(index) returns the instance
 */





/**
 *
 * @author Kelsey Maricic
 */
public class CS5001Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String file, numRulesStr, filePath, tempFile;
        int minCoverage, maxSize, numRules;
        double minAccuracy;
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        ArffReader arff;
        Instances data;
	File folder = new File(".");
	File[] filesArr = folder.listFiles();
        
        System.out.println("Hello! Which of the following files would you like to use? (Do not include the extension): ");
		
		for (int i = 0; i < filesArr.length; i++) {
			if (filesArr[i].isFile()) {
				tempFile = filesArr[i].getName();
				
				if (tempFile.endsWith(".arff"))
					System.out.println(tempFile);
			}
		}
        file = scanner.next();
        filePath = System.getProperty("user.dir")+"/"+file+".arff";
        
        //Will continue to ask user for a file name until a valid one is given
        while(!(new File(filePath).isFile())){
            System.out.println("That file does not exist. Please try again.");
            System.out.println("What is the name of the file that you wish to use: ");
            file = scanner.next();
            filePath = System.getProperty("user.dir")+"/"+file+".arff";
        }
        
        //reader = new BufferedReader(new FileReader(filePath));
        //arff = new ArffReader(reader);
        //data = arff.getData();
        //data.setClassIndex(data.numAttributes() - 1);
        
        //SparseInstance si = new SparseInstance(data.instance(1));
        
        
        //System.out.println(si.toString(1));
        //data.sort(0);
        
        //System.out.println((data.instance(1).stringValue(3)));
        
        
        System.out.println("\nMinimum coverage requirements (how many times that value should appear at minimum): ");
        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Must enter an int-type number.\n");
            System.out.println("Minimum coverage requirements (how many times that value should appear at minimum): ");
            scanner.next();          
        }
        minCoverage = scanner.nextInt();
        
        
        
        System.out.println("\nMaximum size of item sets to consider: ");
        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Please enter an int-type number.\n");
            System.out.println("\nMaximum size of item sets to consider: ");
            scanner.next();
        }
        maxSize = scanner.nextInt();
        
        //Apriori apriori = new Apriori(filePath, minCoverage, maxSize);
        //apriori.createSet();
        
        FPGrowth fp = new FPGrowth(filePath, minCoverage);
        fp.createFrequentSet();
        
        
        /*
        System.out.println("\nMinimum accuracy requirements: ");
        while(!scanner.hasNextDouble()){
            System.out.println("Invalid input. Must enter a double-type number. So for 60%, type 0.6.\n");
            System.out.println("\nMinimum accuracy requirements: ");
            scanner.next();        
        }
        minAccuracy = scanner.nextDouble();
        
        do{
            System.out.println("\nNumber of best rule to report (enter * if you would like all rules listed): ");
            numRulesStr = scanner.next();
        }while(!numRulesStr.equals("*") && (!isInteger(numRulesStr) || (Integer.parseInt(numRulesStr) < 0)));
        
        if(numRulesStr.equals("*")){
            numRules = -1;
        }
        else{
            numRules = Integer.parseInt(numRulesStr);
        }
        
        
        
        
        
    }
    
    private static boolean isInteger(String s){
         try{ 
            Integer.parseInt(s); 
        } 
        catch(NumberFormatException e){ 
            return false; 
        }
    
        return true;
    }
    */
    }
}
