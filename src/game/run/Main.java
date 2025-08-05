package game.run;

import game.logic.*;
import game.frame.*;

public class Main implements FrameDimensions {

	public static void main(String[] args) {
		 GameLogic gameLogic = new GameLogic();
	     GameFrame gameFrame = new GameFrame(gameLogic);
	     
	     //Playable cycle
	     while (GameLogic.alive) {
	    	 gameLogic.update();
	         gameFrame.render();
	         
	         //Time control
	         try {
	        	 Thread.sleep(20); //60 FPS
	         } catch (InterruptedException e) {
	        	 e.printStackTrace();
	         }
	     }
	     
	     //Death animation
	     for(int i=0;i<40;i++) {
	    	 gameFrame.render();
	    	 try {
	        	 Thread.sleep(20); //60 FPS
	         } catch (InterruptedException e) {
	        	 e.printStackTrace();
	         }
	     }
	     System.out.println("Game Over");
	}

}
