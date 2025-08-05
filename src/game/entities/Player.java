package game.entities;

public class Player extends Entity {
	private static Player instance=null;
	private int points=0;
	
	
	
	//Constructor
	public Player() {
		super(MAX_WIDTH/2, MAX_HEIGHT/2, MAX_HEALTH);
		super.setDamagePerHit(MAX_DAMAGE/2);
		super.setSpeed(MAX_SPEED/12);
		super.setHitCoolDown(160);
	}
	
	//GetInstance
	public static Player getInstance() {
		if(instance==null)
			instance=new Player();
		return instance;
	}
	
	
	//Setters
	@Override
	public void setDamagePerHit(int damagePerHit) {
		super.setDamagePerHit(damagePerHit);
	}
	
	@Override
	public void setSpeed(int speed) {
		super.setSpeed(speed);
	}
	
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
	@Override
	public void setHitCoolDown(int hitCoolDown) {
		super.setHitCoolDown(hitCoolDown);
	}
	
	@Override
	public void setAction(Action action) {
		super.setAction(action);
	}
	
	public void setPoints(int points) {
		this.points+=points;
	}
	
	
	//Getters
	@Override
	public int getSpeed() {
		return super.getSpeed();
	}
	
	@Override
	public int getDamagePerHit() {
		return super.getDamagePerHit();
	}
	
	@Override
	public Direction getDirection() {
		return super.getDirection();
	}
	
	@Override
	public int getHitCoolDown() {
		return super.getHitCoolDown();
	}
	
	@Override
	public Action getAction() {
		return super.getAction();
	}
	
	public int getPoints() {
		return points;
	}
	
	
	//Methods
	@Override
	public void dealDamage() {}
	
}
