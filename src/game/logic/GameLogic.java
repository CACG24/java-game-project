package game.logic;

import game.world.*;
import game.entities.*;
import game.frame.FrameDimensions;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.awt.Rectangle;

public class GameLogic {
	public static boolean alive=true;
	private List<Level> levels;
	private Player player;
    private int currentLevel;
    
    
    
    //Constructor
    public GameLogic() {
        player = Player.getInstance();
        levels = new ArrayList<>();
        initializeLevels();
        currentLevel = 0;
    }
    
    
    //Getters
    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }
    
    public int getCurrentLevelNumber() {
    	return currentLevel;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    
    //Methods
    private void initializeLevels() {
    	for(int i=0;i<50;i++) {
    		levels.add(new Level());
    	}
    }
    
    
    public void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A: if(canPlayerMove(-player.getSpeed(), 0)) player.move(-player.getSpeed(), 0); 
            					player.setDirection(Direction.LEFT); 
            					player.setAction(Action.RUN); break;
            case KeyEvent.VK_D: if(canPlayerMove(player.getSpeed(), 0)) player.move(player.getSpeed(), 0); 
            					player.setDirection(Direction.RIGHT); 
            					player.setAction(Action.RUN); break;
            case KeyEvent.VK_W: if(canPlayerMove(0, -player.getSpeed())) player.move(0, -player.getSpeed()); 
            					player.setDirection(Direction.UP);
            					player.setAction(Action.RUN); break;
            case KeyEvent.VK_S: if(canPlayerMove(0, player.getSpeed())) player.move(0, player.getSpeed()); 
            					player.setDirection(Direction.DOWN);
            					player.setAction(Action.RUN); break;
            case KeyEvent.VK_SPACE: if(player.getHitCoolDown()>30) {
            							playerHit();
            							player.setHitCoolDown(0);
            						} 
            						player.setAction(Action.ATTACK); break;
            case KeyEvent.VK_SHIFT: player.setAction(Action.DEFEND); break;
            case KeyEvent.VK_F: nextLevel();
        }
    }

    
    public void handleKeyRelease(KeyEvent e) {
    	switch (e.getKeyCode()) {
    		case KeyEvent.VK_A: 
    		case KeyEvent.VK_D: 
    		case KeyEvent.VK_W: 
    		case KeyEvent.VK_S: 
    		case KeyEvent.VK_SHIFT: player.setAction(Action.IDLE); break;
    	}
    }

    
    public void update() {
    	//Enemy
        for (Enemy enemy : getCurrentLevel().getEnemies()) {
        	//Movement
        	int disX=player.getPosX()-enemy.getPosX();
        	int disY=player.getPosY()-enemy.getPosY();
            int dx=(disX!=0)? ((disX>0)? enemy.getSpeed():-enemy.getSpeed()):0;
            int dy=(disY!=0)? ((disY>0)? enemy.getSpeed():-enemy.getSpeed()):0;
            if (!canEnemyMove(enemy, dx, 0) || (disX<30 && disX>-30))
                dx = 0;
            if (!canEnemyMove(enemy, 0, dy) || (disY<30 && disY>-30))
                dy = 0;
            enemy.move(dx, dy);
            
            //Attack
            enemy.setHitCoolDown(enemy.getHitCoolDown()+1);
            if(canEnemyHit(enemy)) {
            	if(enemy.getAction()!=Action.DEAD && enemy.getHitCoolDown()>enemy.getType().getCoolDown()) {
                	enemy.setAction(Action.ATTACK);
                	player.takeDamage(enemy.getDamagePerHit());
                	player.setAction(Action.HURT);
                	enemy.setHitCoolDown(0);
                } 
            } else if(dx!=0 || dy!=0) {
            	enemy.setAction(Action.RUN);
            } else {
            	enemy.setAction(Action.IDLE);
            }
            
        }
        //Die
        Iterator<Enemy> iterator = getCurrentLevel().getEnemies().iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.getHealthPoints()==0 && enemy.getAction()!=Action.DEAD) {
            	enemy.setAction(Action.DEAD);
            }
            if(enemy.getAlive()==false) {
            	iterator.remove();
            	player.setPoints(enemy.getType().getPoints());
            }
        }
        
        //Player
        player.setHitCoolDown(player.getHitCoolDown()+1);
        if(player.getHealthPoints()==0) {
        	alive=false;
        	player.setAction(Action.DEAD);
        }
        
        //Pass level
        if(	getCurrentLevel().getEnemies().size()==0 &&
        	player.getPosY()>270 && player.getPosY()<330) {
        	if(player.getPosX()<20) previousLevel();
        	if(player.getPosX()>560) nextLevel();
        	
        }
        
        
    }


    public void nextLevel() {
        if(currentLevel<levels.size()-1) {
        	getCurrentLevel().setLastX(player.getPosX());
        	getCurrentLevel().setLastY(player.getPosY());
            currentLevel++;
            if(player.getPosX()>555) {
            	getCurrentLevel().setLastX(30);
            } else if(player.getPosX()<25) {
            	getCurrentLevel().setLastX(550);
            }
            if(player.getPosY()==570) {
            	getCurrentLevel().setLastY(30);
            } else if(player.getPosX()==30) {
            	getCurrentLevel().setLastX(570);
            }
            player.setPosX(getCurrentLevel().getLastX());
            player.setPosY(getCurrentLevel().getLastY());
            if(getCurrentLevel().getEnemies().size()!=0)
            	player.setHealthPoints(player.getHealthPoints()+400);
        }
    }

    
    public void previousLevel() {
        if(currentLevel>0) {
        	getCurrentLevel().setLastX(player.getPosX());
        	getCurrentLevel().setLastY(player.getPosY());
            currentLevel--;
            if(player.getPosX()>555) {
            	getCurrentLevel().setLastX(30);
            } else if(player.getPosX()<25) {
            	getCurrentLevel().setLastX(550);
            }
            if(player.getPosY()==570) {
            	getCurrentLevel().setLastY(30);
            } else if(player.getPosX()==30) {
            	getCurrentLevel().setLastX(570);
            }
            player.setPosX(getCurrentLevel().getLastX());
            player.setPosY(getCurrentLevel().getLastY());
        }
    }
    
    
    private boolean canEnemyMove(Enemy enemy, int dx, int dy) {
        int nextPosX = enemy.getPosX() + dx;
        int nextPosY = enemy.getPosY() + dy;
        Rectangle enemyHitbox = new Rectangle(	nextPosX, nextPosY, 
        										FrameDimensions.ENTITY_WIDTH,
        										FrameDimensions.ENTITY_HEIGHT);

        //Check collision with other enemies
        for (Enemy otherEnemy : getCurrentLevel().getEnemies()) {
            if (otherEnemy != enemy) {
                Rectangle otherHitbox = new Rectangle(	otherEnemy.getPosX(), otherEnemy.getPosY(),
                										FrameDimensions.ENTITY_WIDTH,
                										FrameDimensions.ENTITY_HEIGHT);
                if (enemyHitbox.intersects(otherHitbox)) {
                    return false;
                }
            }
        }

        //Check collision with player
        Rectangle playerHitbox = new Rectangle(	player.getPosX(), player.getPosY(),
        										FrameDimensions.ENTITY_WIDTH,
        										FrameDimensions.ENTITY_HEIGHT);
        if (enemyHitbox.intersects(playerHitbox)) {
            return false;
        }
        return true;
    }
    
    
    private boolean canPlayerMove(int dx, int dy) {
    	int nextPosX = player.getPosX() + dx;
        int nextPosY = player.getPosY() + dy;
        Rectangle playerHitbox = new Rectangle(	nextPosX, nextPosY,
        										FrameDimensions.ENTITY_WIDTH,
        										FrameDimensions.ENTITY_HEIGHT);
        
    	for(Enemy enemy : getCurrentLevel().getEnemies()) {
    		Rectangle enemyHitbox = new Rectangle(	enemy.getPosX(), enemy.getPosY(),
    												FrameDimensions.ENTITY_WIDTH,
    												FrameDimensions.ENTITY_HEIGHT);
    		if (playerHitbox.intersects(enemyHitbox)) {
    			return false;
    		}
        }
    	return true;
    }
    
    
    private boolean canEnemyHit(Enemy enemy) {
    	int damageBoxPosX=0;
    	int damageBoxPosY=0;
    	switch(enemy.getDirection()) {
    		case UP:	damageBoxPosX=enemy.getPosX();
    					damageBoxPosY=enemy.getPosY()-FrameDimensions.ENTITY_HEIGHT/2; break;
    		case DOWN:	damageBoxPosX=enemy.getPosX();
						damageBoxPosY=enemy.getPosY()+FrameDimensions.ENTITY_HEIGHT/2; break;
    		case LEFT:	damageBoxPosX=enemy.getPosX()-FrameDimensions.ENTITY_WIDTH/2;
						damageBoxPosY=enemy.getPosY();	break;
    		case RIGHT:	damageBoxPosX=enemy.getPosX()+FrameDimensions.ENTITY_WIDTH/2;;
						damageBoxPosY=enemy.getPosY(); break;
    	}
    	Rectangle enemyDamageBox = new Rectangle(	damageBoxPosX, damageBoxPosY,
													FrameDimensions.ENTITY_WIDTH,
													FrameDimensions.ENTITY_HEIGHT);
    	Rectangle playerHitbox = new Rectangle(	player.getPosX(), player.getPosY(),
												FrameDimensions.ENTITY_WIDTH,
												FrameDimensions.ENTITY_HEIGHT);
    	if(enemyDamageBox.intersects(playerHitbox) && player.getAction()!=Action.DEFEND) {
    		return true;
    	}
    	return false;
    }
    
    
    private void playerHit() {
    	int damageBoxPosX=0;
    	int damageBoxPosY=0;
    	int width=0;
    	int height=0;
    	switch(player.getDirection()) {
			case UP:	damageBoxPosX=player.getPosX();
						damageBoxPosY=player.getPosY()-FrameDimensions.ENTITY_WIDTH; 
						width=FrameDimensions.ENTITY_WIDTH*2; 
						height=FrameDimensions.ENTITY_WIDTH; break;
			case DOWN:	damageBoxPosX=player.getPosX();
						damageBoxPosY=player.getPosY()+FrameDimensions.ENTITY_WIDTH*2;
						width=FrameDimensions.ENTITY_WIDTH*2; 
						height=FrameDimensions.ENTITY_WIDTH; break;
			case LEFT:	damageBoxPosX=player.getPosX()-FrameDimensions.ENTITY_WIDTH;
						damageBoxPosY=player.getPosY();
						width=FrameDimensions.ENTITY_WIDTH; 
						height=FrameDimensions.ENTITY_WIDTH*2; break;
			case RIGHT:	damageBoxPosX=player.getPosX()+FrameDimensions.ENTITY_WIDTH;
						damageBoxPosY=player.getPosY();
						width=FrameDimensions.ENTITY_WIDTH; 
						height=FrameDimensions.ENTITY_WIDTH*2; break;
    	}
    	Rectangle playerDamageBox = new Rectangle(damageBoxPosX, damageBoxPosY, width, height);
    	for(Enemy enemy : getCurrentLevel().getEnemies()) {
    		Rectangle enemyHitbox = new Rectangle(	enemy.getPosX(), enemy.getPosY(),
													FrameDimensions.ENTITY_WIDTH,
													FrameDimensions.ENTITY_HEIGHT);
    		if (playerDamageBox.intersects(enemyHitbox)) {
    			enemy.takeDamage(player.getDamagePerHit());
    			switch(player.getDirection()) {
    				case UP: 	if(canEnemyMove(enemy,0,-FrameDimensions.ENTITY_WIDTH)) 
    								enemy.move(0,-FrameDimensions.ENTITY_WIDTH);
    							break;
    				case DOWN: 	if(canEnemyMove(enemy,0,FrameDimensions.ENTITY_WIDTH)) 
    								enemy.move(0,FrameDimensions.ENTITY_WIDTH);
    							break;
    				case LEFT: 	if(canEnemyMove(enemy,-FrameDimensions.ENTITY_WIDTH,0))
    								enemy.move(-FrameDimensions.ENTITY_WIDTH,0); 
    							break;
    				case RIGHT: if(canEnemyMove(enemy,FrameDimensions.ENTITY_WIDTH,0))
    								enemy.move(FrameDimensions.ENTITY_WIDTH,0); 
    							break;
    			}
    		}
        }
    }
    
}
