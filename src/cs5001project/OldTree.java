package cs5001project;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Kelsey Maricic
 */
public class OldTree {
    private Node root;
    
    public OldTree(Instances data) {
        int numInstances = data.numInstances();      
        Instance rootInstance = data.instance(numInstances-1);
        root = new Node(rootInstance.toString(0));
        
	for(int i = 1; i < rootInstance.numAttributes(); i++) {
            root.addChild(new Node(rootInstance.toString(i)));
        }
        
        data.delete(numInstances-1);
        
        buildTree(data);
    }
    
    private void buildTree(Instances data) {
        
    }
    
    public class Node {
	private List<Node> children = null;
        private String value;
        private int count;

	public Node(String value) {
            this.children = new ArrayList<>();
            this.value = value;
            this.count = 1;
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
    }
}
