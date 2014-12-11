package cs5001project;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Kelsey Maricic
 */
public class Tree {
    private Node root;
    private int maxLevels;
    ArrayList<ArrayList<Entry>> dataSet = new ArrayList<>();
    
    public Tree() {
        root = new Node(new Entry("Root", -1));
    }
    
    public void buildTree(ArrayList<ArrayList<Entry>> arrList, int max) {
        maxLevels = max;
        dataSet = arrList;
        for(int i = 0; i < dataSet.size(); i++) {
            root.addChild(new Node(dataSet.get(i).get(0)));
        }
        for(int i = 0; i < root.children.size(); i++) {
            recursiveBuild(0, root.children.get(i));
        }
    }
    
    public void recursiveBuild(int level, Node currentEntry) {
        if(level >= maxLevels)
            return;
        
        for(int j = 0; j < dataSet.size(); j++) {
            //if the value in the dataset matches the corresponding node entry, add the next value in that row to the tree
            if((dataSet.get(j).get(level).getValue()).equals(currentEntry.getValue())) {
                currentEntry.addChild(new Node(dataSet.get(j).get(level+1)));
            }
        }
        
        for(int i = 0; i < currentEntry.children.size(); i++)
            recursiveBuild(level+1, currentEntry.children.get(i));
    }
    
    public class Node {
	private ArrayList<Node> children = null;
        private String value;
        private int count, attNum;

	public Node(Entry entry) {
            this.children = new ArrayList<>();
            this.value = entry.getValue();
            this.count = 1;
            this.attNum = entry.getAttNum();
        }


        public void addChild(Node child)
        {
            if(!children.contains(child))
                children.add(child);
            else {
                int index = children.indexOf(child);
                children.get(index).count++;
            }  
        }
        
        public String getValue() {
            return value;
        }
}
