
public class MultiLinkedList {

	// Attributes
	private Node head;
	
	public void addLayer(Object dataToAdd) {
		Node temp;
		if (head == null) {
			temp = new Node(dataToAdd); 
			head = temp;
		} else {		     
			temp = head;
			while (temp.getDown() != null)
				temp = temp.getDown();
			Node newnode = new Node(dataToAdd);
			temp.setDown(newnode);
		}
	}
	
	public void addItem(Object Category, Object Item) {
		if (head == null)    
			System.out.println("Add a Category before Item");
		else {
			Node temp = head;
			while (temp != null) {	    	 
				if (Category.equals(temp.getLayer())) {
					Node temp2 = temp.getRight(); 
					if (temp2 == null) {
						temp2 = new Node(Item); 
						temp.setRight(temp2);
					} else {				 
						while (temp2.getNext() != null)
							temp2 = temp2.getNext();
						Node newnode = new Node(Item);
						temp2.setNext(newnode);
					}			          
				}
				temp = temp.getDown();
			}
		}
	}
	
	public void display() {	
		int i = 6;
		if (head == null) {   
			// System.out.println("linked list is empty");
		} else {
			Node temp = head;
			while (temp != null) {	
				Chain.cn.getTextWindow().setCursorPosition(40, i);
				Node temp2 = temp.getRight();
				while (temp2 != null) {
					System.out.print(temp2.getData());
					temp2 = temp2.getNext();
				}
				temp = temp.getDown();
				i++;
			}
		}
	}
	
}
