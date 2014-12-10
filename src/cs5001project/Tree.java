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
    private int maxEntries;
    ArrayList<ArrayList<Entry>> dataSet = new ArrayList<>();
    
    public Tree() {
        root = new Node(new Entry("Root", -1));
    }
    
    public void buildTree(ArrayList<ArrayList<Entry>> arrList, int maxE) {
        maxEntries = maxE;
        dataSet = arrList;
        for(int i = 0; i < dataSet.size(); i++) {
            Node node = new Node(dataSet.get(i).get(0));
            root.addChild(new Node(dataSet.get(i).get(0)));
        }
    }
    
    public class Node {
	private List<Node> children = null;
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
    }
}
