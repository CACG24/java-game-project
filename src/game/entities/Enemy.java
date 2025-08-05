package game.entities;

public class Enemy extends Entity {	
	private EnemyType type;
	
	
	
	//Constructor
	public Enemy(int posX, int posY, EnemyType type) {
		super(posX, posY, type.getHealthPoints());
		setType(type);
		setDamagePerHit(type.getDamagePerHit());
		setSpeed(type.getSpeed());
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
	
	public void setType(EnemyType type) {
		this.type=type;
	}
	
	
	//Getters
	@Override
	public int getDamagePerHit() {
		return super.getDamagePerHit();
	}
	
	@Override
	public int getSpeed() {
		return super.getSpeed();
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
	
	public EnemyType getType() {
		return type;
	}
	
	
	//Methods
	@Override
	public void dealDamage() {}

}
