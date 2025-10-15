
public class Node {
	
	// Attributes
	private Object data;
	private Node link;
	
	private Node prev;
	private Node next;
	
	// Multiple Linked List
	private Object layer; // for every chain layer.
	private Node down;
	private Node right;
	
	// Constructor
	public Node(Object dataToAdd) {
		data = dataToAdd;
		link = null;
		
		prev = null;
		next = null;
		
		layer = dataToAdd;
		down = null;
		right = null;
	}
 
	// Getters & Setters
	public Object getData() { return data; }
	public void setData(Object data) { this.data = data; }
 
	public Node getLink() { return link; }
	public void setLink(Node link) { this.link = link; }  
	
	public Node getPrev() { return prev; }
	public void setPrev(Node prev) { this.prev = prev; }
	
	public Node getNext() { return next; }
	public void setNext(Node next) { this.next = next; }
	
	public Object getLayer() { return layer; }
	public void setLayer(Object layer) { this.layer = layer; }

	public Node getDown() { return down; }
	public void setDown(Node down) { this.down = down; }

	public Node getRight() { return right; }
	public void setRight(Node right) { this.right = right; }
	
}