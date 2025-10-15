import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {
	
	// Attributes
	public static enigma.console.Console Console = Enigma.getConsole("Chain", 100, 30, 25); //(x, y, font size)
	public enigma.console.TextWindow ct = Console.getTextWindow();
	
	public KeyListener klis; 
    // ------ Standard variables for keyboard ------
 	public int keypr;   // key pressed?
 	public int rkey;    // key   (for press/release)
 	public char keyChar; // character of the pressed key
 	
 	private Random rnd = new Random();
 	
	public static char[][] gameBoard = new char[20][32]; // Game Board 10*16 without spaces
	
	private Boolean gameBegin = true;
	private Boolean gameOver = false;
	private Boolean menu = true;
	
	private String playerName = "";
	private int seed;
	private int round = 1;
	private int score = 0;
	
	private Cursor cursor = new Cursor(10,10); // (10, 10) random position of the cursor on the game board
	
	private int[][] plusPositions = new int[999][2];
	private int plusCounter = 0;
	
	private int[][] numberPositions = new int[999][2];
	private int numberCounter = 0;
	
	SingleLinkedList chain = new SingleLinkedList();
	DoubleLinkedList names = new DoubleLinkedList();
	DoubleLinkedList scores = new DoubleLinkedList();

    // ----------------------------------------------------
	// Game Start
	public Game() throws Exception {   // --- Constructor
		
		// ------ Standard code for keyboard ------ Do not change
		klis = new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					rkey = e.getKeyCode();
					keyChar = e.getKeyChar();
				}
			}
			public void keyReleased(KeyEvent e) {}
	    };
	    ct.addKeyListener(klis);
	    // ----------------------------------------------------
	    initializeMap(); // Board Game
	  	showGameMenu(); // Menu
        clear();
	  	// game
	  	while(!gameOver) {
			
	  		boardGame(); // print the board game on the console
			movement(); // move the cursor | --- place the +

			Thread.sleep(250);
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
	 	Thread.sleep(10000);
	 	for(int j = 0; j < 30; j++) 
			for(int i = 0; i < 80; i++) 
				ct.output(i, j, ' ');
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
		    ct.setCursorPosition(31, 6 + k);
		    ct.output(String.format("%s: %d", name, score));
		    k++;
		}	  	
	}
	// ----------------------------------------------------
	public void showGameMenu() throws Exception {
		ct.setCursorPosition(10, 2);
		ct.output("			*Welcome to the Chain Game*");
		ct.setCursorPosition(10, 3);
		ct.output("			---------------------------");
		
	    Scanner inputMenu = new Scanner(System.in);
	    int choice = 0;
	    while (menu) {
	    	
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
	                	clear();
	              		startGame(); //Press Enter to start the game
	                    // Board Seed
	            		Random rnd = new Random(getBoardSeed(Console));
	            	    seed = Math.abs(rnd.nextInt());
	                    ct.setCursorPosition(45, 7);
	                    ct.output(String.format("Board Seed:   %d", seed));
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
	            		    ct.output(String.format("%s: %d", name, score));
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
	                    // ----------------------------------------------------
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
	        break;
	    } 
	}
	// ----------------------------------------------------
	public void initializeMap() {

		ct.setCursorPosition(7, 7);
		
		// fill the map
		/*
	    for (int i = 0; i < 31; i++) {
	        for (int j = 0; j < 21; j++) {
	            if (i % 2 == 0 && j % 2 == 0) {
	                int e = rnd.nextInt(4) + 1; // random number (1-4)
	                map[i][j] = (char) (e + 48); // fill the position with the number
	            } else {
	                map[i][j] = ' '; // set odd positions to spaces
	            }
	        }
	    }
	    */
		for(int j = 0; j < 19; j++) {
			for(int i = 0; i < 31; i++) {
				if(i % 2 == 0 && j % 2 == 0) { gameBoard[j][i] = Character.forDigit(rnd.nextInt(4) + 1, 10); }
				else { gameBoard[j][i] = ' '; }
			}
		}
	}
	// ----------------------------------------------------
	public void boardGame() {

		ct.setCursorPosition(7, 7);
		
	    // print the map
		/*
	    for (int i = 0; i < 31; i++) {
	        for (int j = 0; j < 21; j++) {
	            ct.setCursorPosition(i + 7, j + 7);
	            char c = map[i][j];
	            if (c >= '1' && c <= '4') { // 1, 2, 3, 4
	                ct.output(c, new TextAttributes(Color.PINK));
	            } else if (c == '+') { // player: +
	                ct.output(c, new TextAttributes(Color.RED));
	            } else {
	                ct.output(c, new TextAttributes(Color.BLACK));
	            }
	        }
	    }
	    */
        ct.setCursorPosition(7,7);
		for(int j = 0; j <= 18; j++) {
			for(int i = 0; i <= 30;i++) {
	            char c = gameBoard[j][i];
	            if (c == '+') { // player: +
	                ct.output(i + 7, j + 7, gameBoard[j][i], new TextAttributes(Color.RED));
	            }else { // 1, 2, 3, 4
	                ct.output(i + 7, j + 7, c, new TextAttributes(Color.PINK));
	            } 
			}
		}
		for(int j = 0; j <= 18; j++) {
			ct.output(31 + 7, j + 7, ' ');
		}
		for(int i = 0; i <= 31; i++) {
			ct.output(i + 7, 19 + 7, ' ');
		}
	       
	    // Status
		ct.setCursorPosition(45, 7);
	    ct.output(String.format("Board Seed:   %d  ", seed));
        ct.setCursorPosition(45, 8); // Round
        ct.output(String.format("Round:        %d", round));
        ct.setCursorPosition(45, 9); // Score
        ct.output(String.format("Score:        %d", score));
        ct.setCursorPosition(45, 10); // Live Chain
        ct.output(String.format("Live Chain:   "));
        ct.setCursorPosition(45, 11);
        ct.output(String.format("Chain Length: %d", numberCounter));
        ct.setCursorPosition(45, 12); // Table
        ct.output("--------------------------------");
        ct.setCursorPosition(45, 13);
        ct.output("Table:");
        
        for(int i = 0; i < chain.size(); i++) {
        	ct.setCursorPosition(45 + 14 + (i * 3), 10);
        	if(i == chain.size() - 1) { 
      		   ct.output(String.format("%c", chain.getDataFromIndex(i)));
      	   } else {
      		   ct.output(String.format("%c+", chain.getDataFromIndex(i)));
      	   }
        }
        // Cursor Display
        ct.output(cursor.getX(), cursor.getY(), '*');
	}
	// ----------------------------------------------------
	public void startGame() throws Exception {
	    //Press Enter to start the game
	    if (gameBegin) {
	    	Scanner input = new Scanner(System.in);
	    	    
	    	// Ask for player's name
	        ct.setCursorPosition(28, 5);
	    	System.out.print("Enter your name: ");
	        ct.setCursorPosition(45, 5);
	    	playerName = Console.readLine();
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
	}
	// ----------------------------------------------------
	public int getBoardSeed(Console console) {
	    int seedValue = 0;
        ct.setCursorPosition(22, 5);
	    System.out.println("Please Enter the seed value: ");
	    
		Scanner input = new Scanner(System.in);
		try {
	        ct.setCursorPosition(51, 5);
		    seedValue = Integer.parseInt(Console.readLine());
	        ct.setCursorPosition(51, 5);
	        System.out.println("									   ");
	    } catch (InputMismatchException e) {
	        // handle invalid input
	        ct.setCursorPosition(22, 5);
	        System.out.println("Invalid input. Please enter an integer.");
	        ct.setCursorPosition(22, 5);
	        System.out.println("									   ");
	       return getBoardSeed(console);
	    }
		
        ct.setCursorPosition(22, 5);
	    System.out.println("                             ");
	    return seedValue;
	}
    // ----------------------------------------------------
	public boolean elementFinder(int[][] arr, int inputX, int inputY, int size) {   
		for(int i = 0; i < size; i++) {
			if(arr[i][0] == inputX && arr[i][1] == inputY) { return true; }
		}
		return false;
	}
	// ----------------------------------------------------
	public void movement() {
		if (keypr == 1) { // if keyboard button pressed
			// Cursor keys: To move cursor on the board
			if(rkey == KeyEvent.VK_LEFT && cursor.getX() > 7) {
				ct.output(cursor.getX(), cursor.getY(), '*');
				cursor.setX(cursor.getX()-1);
			}
			if(rkey == KeyEvent.VK_RIGHT && cursor.getX() < 37) {
        		ct.output(cursor.getX(), cursor.getY(), '*');
    			cursor.setX(cursor.getX()+1);
			}
            if(rkey == KeyEvent.VK_UP && cursor.getY() > 7) {
        		ct.output(cursor.getX(), cursor.getY(), '*');
        		cursor.setY(cursor.getY()-1);
            }
            if(rkey == KeyEvent.VK_DOWN && cursor.getY() < 25) {
        		ct.output(cursor.getX(), cursor.getY(), '*');
        		cursor.setY(cursor.getY()+1);
            }
            if(rkey==KeyEvent.VK_ENTER) { // End of the round and calculate the chain          	
        		boolean error = false;
        		if(numberCounter <= 4) { error = true; }
        		for(int i = 0 ; i < chain.size()-1; i++) {
        			if(Integer.parseInt(chain.getDataFromIndex(i).toString())+1 == Integer.parseInt(chain.getDataFromIndex(i+1).toString()) || Integer.parseInt(chain.getDataFromIndex(i).toString())-1 == Integer.parseInt(chain.getDataFromIndex(i+1).toString())) {
        				
        			} else { error = true; }
        		}
        		if(error) {
        			// Display an error message to the player
	                ct.setCursorPosition(45, 24);
	                ct.output("ERROR IN CHAIN");
	                ct.setCursorPosition(45, 25);
	                ct.output("- Game Over -");
	                ct.setCursorPosition(45, 26);
	                ct.output("Press E to Exit.");
        		    gameOver = true;
        		} else {
        			ChainTable.construtTable(chain);
        			score += (chain.size() * chain.size());
        			round += 1;
        			
        			for(int i = 0; i < chain.size(); i++) {
        		    	ct.setCursorPosition(45+i, 14);
        		        ct.output(String.format(" "));
        		        ct.setCursorPosition(45+i, 15);
        		        ct.output(String.format(" "));
        		    }
        			
        			for(int i = 0; i < plusCounter; i++) {
            			gameBoard[plusPositions[i][1]][plusPositions[i][0]] = ' ';
            			plusPositions[i][0] = 0;
            			plusPositions[i][1] = 0;
            		}
            		plusCounter = 0;
            		
            		for(int i = 0;i < numberCounter; i++) {
            			gameBoard[numberPositions[i][1]][numberPositions[i][0]] = '.';
            			numberPositions[i][0] = 0;
            			numberPositions[i][1] = 0;
            		}
            		numberCounter = 0;
            		
            		chain = new SingleLinkedList();

        		}
        	}
            if(rkey == KeyEvent.VK_SPACE) { // start adding chain : Space: Insert/remove +
            	boolean flag = false;
            	boolean cursorCorrect = false;
            	
            	if(cursor.getX()%2 == 1 && cursor.getY()%2 == 1);
            	else if(cursor.getX()%2 == 0 && cursor.getY()%2 == 0);
            	else if(gameBoard[cursor.getY()][cursor.getX()] == '+') {
            		
            		for(int i = 0; i < chain.size()*2; i++) {
            			ct.setCursorPosition(45+i, 14);
        		        ct.output(String.format(" "));
        		        ct.setCursorPosition(45+i, 15);
        		        ct.output(String.format(" "));
        		    }
            		
            		for(int i = 0; i < plusCounter; i++) {
            			gameBoard[plusPositions[i][1]][plusPositions[i][0]] = ' ';
            			plusPositions[i][0] = 0;
            			plusPositions[i][1] = 0;
            		}
            		plusCounter = 0;
            		
            		for(int i = 0; i < numberCounter; i++) {
            			numberPositions[i][0] = 0;
            			numberPositions[i][1] = 0;
            		}
            		
            		numberCounter = 0;
            		
            		chain = new SingleLinkedList();
            	} else {
            		if(plusCounter > 0 && cursor.getX()%2 == 1 && cursor.getY()%2 == 0 && ((elementFinder(numberPositions, cursor.getX()-1, cursor.getY(), numberCounter) && !elementFinder(numberPositions, cursor.getX()+1, cursor.getY(), numberCounter)) || (!elementFinder(numberPositions, cursor.getX()-1, cursor.getY(), numberCounter) && elementFinder(numberPositions, cursor.getX()+1, cursor.getY(), numberCounter)))) { // horizontal cursorChecker
            			if(gameBoard[cursor.getY()][cursor.getX()-1] != '.' && gameBoard[cursor.getY()][cursor.getX()+1] != '.') {
            				cursorCorrect = true;
            			}
            		}
            		else if(plusCounter > 0 && cursor.getX()%2 == 0 && cursor.getY()%2 == 1 && ((elementFinder(numberPositions, cursor.getX(), cursor.getY()-1, numberCounter) && !elementFinder(numberPositions, cursor.getX(), cursor.getY()+1, numberCounter)) || (!elementFinder(numberPositions, cursor.getX(), cursor.getY()-1, numberCounter) && elementFinder(numberPositions, cursor.getX(), cursor.getY()+1, numberCounter)))) { // vertical cursorChecker 
            			if(gameBoard[cursor.getY()-1][cursor.getX()] != '.' && gameBoard[cursor.getY()+1][cursor.getX()] != '.') {
            				cursorCorrect = true;
            			}
            		}
            		
            		if(plusCounter == 0) { // First +
            			boolean cursorCorrectForFirst= false;
            			
            			if(cursor.getX() == 0 && gameBoard[cursor.getY()-1][cursor.getX()] != '.' && gameBoard[cursor.getY()+1][cursor.getX()] != '.') {cursorCorrectForFirst = true;}
            			else if(cursor.getY() == 0 && gameBoard[cursor.getY()][cursor.getX()-1] != '.' && gameBoard[cursor.getY()][cursor.getX()+1] != '.') {cursorCorrectForFirst = true;}
            			else if(gameBoard[cursor.getY()][cursor.getX()-1] != '.' && gameBoard[cursor.getY()][cursor.getX()+1] != '.' && gameBoard[cursor.getY()-1][cursor.getX()] != '.' && gameBoard[cursor.getY()+1][cursor.getX()] != '.')  {
            				cursorCorrectForFirst = true;
            			}
            			
            			if(cursorCorrectForFirst) {
            				gameBoard[cursor.getY()][cursor.getX()] = '+';
                			plusPositions[plusCounter][0] = cursor.getX();
                			plusPositions[plusCounter][1] = cursor.getY();
                			plusCounter++;
                			
                			// Check vertical or horizontal?
                			
                			if(cursor.getX()%2 == 1 && cursor.getY()%2 == 0) { // horizontal
                				
                				// First left
                    			
                    			numberPositions[numberCounter][0] = cursor.getX()-1;
                    			numberPositions[numberCounter][1] = cursor.getY();
                    			chain.add(gameBoard[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                    			
                    			// Next right
                    			
                    			numberPositions[numberCounter][0] = cursor.getX()+1;
                    			numberPositions[numberCounter][1] = cursor.getY();
                    			chain.add(gameBoard[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                			} else if (cursor.getX()%2 == 0 && cursor.getY()%2 == 1) { // vertical
                				// First up
                    			
                    			numberPositions[numberCounter][0] = cursor.getX();
                    			numberPositions[numberCounter][1] = cursor.getY()-1;
                    			chain.add(gameBoard[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                    			
                    			// Next down
                    			
                    			numberPositions[numberCounter][0] = cursor.getX();
                    			numberPositions[numberCounter][1] = cursor.getY()+1;
                    			chain.add(gameBoard[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
                    			
                    			numberCounter++;
                			}
            			}
            		}
            		
            		else if(cursorCorrect && plusPositions[plusCounter-1][0]%2 == 0 && plusPositions[plusCounter-1][1]%2 == 1) { // last plus is vertical
            			if(plusPositions[plusCounter-1][0] == cursor.getX() && plusPositions[plusCounter-1][1]+2 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) { // 1 x0 y+  down 
            				
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		} else if(plusPositions[plusCounter-1][0] == cursor.getX() && plusPositions[plusCounter-1][1]-2 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) { // 2 x0 y- up 
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		} else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 3 x+ y+ right
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		} else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 4 x- y+ left
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		} else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 5 x+ y- right
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		} else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 6 x- y- left 
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		}
            		} else if(cursorCorrect && plusPositions[plusCounter-1][0]%2 == 1 && plusPositions[plusCounter-1][1]%2 == 0) { // last plus is horizontal
            		
            			if(plusPositions[plusCounter-1][0]+2 == cursor.getX() && plusPositions[plusCounter-1][1] == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()-1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 1 x+ y0  right
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()+1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		} else if(plusPositions[plusCounter-1][0]-2 == cursor.getX() && plusPositions[plusCounter-1][1] == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX()+1 && numberPositions[numberCounter-1][1] == cursor.getY()) { // 2 x- y0 left
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX()-1;
                			numberPositions[numberCounter][1] = cursor.getY();
                		} else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) { // 3 x+ y+ down
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		} else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]+1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()-1) { // 4 x- y+ down
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()+1;
                		} else if(plusPositions[plusCounter-1][0]+1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) { // 5 x+ y- up
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		} else if(plusPositions[plusCounter-1][0]-1 == cursor.getX() && plusPositions[plusCounter-1][1]-1 == cursor.getY() && numberPositions[numberCounter-1][0] == cursor.getX() && numberPositions[numberCounter-1][1] == cursor.getY()+1) { // 6 x- y- up
                		
                			flag = true;
                			
                			numberPositions[numberCounter][0] = cursor.getX();
                			numberPositions[numberCounter][1] = cursor.getY()-1;
                		}
            		}
            		if (flag) {
            			gameBoard[cursor.getY()][cursor.getX()] = '+';
            			plusPositions[plusCounter][0] = cursor.getX();
            			plusPositions[plusCounter][1] = cursor.getY();
            			plusCounter++;
            			
            			chain.add(gameBoard[numberPositions[numberCounter][1]][numberPositions[numberCounter][0]]);
            			numberCounter++;
            		}
            	}
            }
            
            if (rkey == KeyEvent.VK_E) { // E: End of the game
            	gameOver = true;
            	System.exit(0);
	        }
            
        keypr=0;    // last action  
		}
	}
	// ----------------------------------------------------
	public void clear() {
		for(int j = 5; j < 30; j++) 
			for(int i = 11; i < 70; i++) 
				ct.output(i, j, ' ');
	}
	// ----------------------------------------------------
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
	// ----------------------------------------------------
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
	        ct.setCursorPosition(7, 28);
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
	// ----------------------------------------------------
	public String getPlayerName() { return playerName; }
	// ----------------------------------------------------
	public int getScore() { return score; }
	// ----------------------------------------------------
	public void highscoreSort(DoubleLinkedList names, DoubleLinkedList scores) {
	    int size = scores.size();

	    for (int i = 0; i < size - 1; i++) {
	        for (int j = 0; j < size - i - 1; j++) {
	            if ((int) scores.get(j) < (int) scores.get(j + 1)) {
	                // Swap scores
	                Object tempScore = scores.get(j);
	                scores.set(j, scores.get(j + 1));
	                scores.set(j + 1, tempScore);

	                // Swap names
	                Object tempName = names.get(j);
	                names.set(j, names.get(j + 1));
	                names.set(j + 1, tempName);
	            } else if ((int) scores.get(j) == (int) scores.get(j + 1)) {
	                // If scores are equal, compare names
	                String name1 = (String) names.get(j);
	                String name2 = (String) names.get(j + 1);
	                if (name1.compareTo(name2) > 0) {
	                    // Swap names
	                    Object tempName = names.get(j);
	                    names.set(j, names.get(j + 1));
	                    names.set(j + 1, tempName);
	                }
	            }
	        }
	    }
	}
	// ----------------------------------------------------
}
