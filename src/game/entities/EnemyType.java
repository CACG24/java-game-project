package game.entities;
import java.util.Random;

public enum EnemyType {
	ENEMY_1(200, 60, EntityData.MAX_SPEED/30, 100, 80),
	ENEMY_2(300, 100, EntityData.MAX_SPEED/60, 200, 120),
	ENEMY_3(100, 20, EntityData.MAX_SPEED/20, 50, 60);
	
	private int healthPoints;
	private int damagePerHit;
	private int speed;
	private int points;
	private int coolDown;
	
	
	//Constructor
	private EnemyType(int healthPoints, int damagePerHit, int speed, int points, int coolDown) {
		this.healthPoints=healthPoints;
		this.damagePerHit=damagePerHit;
		this.speed=speed;
		this.points=points;
		this.coolDown=coolDown;
	}
	
	
	//Getters
	public int getHealthPoints() {
		return healthPoints;
	}
	
	public int getDamagePerHit() {
		return damagePerHit;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getCoolDown() {
		return coolDown;
	}
	
	
	//Method
    public static EnemyType getRandomEnemyType() {
        EnemyType[] enemies = values();
        int randomIndex = new Random().nextInt(enemies.length);
        return enemies[randomIndex];
    }

}
