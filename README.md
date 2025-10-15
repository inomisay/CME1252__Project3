# 🎮 Chain Game - CME1252 Project

This repository contains the final submission for the "Chain" game, a project for the CME1252 Project Based Learning – II course at Dokuz Eylul University. The game is a strategic number-based puzzle developed entirely in Java, with a focus on implementing and utilizing custom data structures from scratch.

---

## 📋 About The Project

The goal of "Chain" is to create a captivating number game where players strategically build chains of numbers on a grid to achieve the highest possible score. The game is played on a **10x16 board** populated with numbers from 1 to 4.

Players navigate the board with a cursor and place plus signs `+` to connect adjacent numbers, forming a "chain." A chain is considered valid only if it adheres to specific rules:
<ul>
  <li>The absolute difference between any two adjacent numbers in the chain must be <b>1</b>.</li>
  <li>A chain must have a minimum length of <b>four</b> numbers.</li>
  <li>Only one chain can be constructed per round.</li>
</ul>
The score for a valid chain is calculated as <b>n²</b>, where 'n' is the number of elements in the chain. Successful chains are added to a table, and the player's total score is updated.

---

## ✨ Core Features

<ul>
  <li><b>Randomized Game Board</b>: The 10x16 board is filled with random numbers, with an option for the player to set a custom <b>seed</b> for reproducible gameplay.</li>
  <li><b>Cursor-Based Navigation</b>: Players move a cursor around the board using the arrow keys to select spots for plus signs.</li>
  <li><b>Interactive Menu</b>: A user-friendly main menu allows the player to start a new game, view the high score table, or exit the application.</li>
  <li><b>High Score Table</b>: The game loads and saves a persistent high score table from a <code>highscore.txt</code> file. Scores are sorted in descending order.</li>
  <li><b>Personalization</b>: Players are prompted to enter their name at the start of the game, which is then used for the high score table.</li>
  <li><b>Live Chain Display</b>: As the player builds a chain, its current contents and length are displayed live on the screen.</li>
</ul>

---

## 🛠️ Data Structures Implemented

A key requirement of this project was to build and use custom data structures. The following were implemented:
<ul>
  <li><b><code>SingleLinkedList</code></b>: Used to temporarily store and validate the chain being constructed by the player in the current round.</li>
  <li><b><code>DoubleLinkedList</code></b>: Used to manage the high score table, storing player names and their corresponding scores. This structure was chosen to allow for efficient sorting.</li>
  <li><b><code>MultiLinkedList</code></b>: Used to build and display the table of all successfully completed chains within a game session.</li>
</ul>

---

## ⚙️ How to Run

### Prerequisites
* Java Development Kit (JDK) installed.
* The project is designed to be run in a console that supports cursor positioning, like the one provided by the Enigma library or a standard terminal.

### Setup & Execution
1.  Place the `highscore.txt` file (if provided) in the root directory of the project. If it doesn't exist, the game will create it.
2.  Compile all the `.java` files.
    ```
    javac *.java
    ```
3.  Run the main class to start the game.
    ```
    java Main
    ```
4.  Follow the on-screen instructions to play the game.

---

## 🖥️ Screenshots

---

## 👥 Team Members

<ul>
  <li><b>YASAMIN VALISHARIATPANAHI</b></li>
  <li><b>BAHADIR SEMİH KORKMAZ</b></li>
  <li><b>TALHA MUSTAFA ANTEP</b></li>
  <li><b>ABDULLAH DEMİRCİ</b></li>
  <li><b>ELİF DORUK</b></li>
  <li><b>GÖKBERK SARI</b></li>
  <li><b>ABDULKADIR CAN</b></li>
</ul>
