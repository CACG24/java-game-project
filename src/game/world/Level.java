package game.world;

import game.entities.*;
import game.frame.FrameDimensions;
import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class Level {
	private static int instances=0;
	
	private static final int MAX_ENEMIES=12;
	private static final int MIN_ENEMIES=3;
	
	private List<Enemy> enemies;
	private Player player;
	private int lastX=0;
	private int lastY=0;
	
	
	
	//Constructor
	public Level() {
		player=Player.getInstance();
		if(instances==0) {
			setLastX(300);
			setLastY(300);
			player.setPosX(lastX);
			player.setPosY(lastY);
		}
		
		//Enemy creation
		enemies=new ArrayList<>();
		//Random enemy number
		int enemyAmount=(int)(Math.random()*((MAX_ENEMIES-MIN_ENEMIES)+1))+MIN_ENEMIES;
		for(int i=0;i<enemyAmount; i++) {
			boolean done=false;
			while(!done) {
				done=true;
				Enemy e=new Enemy(	(int)(Math.random()*((FrameDimensions.MAX_WIDTH-40)+1))+10,
									(int)(Math.random()*((FrameDimensions.MAX_HEIGHT-60)+1))+30,
									EnemyType.getRandomEnemyType());
				Rectangle eHitbox = new Rectangle(	e.getPosX(), e.getPosY(),
													FrameDimensions.ENTITY_WIDTH,
													FrameDimensions.ENTITY_HEIGHT);
				Rectangle playerHitbox = new Rectangle(	player.getPosX(), player.getPosY(),
														FrameDimensions.ENTITY_WIDTH,
														FrameDimensions.ENTITY_HEIGHT);
				//Collision with player
				if(eHitbox.intersects(playerHitbox)) done=false;
				
				//Collision with other enemies
				for(Enemy enemy:enemies) {
					Rectangle enemyHitbox = new Rectangle(	enemy.getPosX(), enemy.getPosY(),
															FrameDimensions.ENTITY_WIDTH,
															FrameDimensions.ENTITY_HEIGHT);
					if(eHitbox.intersects(enemyHitbox) || done==false) {
						done=false;
						break;
					}
				}
				if(done==true) enemies.add(e);
			}
		}
		instances++;
	}
	
	
	//Setters
	public void setLastX(int lastX) {
		this.lastX=lastX;
	}
	
	public void setLastY(int lastY) {
		this.lastY=lastY;
	}
	
	
	//Getters
	public List<Enemy> getEnemies(){
		return enemies;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getLastX() {
		return lastX;
	}
	
	public int getLastY() {
		return lastY;
	}

}
