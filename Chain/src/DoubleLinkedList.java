
public class DoubleLinkedList {
	
	// Attributes
	private Node head;
	private Node tail;
	
	public DoubleLinkedList() {
		head = null;
		tail = null;
	}
	
	public Node getHead() { return head; }
	public void setHead(Node node) { head = node; }
	
	public Node getTail() { return tail; }
    public void setTail(Node node) { tail = node; }
    
	public int size() {
		int count = 0;
		if(head == null) {
			//System.out.println("Linked List is empty");
		}
		else {
			Node temp = head;
			while(temp != null) {
				count++;
				temp = temp.getNext();
			}
		}
		return count; 
	}
	
	 // Get the element at the specified index
    public Object get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node current = getNodeAtIndex(index);
        return current.getData();
    }

    // Set the element at the specified index
    public void set(int index, Object element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node current = getNodeAtIndex(index);
        current.setData(element);
    }
    
    private Node getNodeAtIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node current = head;
        int counter = 0;

        while (counter < index) {
            current = current.getNext();
            counter++;
        }

        return current;
    }

	public void add(Object dataToAdd) {
		
		// add a new Node to the end
		if(head == null && tail == null) {
			Node newNode = new Node(dataToAdd);
			head = newNode; /* pointing the first Node */
			tail = newNode; /* pointing the last Node */
		} else {
			Node newNode = new Node(dataToAdd);
			newNode.setPrev(tail);
			tail.setNext(newNode);
			tail = newNode;
		}
	}
	
	public void add(String dataToAdd) {
		
		// add a new Node to the front
		if(head == null && tail == null) {
			Node newNode = new Node(dataToAdd);
			head = newNode; /* pointing the first Node */
			tail = newNode; /* pointing the last Node */
		} else {
			Node newNode = new Node(dataToAdd);
			if(dataToAdd.compareTo((String) head.getData()) < 0) {
				newNode.setNext(head);
				head.setPrev(newNode);
				head = newNode;
			} else {
				Node temp = head;
				while(temp.getNext() != null && dataToAdd.compareTo((String) temp.getNext().getData()) > 0) {
					temp = temp.getNext();
				}
				newNode.setPrev(temp);
				newNode.setNext(temp.getNext());
				if(temp.getNext() != null) // adding between Nodes
					temp.getNext().setPrev(newNode);
				else // adding to the end
					tail = newNode;
				
				temp.setNext(newNode);
			}
		}
	}
	
	public void delete(Object dataToDelete) {
	    if(head == null) {
	        //System.out.println("Linked List is empty");
	    } else {
	        Node temp = head;
	        while(temp != null) {
	            if(temp.getData().equals(dataToDelete)) {
	                if(temp == head) {
	                    head = temp.getNext();
	                    if(head != null) {
	                        head.setPrev(null);
	                    }
	                    if(temp == tail) {
	                        tail = null;
	                    }
	                } else if(temp == tail) {
	                    tail = temp.getPrev();
	                    tail.setNext(null);
	                } else {
	                    temp.getPrev().setNext(temp.getNext());
	                    temp.getNext().setPrev(temp.getPrev());
	                }
	                break;
	            }
	            temp = temp.getNext();
	        }
	        if(temp == null) {
	            System.out.println("Item not found in the Linked List");
	        }
	    }
	}

	public Node search(Object searchData) {
	    Node temp = head;
	    while(temp != null) {
	        if(temp.getData().equals(searchData)) {
	            return temp;
	        }
	        temp = temp.getNext();
	    }
	    System.out.println("Item not found in the Linked List");
	    return null;
	}

	public Object getDataFromIndex(int index) {
		int counter = 0;
		Node temp = head;
		
		if(head == null) {
			System.out.println("Linked List is empty.");
		} else {
			while(counter != index) {
				temp = temp.getNext();
				counter++;
			}
		}
		return temp.getData();
	}
	
	// (which prints all items in the list from head to tail)
	public void display1() {
		if(head == null) {
			//System.out.println("Linked List is empty");
		} else {
			Node temp = head;
			while(temp != null) {
				System.out.println(temp.getData() + " ");
				temp = temp.getNext();
			}
			System.out.println();
		}
	}
	
	// (which prints all items in the list from tail to head)
	public void display2() {
		if(head == null) {
			//System.out.println("Linked List is empty");
		} else {
			Node temp = tail;
			while(temp != null) {
				System.out.println(temp.getData() + " ");
				temp = temp.getPrev();
			}
			System.out.println();
		}
	}
	
}
