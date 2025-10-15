import enigma.core.Enigma;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Chain {

	public static enigma.console.Console cn = Enigma.getConsole("Chain");
	public enigma.console.TextWindow ct = cn.getTextWindow();
   
	SingleLinkedList chain = new SingleLinkedList();
	DoubleLinkedList names = new DoubleLinkedList();
	DoubleLinkedList scores = new DoubleLinkedList();
	
	private int THREADTIME = 50;
	private int SEED = 34;
	private int score = 0;
	   
	private String playerName = "";
	
   public TextMouseListener tmlis; 
   public KeyListener klis; 

   Random rnd = new Random(SEED);
   
   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          // mouse pressed?
   public int mousex, mousey;   // mouse text coords.
   public int keypr;   // key pressed?
   public int rkey;    // key   (for press/release)
   // ----------------------------------------------------
   
   public void clear()
   {
	   for(int j = 0;j<30;j++) for(int i = 0;i<70;i++) ct.output(i,j, ' ');
   }
   
   public boolean elementFinder(int[][] arr,int inputX, int inputY , int size)
   {   
	   for(int i = 0; i<size;i++)
	   {
		   if(arr[i][0] == inputX && arr[i][1] == inputY)
		   {
			   return true;
		   }
	   }
	   
	   return false;
   }
  
   public void loadScoresFromFile(String filename) {
	   // Reading the unsorted file using the BufferedReader class
	   BufferedReader br = null;
	   try {
	        br = new BufferedReader(new FileReader(filename));
	        String line = br.readLine().trim();
	        while (line != null) {
	            // Splitting each line into name and score
	            String[] parts = line.split(" ");
	            if (parts.length != 2) {
	                System.out.println("Invalid file format!");
	                return;
	            }
	            names.add(parts[0]); // names
	            scores.add(Integer.parseInt(parts[1])); // scores
	            line = br.readLine();
	        }
	    } catch (IOException e) {
	        System.out.println("Error reading file: " + e.getMessage());
	        return;
	    } catch (NumberFormatException e) {
	        System.out.println("Invalid file format: " + e.getMessage());
	        return;
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                System.out.println("We encountered an error: " + e.getMessage());
	            }
	        }
	    }
	}
   
   public void saveScoresToFile(String filename) {
	    BufferedWriter bw = null;
	    try {
	        bw = new BufferedWriter(new FileWriter(filename));
	        Node namesNode = names.getHead();
	        Node scoresNode = scores.getHead();
	        while (namesNode != null && scoresNode != null) {
	            String playerName = (String) namesNode.getData();
	            int score = (int) scoresNode.getData();
	            namesNode = namesNode.getNext();
	            scoresNode = scoresNode.getNext();
	            String entry = playerName + " " + score;
	            bw.write(entry);
	            bw.newLine();
	        }
	        cn.getTextWindow().setCursorPosition(3, 25);
	        System.out.println("Data written to file successfully.");
	    } catch (IOException e) {
	        System.out.println("Error writing to file: " + e.getMessage());
	    } finally {
	        if (bw != null) {
	            try {
	                bw.close();
	            } catch (IOException e) {
	                System.out.println("We encountered an error: " + e.getMessage());
	            }
	        }
	    }
	}

   public void highscoreSort(DoubleLinkedList names, DoubleLinkedList scores) {
	    int size = scores.size();

	    // Create a 2D array to store the names and scores together
	    Object[][] data = new Object[size][2];

	    // Populate the 2D array with names and scores
	    for (int i = 0; i < size; i++) {
	        data[i][0] = names.get(i);
	        data[i][1] = scores.get(i);
	    }

	    // Sort the 2D array based on scores in descending order
	    for (int i = 0; i < size - 1; i++) {
	        for (int j = 0; j < size - i - 1; j++) {
	            if ((int) data[j][1] < (int) data[j + 1][1]) {
	                // Swap rows
	                Object[] temp = data[j];
	                data[j] = data[j + 1];
	                data[j + 1] = temp;
	            } else if ((int) data[j][1] == (int) data[j + 1][1]) {
	                // If scores are equal, compare names
	                String name1 = (String) data[j][0];
	                String name2 = (String) data[j + 1][0];
	                if (name1.compareTo(name2) > 0) {
	                    // Swap rows
	                    Object[] temp = data[j];
	                    data[j] = data[j + 1];
	                    data[j + 1] = temp;
	                }
	            }
	        }
	    }

	    // Update the original lists with the sorted names and scores
	    for (int i = 0; i < size; i++) {
	        names.set(i, data[i][0]);
	        scores.set(i, data[i][1]);
	    }
	}


   public String getPlayerName() { return playerName; }
   
   public int getScore() { return score;}
   
   public Chain() throws Exception {   // --- Constructor
      
	   char[][] gameMap = new char[20][32];
	   
	   int[][] plusPositions = new int[999][2];
	   int[][] numberPositions = new int[999][2];
	   
	   int plusCounter = 0;
	   int numberCounter = 0;
	   int round = 1;
	   
	   boolean gameBegin = true;
	   boolean finish = false;
	   boolean menu = true;

	   Cursor cursor = new Cursor(10,10);

	  // Map Declaring
	   
	   for(int j = 0; j<19;j++)
	   {
		   for(int i = 0; i<31;i++)
		   {
			   if(i%2==0 && j%2==0) {gameMap[j][i] = Character.forDigit(rnd.nextInt(4)+1, 10);}
			   else
			   {
				   gameMap[j][i] = ' ';
			   }
		   }
	   }
    
      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      
      cn.getTextWindow().addKeyListener(klis);
      // ----------------------------------------------------
      
      ct.setCursorPosition(10, 2);
		ct.output("			*Welcome to the Chain Game*");
		ct.setCursorPosition(10, 3);
		ct.output("			---------------------------");
		
	    Scanner inputMenu = new Scanner(System.in);
	    int choice = 0;
	    while (menu) {
	    	clear();
			ct.setCursorPosition(30, 5);
	        System.out.println("===========");
			ct.setCursorPosition(29, 6);
	        System.out.println("  GAME MENU");
			ct.setCursorPosition(30, 7);
	        System.out.println("===========");
			ct.setCursorPosition(30, 8);
	        System.out.println("1. Start game");
			ct.setCursorPosition(30, 9);
	        System.out.println("2. High score");
	        ct.setCursorPosition(30, 10);
	        System.out.println("3. How To Play");
			ct.setCursorPosition(30, 11);
	        System.out.println("4. Exit");
			ct.setCursorPosition(30, 12);
	        System.out.print("Enter your choice: ");
	        try {
	            choice = inputMenu.nextInt();
	            switch (choice) {
	                case 1:
	                	menu = false;
	                    break;
	            		// ----------------------------------------------------
	                case 2:
	                	clear();
	                	ct.setCursorPosition(30, 5);
	        	        System.out.println("============");
	            		ct.setCursorPosition(29, 6);
	                    System.out.println("  HIGH SCORE");
	            		ct.setCursorPosition(30, 7);
	        	        System.out.println("============");
	            		ct.setCursorPosition(30, 8);
	            		loadScoresFromFile("highscore.txt");
	            		// Sorting the scores in descending order
	            	    highscoreSort(names, scores);
	            		int k = 0;
	            		Node namesNode = names.getHead();
	            		Node scoresNode = scores.getHead();
	            		while (namesNode != null && scoresNode != null) {
	            		    String name = (String) namesNode.getData();
	            		    namesNode = namesNode.getNext();
	            		    int score = (int) scoresNode.getData();
	            		    scoresNode = scoresNode.getNext();

	            		    // Print the name and score
	            		    ct.setCursorPosition(32, 8 + k);
	            		    cn.getTextWindow().output(String.format("%s: %d", name, score));
	            		    k++;
	            		}     	

	        			ct.setCursorPosition(0, 0);
	        			inputMenu.next();
	                    break;
	            		// ----------------------------------------------------
	                case 3:
	                	clear();
	                	ct.setCursorPosition(30, 5);
	        	        System.out.println("===========");
	            		ct.setCursorPosition(30, 6);
	                    System.out.println("HOW TO PLAY");
	            		ct.setCursorPosition(30, 7);
	        	        System.out.println("===========");
	            		ct.setCursorPosition(30, 8);
	            		System.out.println("First Plus");
	            		ct.setCursorPosition(15, 9);
	            		System.out.println("You can put the first '+' anywhere you want.");
	            		ct.setCursorPosition(32, 11);
	            		System.out.println("Others");
	            		ct.setCursorPosition(10, 12);
	            		System.out.println("Other pluses should be next to the last chain element.");
	            		ct.setCursorPosition(9, 13);
	            		System.out.println("Both sides of the plus you put in cannot be in the chain.");	            		
	            		inputMenu.next();
	            		break;
	                case 4:
	                	System.exit(0);
	                    break;
	            		// ----------------------------------------------------
	                default:
	            		ct.setCursorPosition(30, 12);
	                    System.out.println("Invalid choice. Please try again.");
	                    continue; // loop back to show the menu again
	            }
	        } catch (InputMismatchException e) {
      		ct.setCursorPosition(30, 13);
	            System.out.println("									 ");
	            System.out.println("Invalid input. Please enter a number.");
	            inputMenu.nextLine();
	        }
	    } 
      
	    clear();
	    
      while(!finish) {
    	  if (gameBegin) {
  	    	Scanner input = new Scanner(System.in);
  	    	    
  	    	// Ask for player's name
  	        ct.setCursorPosition(28, 5);
  	    	System.out.print("Enter your name: ");
  	        ct.setCursorPosition(45, 5);
  	    	playerName = cn.readLine();
  	        ct.setCursorPosition(45, 5);
  	        ct.output("                       									");
  	        ct.setCursorPosition(28, 4);
  	        System.out.println("Welcome, " + playerName + "!");
  	        ct.setCursorPosition(25, 5);
  	        ct.output("PRESS ENTER TO START");        
  	        System.in.read();
  	        ct.setCursorPosition(25, 5);
  	        ct.output("                    ");
  	        
  	        clear();
  	        
  	        gameBegin = false;
  	    }
         if(mousepr==1) {  // if mouse button pressed
            
         }
         if(keypr==1) {    // if keyboard button pressed
            if(rkey==KeyEvent.VK_LEFT)  
            	if(cursor.getX()>0) 
            	{
            		cn.getTextWindow().output(cursor.getX(), cursor.getY(), ' ');
            		cursor.setX(cursor.getX()-1);
            	}
            if(rkey==KeyEvent.VK_RIGHT) 
            	if(cursor.getX()<30) 
            	{
            		cn.getTextWindow().output(cursor.getX(), cursor.getY(), ' ');
            		cursor.setX(cursor.getX()+1);
            	}
            if(rkey==KeyEvent.VK_UP) 
            	if(cursor.getY()>0) 
            	{
            		cn.getTextWindow().output(cursor.getX(), cursor.getY(), ' ');
            		cursor.setY(cursor.getY()-1);
            	}
            if(rkey==KeyEvent.VK_DOWN) 
            	if(cursor.getY()<18) 
            	{
            		cn.getTextWindow().output(cursor.getX(), cursor.getY(), ' ');
            		cursor.setY(cursor.getY()+1);
            	}
            
            if(rkey==KeyEvent.VK_ENTER) 
            	{          	
            		boolean error = false;
            		if(numberCounter <4)
            		{
            			error = true;
            		}
            		for(int i = 0 ; i<chain.size()-1;i++)
            		{
            			if(Integer.parseInt(chain.getDataFromIndex(i).toString())+1 == Integer.parseInt(chain.getDataFromIndex(i+1).toString()) || Integer.parseInt(chain.getDataFromIndex(i).toString())-1 == Integer.parseInt(chain.getDataFromIndex(i+1).toString()))
            			{
            				
            			}
            			else {error = true;}
            		}
            		if(error)
            		{
            			cn.getTextWindow().setCursorPosition(40, 15);
            		    cn.getTextWindow().output(String.format("ERROR IN CHAIN" ));
            		    finish = true;
            		}
            		else 
            		{
            			ChainTable.construtTable(chain);
            			
            			score+=(chain.size()*chain.size());
            			round+=1;
            			
            			for(int i = 0;i<chain.size()*2;i++)
            		    {
            				cn.getTextWindow().setCursorPosition(53+i, 3);
            		        cn.getTextWindow().output(String.format(" "));
            		        cn.getTextWindow().setCursorPosition(53+i, 4);
            		        cn.getTextWindow().output(String.format(" "));
            		    }
            			
            			for(int i = 0;i<plusCounter;i++)
                		{
                			gameMap[plusPositions[i][1]][plusPositions[i][0]] = ' ';
                			plusPositions[i][0] = 0;
                			plusPositions[i][1] = 0;
                		}
            			
                		plusCounter = 0;
                		
                		for(int i = 0;i<numberCounter;i++)
                		{
                			gameMap[numberPositions[i][1]][numberPositions[i][0]] = '.';
                			numberPositions[i][0] = 0;
                			numberPositions[i][1] = 0;
                		}
                		
                		numberCounter = 0;
                		
                		chain = new SingleLinkedList();
            		}
            	}
            
            if(rkey==KeyEvent.VK_E) finish = true;
            
            if(rkey==KeyEvent.VK_SPACE) 
            {
            	boolean flag = false;
            	boolean cursorCorrect = false;
            	
            	if(cursor.getX()%2==1 && cursor.getY()%2==1);
            	else if(cursor.getX()%2==0 && cursor.getY()%2==0);
            	else if(gameMap[cursor.getY()][cursor.getX()] == '+') 
            	{
            		for(int i = 0;i<chain.size()*2;i++)
        		    {
            			cn.getTextWindow().setCursorPosition(53+i, 3);
        		        cn.getTextWindow().output(String.format(" "));
        		        cn.getTextWindow().setCursorPosition(53+i, 4);
        		        cn.getTextWindow().output(String.format(" "));
        		    }
            		
            		for(int i = 0;i<plusCounter;i++)
            		{
            			gameMap[plusPositions[i][1]][plusPositions[i][0]] = ' ';
            			plusPositions[i][0] = 0;
            			plusPositions[i][1] = 0;
            		}
            		plusCounter = 0;
            		
            		for(int i = 0;i<numberCounter;i++)
            		{
            			numberPositions[i][0] = 0;
            			numberPositions[i][1] = 0;
            		}
            		
            		numberCounter = 0;
            		
            		chain = new SingleLinkedList();
            	}
            	else 
            	{
            		if(plusCounter > 0 && cursor.getX()%2==1 && cursor.getY()%2==0 && ((elementFinder(numberPositions,cursor.getX()-1,cursor.getY(),numberCounter) && !elementFinder(numberPositions,cursor.getX()+1,cursor.getY(),numberCounter)) || (!elementFinder(numberPositions,cursor.getX()-1,cursor.getY(),numberCounter) && elementFinder(numberPositions,cursor.getX()+1,cursor.getY(),numberCounter)))) // horizontal cursorChecker
            		{
            			if(gameMap[cursor.getY()][cursor.getX()-1] != '.' && gameMap[cursor.getY()][cursor.getX()+1] != '.') 
            				{cursorCorrect = true;}
            		}
            		else if(plusCounter > 0 && cursor.getX()%2==0 && cursor.getY()%2==1 && ((elementFinder(numberPositions,cursor.getX(),cursor.getY()-1,numberCounter) && !elementFinder(numberPositions,cursor.getX(),cursor.getY()+1,numberCounter)) || (!elementFinder(numberPositions,cursor.getX(),cursor.getY()-1,numberCounter) && elementFinder(numberPositions,cursor.getX(),cursor.getY()+1,numberCounter)))) // vertical cursorChecker
            		{
            			if(gameMap[cursor.getY()-1][cursor.getX()] != '.' && gameMap[cursor.getY()+1][cursor.getX()] != '.') 
            				{cursorCorrect = true;}
            		}
            		
            		if(plusCounter == 0) // First +
            		{
            			boolean cursorCorrectForFirst= false;
            			
            			if(cursor.getX() == 0 && gameMap[cursor.getY()-1][cursor.getX()] != '.' && gameMap[cursor.getY()+1][cursor.getX()] != '.') {cursorCorrectForFirst = true;}
            			else if(cursor.getY() == 0 && gameMap[cursor.getY()][cursor.getX()-1] != '.' && gameMap[cursor.getY()][cursor.getX()+1] != '.') {cursorCorrectForFirst = true;}
            			else if(gameMap[cursor.getY()][cursor.getX()-1] != '.' && gameMap[cursor.getY()][cursor.getX()+1] != '.' && gameMap[cursor.getY()-1][cursor.getX()] != '.' && gameMap[cursor.getY()+1][cursor.getX()] != '.') 
        					{cursorCorrectForFirst = true;}
            			
            			if(cursorCorrectForFirst)
            			{
            				gameMap[cursor.getY()][cursor.getX()] = '+';
                			plusPositions[plusCounter][0] = cursor.getX();
                			plusPositions[plusCounter][1] = cursor.getY();
                			plusCounter++;
                			
                			// Check vertical or horizontal?
                			
                			if(cursor.getX()%2==1 && cursor.getY()%2==0) // horizontal
                			{
                				// First left
                    			
                    			numberPositions[numberCounter][0] = cursor.getX()-1;
                    			numberPositions[numberCounter][1] = cursor.getY();
                    			chain.add(gameMap[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                    			
                    			// Next right
                    			
                    			numberPositions[numberCounter][0] = cursor.getX()+1;
                    			numberPositions[numberCounter][1] = cursor.getY();
                    			chain.add(gameMap[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                			}
                			else if (cursor.getX()%2==0 && cursor.getY()%2==1) // vertical
                			{
                				// First up
                    			
                    			numberPositions[numberCounter][0] = cursor.getX();
                    			numberPositions[numberCounter][1] = cursor.getY()-1;
                    			chain.add(gameMap[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                    			
                    			// Next down
                    			
                    			numberPositions[numberCounter][0] = cursor.getX();
                    			numberPositions[numberCounter][1] = cursor.getY()+1;
                    			chain.add(gameMap[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                			}
            			}
            		}
            		
            		else if(cursorCorrect && plusPositions[plusCounter-1][0]%2 == 0 && plusPositions[plusCounter-1][1]%2 == 1) // last plus is vertical
            		{
            			if(plusPositions[plusCounter-1][0] == cursor.getX() && plusPositions[plusCounter-1][1]+2 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) // 1 x0 y+  down 
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		}
                		else if(plusPositions[plusCounter-1][0] == cursor.getX() && plusPositions[plusCounter-1][1]-2 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) // 2 x0 y- up 
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		}
                		
                		else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 3 x+ y+ right
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
                		else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 4 x- y+ left
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
                		else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 5 x+ y- right
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
                		else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 6 x- y- left 
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
            		}
            		else if(cursorCorrect && plusPositions[plusCounter-1][0]%2 == 1 && plusPositions[plusCounter-1][1]%2 == 0) // last plus is horizontal
            		{
            			if(plusPositions[plusCounter-1][0]+2 == cursor.getX() && plusPositions[plusCounter-1][1] == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 1 x+ y0  right
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
                		else if(plusPositions[plusCounter-1][0]-2 == cursor.getX() && plusPositions[plusCounter-1][1] == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) // 2 x- y0 left
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
                		
                		else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) // 3 x+ y+ down
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		}
                		else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) // 4 x- y+ down
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		}
                		else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) // 5 x+ y- up
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		}
                		else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) // 6 x- y- up
                		{
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		}
            		}
            		if (flag)
            		{
            			gameMap[cursor.getY()][cursor.getX()] = '+';
            			plusPositions[plusCounter][0] = cursor.getX();
            			plusPositions[plusCounter][1] = cursor.getY();
            			plusCounter++;
            			
            			chain.add(gameMap[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
            			numberCounter++;
            		}
            	}
            }
            
            keypr=0;    // last action  
         }
         
         // Map Displaying
         
       for(int j = 0; j<=18;j++) for(int i = 0; i<=30;i++) cn.getTextWindow().output(i, j,gameMap[j][i]);
       
       for(int j = 0;j<=18;j++) cn.getTextWindow().output(31, j, ' ');
       
       for(int i = 0;i<=31;i++) cn.getTextWindow().output(i, 20, ' ');
       
       cn.getTextWindow().setCursorPosition(39, 0);
       cn.getTextWindow().output(String.format("Board Seed:   %d  ", SEED));
       cn.getTextWindow().setCursorPosition(39, 1);
       cn.getTextWindow().output(String.format("Round:        %d  ", round));
       cn.getTextWindow().setCursorPosition(39, 2);
       cn.getTextWindow().output(String.format("Score:        %d  ", score));
       cn.getTextWindow().setCursorPosition(39, 3);
       cn.getTextWindow().output(String.format("Live Chain:   "));
       cn.getTextWindow().setCursorPosition(39, 4);
       cn.getTextWindow().output(String.format("Chain Length: %d  ", numberCounter));
       cn.getTextWindow().setCursorPosition(39, 5);
       cn.getTextWindow().output(String.format("----------------  "));
       cn.getTextWindow().setCursorPosition(3, 22);
       cn.getTextWindow().output(String.format("Player Name:  %s  " , playerName));
       
       for(int i = 0;i<chain.size();i++)
       {
    	   cn.getTextWindow().setCursorPosition(53+(i*2), 3);
    	   if(i == chain.size()-1) cn.getTextWindow().output(String.format("%c   ", chain.getDataFromIndex(i)));
    	   else cn.getTextWindow().output(String.format("%c+", chain.getDataFromIndex(i))); 
       }
       
       // Cursor Display
       cn.getTextWindow().output(cursor.getX(), cursor.getY(), 'O');
       
         Thread.sleep(THREADTIME);
      }
      // After the game finishes
      // Add the new score to the list
	  String newWinner = playerName;
	  names.add(newWinner);
      scores.add(score);
      // Sorting the scores in descending order
   	  highscoreSort(names, scores);
      loadScoresFromFile("highscore.txt");
	  saveScoresToFile("highscore.txt");
      Thread.sleep(2000);
	  clear();
  	  ct.setCursorPosition(30, 2);
      System.out.println("============");
		ct.setCursorPosition(29, 3);
      System.out.println("  HIGH SCORE");
		ct.setCursorPosition(30, 4);
      System.out.println("============");
		ct.setCursorPosition(30, 5);
		// Sorting the scores in descending order
	    highscoreSort(names, scores);
		int k = 0;
		Node namesNode = names.getHead();
		Node scoresNode = scores.getHead();
		while (namesNode != null ) {
		    String name = (String) namesNode.getData();
		    namesNode = namesNode.getNext();
		    int score = (int) scoresNode.getData();
		    scoresNode = scoresNode.getNext();

		    // Print the name and score
		    ct.setCursorPosition(32, 6 + k);
		    cn.getTextWindow().output(String.format("%s: %d", name, score));
		    k++;
		}     	

		ct.setCursorPosition(0, 0);

   }
}
