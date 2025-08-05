package game.entities;

import game.frame.FrameDimensions;

public abstract class Entity implements FrameDimensions, EntityData {
	private int posX=0;
	private int posY=0;
	private int healthPoints=1;
	private boolean alive=true;
	public int counter=0;
	protected int damagePerHit=1;
	protected int speed=1;
	protected Direction direction=Direction.RIGHT;
	protected int hitCoolDown=0;
	protected Action action=Action.IDLE;
	
	
	
	//Constructor
	protected Entity(int posX, int posY, int healthPoints) {
		setPosX(posX);
		setPosY(posY);
		setHealthPoints(healthPoints);
	}
	
	
	//Setters
	public void setPosX(int posX) {
		if(posX>=10 && posX<=MAX_WIDTH-30)
			this.posX=posX;
	}
	
	public void setPosY(int posY) {
		if(posY>=30 && posY<=MAX_HEIGHT-30)
			this.posY=posY;
	}
	
	public void setHealthPoints(int healthPoints) {
		if(healthPoints>=0) {
			if(healthPoints>MAX_HEALTH)
				healthPoints=MAX_HEALTH;
			this.healthPoints=healthPoints;
		}
	}
	
	public void setAlive(boolean alive) {
		this.alive=alive;
	}
	
	protected void setDamagePerHit(int damagePerHit) {
		if(damagePerHit>0 && damagePerHit<=MAX_DAMAGE)
			this.damagePerHit=damagePerHit;
	}
	
	protected void setSpeed(int speed) {
		if(speed>0 && speed<=MAX_SPEED)
			this.speed=speed;
	}
	
	protected void setDirection(Direction direction) {
		this.direction=direction;
	}
	
	protected void setHitCoolDown(int hitCoolDown) {
		this.hitCoolDown=hitCoolDown;
	}
	
	protected void setAction(Action action) {
		this.action=action;
	}

	
	//Getters
	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getHealthPoints() {
		return healthPoints;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	protected int getDamagePerHit() {
		return damagePerHit;
	}
	
	protected int getSpeed() {
		return speed;
	}
	
	protected Direction getDirection() {
		return direction;
	}
	
	protected int getHitCoolDown() {
		return hitCoolDown;
	}
	
	protected Action getAction() {
		return action;
	}
	
	
	//Methods
	public void move(int dx, int dy) {
		if(dx>0) {
			setDirection(Direction.RIGHT);
		} else if(dx<0) {
			setDirection(Direction.LEFT);
		} else if(dy>0) {
			setDirection(Direction.DOWN);
		} else if(dy<0) {
			setDirection(Direction.UP);
		}
		setPosX(getPosX()+dx);
		setPosY(getPosY()+dy);
	}
	
	public void takeDamage(int damage) {
		healthPoints-=damage;
		if(healthPoints<0) healthPoints=0;
	}
	
	
	//Abstract Methods
	public abstract void dealDamage();

}
