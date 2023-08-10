

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class HandlingCyclicTreeSerializer implements TreeSerializer {

    private static final String SEP = ",";

    @Override
    public String serialize(Node root) {
        if (root == null) return "";

        Map<Node, String> nodeToIdMap = new HashMap<>();
        List<String> serializedData = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        int idCounter = 0;

        queue.offer(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (!nodeToIdMap.containsKey(current)) {
                String currentId = String.valueOf(idCounter++);
                nodeToIdMap.put(current, currentId);
                String leftId = current.left != null ? String.valueOf(idCounter) : "null";
                String rightId = current.right != null ? String.valueOf(idCounter + 1) : "null";
                serializedData.add(currentId + ":" + current.num + ":" + leftId + ":" + rightId);
                
                if (current.left != null) queue.offer(current.left);
                if (current.right != null) queue.offer(current.right);
            }
        }

        return String.join(SEP, serializedData);
    }

    @Override
    public Node deserialize(String data) {
        if (data.isEmpty()) return null;

        String[] parts = data.split(SEP);
        Map<String, Node> idToNodeMap = new HashMap<>();

        for (String part : parts) {
            String[] nodeData = part.split(":");
            String id = nodeData[0];
            int num = Integer.parseInt(nodeData[1]);
            Node node = new Node();
            node.num = num;
            idToNodeMap.put(id, node);
        }

        for (String part : parts) {
            String[] nodeData = part.split(":");
            Node current = idToNodeMap.get(nodeData[0]);
            if (!"null".equals(nodeData[2])) current.left = idToNodeMap.get(nodeData[2]);
            if (!"null".equals(nodeData[3])) current.right = idToNodeMap.get(nodeData[3]);
        }

        return idToNodeMap.get("0");
    }

    public static void main(String[] args) {
        Node node1 = new Node();
        node1.num = 1;
        Node node2 = new Node();
        node2.num = 2;
        Node node3 = new Node();
        node3.num = 3;

        node1.left = node2;
        node1.right = node3;
        node2.left = node3;  // creating cycle

        HandlingCyclicTreeSerializer serializer = new HandlingCyclicTreeSerializer();
        String serialized = serializer.serialize(node1);
        System.out.println("Serialized: " + serialized);

        Node deserializedRoot = serializer.deserialize(serialized);
        String reserialized = serializer.serialize(deserializedRoot);
        System.out.println("Reserialized: " + reserialized);

        System.out.println("Is same output? " + serialized.equals(reserialized));
    }
}

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