
public class Cursor {
	
	// attributes
	private int positionX;
	private int positionY;
	
	// ----------------------------------------------------
	public Cursor(int posX, int posY) {
		positionX = posX;
		positionY = posY;
	}
	// ----------------------------------------------------
	// getters
	public int getX() { return positionX; }
	public int getY() { return positionY; }
	// ----------------------------------------------------
	// setters
	public void setX(int input) { positionX = input; }
	public void setY(int input) { positionY = input; }
	
}
