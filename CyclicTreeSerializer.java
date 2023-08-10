

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

interface  TreeSerializer
{
	String serialize(Node root);
	Node deserialize(String str);
}

class Node 
{
	Node left;
	Node right;
	int num;
	public Node()
	{
		
	}
	public Node(int num)
	{
		this.num = num;
	}
}

public class CyclicTreeSerializer implements TreeSerializer 
{
    private static final String SEPARATOR = ",";
    private static final String NULL_MARKER = "null";
    
    
    
    @Override
    public String serialize(Node root) 
    {
        if (root == null) return NULL_MARKER;
        StringBuilder sb = new StringBuilder();
        serialize(root, sb, new HashMap<>());
        return sb.toString();
    }

    private void serialize(Node node, StringBuilder sb, Map<Node, Boolean> visited) 
    {
        if (node == null) 
        {
            sb.append(NULL_MARKER).append(SEPARATOR);
            return;
        }
        if (visited.getOrDefault(node, false)) 
        {
            throw new RuntimeException("Cyclic tree detected!");
        }
        visited.put(node, true);
        sb.append(node.num).append(SEPARATOR);
        serialize(node.left, sb, visited);
        serialize(node.right, sb, visited);
        visited.put(node, false); // Reset for other paths
    }

    // For deserialization, we don't need to change the method because cycles are detected during serialization.
    @Override
    public Node deserialize(String str) 
    {
        if (str.equals(NULL_MARKER)) return null;
        Queue<String> nodes = new LinkedList<>();
        nodes.addAll(Arrays.asList(str.split(SEPARATOR)));
        return deserialize(nodes);
    }

    private Node deserialize(Queue<String> nodes) 
    {
        String val = nodes.poll();
        if (val.equals(NULL_MARKER)) return null;
        Node node = new Node(Integer.parseInt(val));
        node.left = deserialize(nodes);
        node.right = deserialize(nodes);
        return node;
    }
    
    public static void main(String args[])
	{
		Node root = new Node();
		root.num = 1;
		root.left = new Node();
		root.left.num = 2;
		root.right = new Node();
		root.right.num = 1;
		root.right.right = new Node();
		root.right.right.num = 28;
		root.left.left = new Node();
		root.left.left.num = 7;
		root.left.right = new Node();
		root.left.right.num = 5;
		root.left.right.right = root; //Condition for Cyclic Tree Detection. If not commented would through exception with message
		//"Cyclic Tree Detected", if commented then would run as usual with perfect Serialization and Deserialization. 
		root.left.left.left = new Node();
		root.left.left.left.num = 4;	
		TreeSerializer serializer = new CyclicTreeSerializer();
	    String serialized = serializer.serialize(root);
	    System.out.println("Serialized: " + serialized);

	    Node deserializedRoot = serializer.deserialize(serialized);
	    System.out.println("Deserialized: " + serializer.serialize(deserializedRoot)); // should match the initial serialized string
	}
}

// Test can remain the same as above, but you can create a cyclic tree to test the exception.

