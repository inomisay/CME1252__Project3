
public class ChainTable {
	
	// Attributes
	static MultiLinkedList chaintable = new MultiLinkedList();	
	static int a = 0;
	static int b = 50;

	public static void construtTable(SingleLinkedList chain) {
	  
	    int chainSize = chain.size();
	    
	    chaintable.addLayer(a);
	    chaintable.addItem(a, chain.getDataFromIndex(0));
	    
	    for (int i = 1; i < chainSize; i++) {
	        chaintable.addItem(a, "+");
	        chaintable.addItem(a, chain.getDataFromIndex(i));
	    }
	    
	    chaintable.addLayer(b);
	    chaintable.addItem(b, "+");
	    
	    chaintable.display();
	    
	    a++;
	    b++;
	}

}
