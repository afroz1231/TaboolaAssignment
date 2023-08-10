

import java.util.LinkedList;
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
public class BinaryTreeSerializer implements TreeSerializer
{
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
		root.left.left.left = new Node();
		root.left.left.left.num = 4;	
		TreeSerializer serializer = new BinaryTreeSerializer();
	    String serialized = serializer.serialize(root);
	    System.out.println("Serialized: " + serialized);

	    Node deserializedRoot = serializer.deserialize(serialized);
	    System.out.println("Deserialized: " + serializer.serialize(deserializedRoot)); // should match the initial serialized string
	}
	
	public String serialize(Node root)
	{
		if(root==null)
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		
		while(!queue.isEmpty())
		{
			Node current = queue.poll();
			if(current==null)
			{
				sb.append("null,");
			}
			else
			{
				sb.append(current.num + ",");
				queue.add(current.left);
				queue.add(current.right);
			}
		}
		System.out.println("The String is " + sb.toString());
		return sb.toString();
	}
	
	public Node deserialize(String str)
	{
		if(str.isEmpty())
		{
			return null;
		}
		String[] nodes = str.split(",");
		Node root = new Node(Integer.parseInt(nodes[0]));
		
		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		
		for(int i=1;i<nodes.length;i++)
		{
			Node parent = queue.poll();
			
			if(!nodes[i].equals("null"))
			{
				parent.left = new Node(Integer.parseInt(nodes[i]));
				queue.add(parent.left);
			}
			if(++i < nodes.length && !nodes[i].equals("null"))
			{
				parent.right = new Node(Integer.parseInt(nodes[i]));
				queue.add(parent.right);
			}
		}
		return root;
	}
}
