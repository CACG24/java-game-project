package game.frame;

import game.logic.*;
import game.entities.*;
import game.entities.Action;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class GameFrame extends JFrame implements KeyListener, FrameDimensions {
	private static final long serialVersionUID = 1L;
	private GameLogic gameLogic;
	private Image backgroundImage;
    private BufferedImage playerIdle;
    private BufferedImage playerRun;
    private BufferedImage playerAttack;
    private BufferedImage playerDefend;
    private BufferedImage playerHurt;
    private BufferedImage playerDead;
    private BufferedImage enemyIdle_1;
    private BufferedImage enemyRun_1;
    private BufferedImage enemyAttack_1;
    private BufferedImage enemyHurt_1;
    private BufferedImage enemyDead_1;
    private BufferedImage enemyIdle_2;
    private BufferedImage enemyRun_2;
    private BufferedImage enemyAttack_2;
    private BufferedImage enemyHurt_2;
    private BufferedImage enemyDead_2;
    private BufferedImage enemyIdle_3;
    private BufferedImage enemyRun_3;
    private BufferedImage enemyAttack_3;
    private BufferedImage enemyHurt_3;
    private BufferedImage enemyDead_3;
    
    //Sword effect
    private static boolean isPlaying=false;
    
    //Cycle counter
    private static int cicles=0;
	
    
    //Constructor
	public GameFrame(GameLogic gameLogic) {
		//Frame configuration
		this.gameLogic=gameLogic;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(MAX_WIDTH, MAX_HEIGHT);
		this.setLayout(null);
		this.addKeyListener(this);
		this.setVisible(true);
		this.setTitle("Bit Dungeon");
		
		//Buffer strategy
		this.createBufferStrategy(2);
		
        //Image reading
        try {
        	backgroundImage = ImageIO.read(getClass().getResource("/resources/fondo.jpg"));
            playerIdle = ImageIO.read(getClass().getResource("/resources/Player/Idle.png"));
            playerRun = ImageIO.read(getClass().getResource("/resources/Player/Run.png"));
            playerAttack = ImageIO.read(getClass().getResource("/resources/Player/Attack 2.png"));
            playerDefend = ImageIO.read(getClass().getResource("/resources/Player/Defend.png"));
            playerHurt = ImageIO.read(getClass().getResource("/resources/Player/Hurt.png"));
            playerDead = ImageIO.read(getClass().getResource("/resources/Player/Dead.png"));
            enemyIdle_1 = ImageIO.read(getClass().getResource("/resources/Enemy_1/Idle.png"));
            enemyRun_1 = ImageIO.read(getClass().getResource("/resources/Enemy_1/Walk.png"));
            enemyAttack_1 = ImageIO.read(getClass().getResource("/resources/Enemy_1/Attack_1.png"));
            enemyHurt_1 = ImageIO.read(getClass().getResource("/resources/Enemy_1/Hurt.png"));
            enemyDead_1 = ImageIO.read(getClass().getResource("/resources/Enemy_1/Dead.png"));
            enemyIdle_2 = ImageIO.read(getClass().getResource("/resources/Enemy_2/Idle.png"));
            enemyRun_2 = ImageIO.read(getClass().getResource("/resources/Enemy_2/Walk.png"));
            enemyAttack_2 = ImageIO.read(getClass().getResource("/resources/Enemy_2/Attack_2.png"));
            enemyHurt_2 = ImageIO.read(getClass().getResource("/resources/Enemy_2/Hurt.png"));
            enemyDead_2 = ImageIO.read(getClass().getResource("/resources/Enemy_2/Dead.png"));
            enemyIdle_3 = ImageIO.read(getClass().getResource("/resources/Enemy_3/Idle.png"));
            enemyRun_3 = ImageIO.read(getClass().getResource("/resources/Enemy_3/Walk.png"));
            enemyAttack_3 = ImageIO.read(getClass().getResource("/resources/Enemy_3/Attack_1.png"));
            enemyHurt_3 = ImageIO.read(getClass().getResource("/resources/Enemy_3/Hurt.png"));
            enemyDead_3 = ImageIO.read(getClass().getResource("/resources/Enemy_3/Dead.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Sound configuration
        try {
            File soundFile = new File("src/resources/Sound/AriaBiblio.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            //Volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(5.0f);

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
	
	
	//Methods
	@Override
    public void paint(Graphics g) {}
	
	
	public void render() {
		cicles++;
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            return;
        }
        Graphics g = bufferStrategy.getDrawGraphics();
        drawGame(g);
        g.dispose();
        bufferStrategy.show();
    }
	
	
	private void drawGame(Graphics g) {
		//Clean screen
		g.drawImage(backgroundImage, 0, 80, getWidth(), 520, null);
		
		//Draw health bar
		drawHealthBar(g);
        
        //Draw player
        Player player = Player.getInstance();
        drawEntity(g, player);

        //Draw enemies
        for (Enemy enemy : gameLogic.getCurrentLevel().getEnemies()) {
        	drawEntity(g, enemy);
        }
        
      //Game Over
        if(GameLogic.alive==false) {
        	String gameOver = "Game Over";
        	g.setColor(Color.RED);
        	g.setFont(new Font("Arial", Font.BOLD, 60));
        	int textWidth = g.getFontMetrics().stringWidth(gameOver);
        	int textX = 0 + (FrameDimensions.MAX_WIDTH - textWidth) / 2;
            int textY = 270 + ((80 + g.getFontMetrics().getAscent()) / 2) - 2;
            g.drawString(gameOver, textX, textY);
        }
        
        
    }
	
	
	public void drawEntity(Graphics g, Entity entity) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = new AffineTransform();
		BufferedImage frame=null;
		int x=entity.getPosX();
		int y=entity.getPosY();
		
		//Player
		if(entity instanceof Player) {
			Player player=(Player) entity;
			Direction direction=player.getDirection();
			BufferedImage sprite;
			int frames;
			//Sprite picker
			switch(player.getAction()) {
				case IDLE: sprite=playerIdle; frames=sprite.getWidth()/128; break;
				case RUN: sprite=playerRun; frames=sprite.getWidth()/128; break;
				case ATTACK: sprite=playerAttack; frames=sprite.getWidth()/128; break;
				case DEAD: 	sprite=playerDead; frames=sprite.getWidth()/128; 
							if(cicles>50) cicles=10; break;
				case DEFEND: sprite=playerDefend; frames=sprite.getWidth()/128; break;
				case HURT: 	sprite=playerHurt; frames=sprite.getWidth()/128; 
							player.counter=cicles; break;
				default: sprite=playerIdle; frames=sprite.getWidth()/128; break;
			}
			//Frame picker
			switch((cicles/10)%frames) {
				case 1: frame = sprite.getSubimage(0, 0, 128, 128); break;
				case 2: frame = sprite.getSubimage(128, 0, 128, 128); break;
				case 3: frame = sprite.getSubimage(256, 0, 128, 128); break;
				case 4: frame = sprite.getSubimage(384, 0, 128, 128); break;
				case 5: frame = sprite.getSubimage(512, 0, 128, 128); break;
				case 6: frame = sprite.getSubimage(640, 0, 128, 128); break;
				case 0: frame = sprite.getSubimage(128*(frames-1), 0, 128, 128);
						if(player.getAction()==Action.ATTACK || (player.getAction()==Action.HURT && cicles>player.counter+20))
							player.setAction(Action.IDLE);
			}
			//Transformations
			transform.scale((double) 122/128, (double) 108/128);
			switch(direction) {
				case UP:
				case DOWN:
				case RIGHT: transform.translate(x, y); break;
				case LEFT: 	transform.translate(x+65, y);
            				transform.scale(-1, 1); break;
			}
		}
		
		//Enemy
		if(entity instanceof Enemy) {
			Enemy enemy=(Enemy) entity;
			Direction direction=enemy.getDirection();
			BufferedImage sprite=null;
			int frames=0;
			//Sprite picker
			switch(enemy.getAction()) {
				case IDLE: 	if(enemy.getType()==EnemyType.ENEMY_1) {
								sprite=enemyIdle_1; frames=sprite.getWidth()/128;
							} else if(enemy.getType()==EnemyType.ENEMY_2) {
								sprite=enemyIdle_2; frames=sprite.getWidth()/128;
							} else {
								sprite=enemyIdle_3; frames=sprite.getWidth()/128;
							}  break;
				case RUN: 	if(enemy.getType()==EnemyType.ENEMY_1) {
								sprite=enemyRun_1; frames=sprite.getWidth()/128;
							} else if(enemy.getType()==EnemyType.ENEMY_2) {
								sprite=enemyRun_2; frames=sprite.getWidth()/128;
							} else {
								sprite=enemyRun_3; frames=sprite.getWidth()/128;
							}  break;
				case ATTACK:if(enemy.getType()==EnemyType.ENEMY_1) {
								sprite=enemyAttack_1; frames=sprite.getWidth()/128;
							} else if(enemy.getType()==EnemyType.ENEMY_2) {
								sprite=enemyAttack_2; frames=sprite.getWidth()/128;
							} else {
								sprite=enemyAttack_3; frames=sprite.getWidth()/128;
							}  break;
				case DEAD:	if(enemy.getType()==EnemyType.ENEMY_1) {
								sprite=enemyDead_1; frames=sprite.getWidth()/128;
							} else if(enemy.getType()==EnemyType.ENEMY_2) {
								sprite=enemyDead_2; frames=sprite.getWidth()/128;
							} else {
								sprite=enemyDead_3; frames=sprite.getWidth()/128;
							}  break;
				case HURT: 	if(enemy.getType()==EnemyType.ENEMY_1) {
								sprite=enemyHurt_1; frames=sprite.getWidth()/128;
							} else if(enemy.getType()==EnemyType.ENEMY_2) {
								sprite=enemyHurt_2; frames=sprite.getWidth()/128;
							} else {
								sprite=enemyHurt_3; frames=sprite.getWidth()/128;
							} enemy.counter=cicles;  break;
				default: sprite=enemyIdle_1; frames=sprite.getWidth()/128; break;
			}
			//Frame picker
			switch((cicles/10)%frames) {
				case 1: frame = sprite.getSubimage(0, 0, 128, 128); break;
				case 2: frame = sprite.getSubimage(128, 0, 128, 128); break;
				case 3: frame = sprite.getSubimage(256, 0, 128, 128); break;
				case 4: frame = sprite.getSubimage(384, 0, 128, 128); break;
				case 5: frame = sprite.getSubimage(512, 0, 128, 128); break;
				case 6: frame = sprite.getSubimage(640, 0, 128, 128); break;
				case 7: frame = sprite.getSubimage(768, 0, 128, 128); break;
				case 0: frame = sprite.getSubimage(128*(frames-1), 0, 128, 128);
						if(enemy.getAction()==Action.ATTACK || (enemy.getAction()==Action.HURT && cicles>enemy.counter+40)) {
							enemy.setAction(Action.IDLE);
						}
						if(enemy.getAction()==Action.DEAD) enemy.setAlive(false);
			}
			//Transformations
			transform.scale((double) 122/128, (double) 108/128);
			switch(direction) {
				case UP:
				case DOWN:
				case RIGHT: transform.translate(x-30, y); break;
				case LEFT: 	transform.translate(x+90, y);
		            		transform.scale(-1, 1); break;
			}
					
		}
		
		g2d.drawImage(frame, transform, null);
        //g2d.drawImage(image, transform, null);
	}
	
	
	private void drawHealthBar(Graphics g) {
        int healthBarCurrentWidth = (int)((Player.getInstance().getHealthPoints()/(double)EntityData.MAX_HEALTH)*FrameDimensions.MAX_WIDTH);
        
        //Health bar background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, FrameDimensions.MAX_WIDTH, 80);
        
        //Health bar green part
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, healthBarCurrentWidth, 80);

        //Health bar frame
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, FrameDimensions.MAX_WIDTH, 80);
        
        //Health text
        String healthText = Player.getInstance().getHealthPoints()+"/"+EntityData.MAX_HEALTH;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int textWidth = g.getFontMetrics().stringWidth(healthText);
        int textX = 0 + (FrameDimensions.MAX_WIDTH - textWidth) / 2;
        int textY = 10 + ((80 + g.getFontMetrics().getAscent()) / 2) - 2;
        g.drawString(healthText, textX, textY);
        
        //Points text
        String pointsText = String.format("%d points"
        		+ "                                                                                                        "
        		+ "Level: %d",Player.getInstance().getPoints(),gameLogic.getCurrentLevelNumber()+1);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        textWidth = g.getFontMetrics().stringWidth(pointsText);
        textX = 0 + (FrameDimensions.MAX_WIDTH - textWidth) / 2;
        textY = 540 + ((80 + g.getFontMetrics().getAscent()) / 2) - 2;
        g.drawString(pointsText, textX, textY);
        
    }
	
	
	private void swordSound() {
		if(!isPlaying) {
			try {
            	File soundFile = new File("src/resources/Sound/Sword.wav");
            	AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            	Clip clip = AudioSystem.getClip();
            	clip.open(audioIn);
            	FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f);
            	clip.start();
        	} catch (Exception e) {
            	e.printStackTrace();
        	}
		}
	}
	
	
	@Override
    public void keyPressed(KeyEvent e) {
        gameLogic.handleKeyPress(e);   
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
        	swordSound();
        	isPlaying=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameLogic.handleKeyRelease(e);
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
        	isPlaying=false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
	
}
