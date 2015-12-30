package ziyue;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 
 * @author Ziyue Chen
 *
 */
public class Tank {
	public static final int X_SPEED = 2, Y_SPEED = 2;
	
	public static final int WIDTH = 30, HEIGHT = 30;
	
	private int x, y;
	
	private int oldX, oldY;
	
	private static Random r = new Random();
	
	private int step = r.nextInt(30) + 10;
	
	private boolean good;
	
	private int HP = 100;
	
	private HPBar hpBar = new HPBar();
	
	Sound shoot = new Sound("sound/shoot.wav");
	Sound getHP = new Sound("sound/getHP.wav");
	Sound gameStart = new Sound("sound/gameStart.wav");
	
	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public boolean isGood() {
		return good;
	}

	private boolean live = true;
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	private boolean bL = false, bR = false, bU = false, bD = false;
	
	public boolean isMoving() {
		return bL || bR || bU || bD;
	}
	
	private Direction dir = Direction.STOP;
	private Direction barrelDir = Direction.U;
	
	TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	
	private static Image tankU = tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif"));
	private static Image tankD = tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif"));
	private static Image tankL = tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif"));
	private static Image tankR = tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif"));
	
	
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void setLocation(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		barrelDir = dir;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		if(good) hpBar.draw(g);
		
		switch(barrelDir) {
		case L:
			g.drawImage(tankL, x-9, y-9, null);
			break;
		case U:
			g.drawImage(tankU, x-9, y-9, null);
			break;
		case R:
			g.drawImage(tankR, x-9, y-9, null);
			break;
		case D:
			g.drawImage(tankD, x-9, y-9, null);
			break;
		case STOP: 
			break;
		}
		
		move();
	}
	
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	public void move() {
		this.oldX = x;
		this.oldY = y;
		
		switch(dir) {
		case L:
			x -= X_SPEED;
			break;
		case U:
			y -= Y_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case STOP: 
			break;
		}
		
		if(dir!=Direction.STOP) {
			barrelDir = dir;
		}
		
		if(x < 0) x = 0;
		if(y < 25) y = 25;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
			
		if(!good) {
			Direction[] dirs = Direction.values();
			if(step==0) {
				step = r.nextInt(30) + 10;
				int randomNum = r.nextInt(dirs.length);
				dir = dirs[randomNum];
			}
			step--;
			
			if(r.nextInt(50)>48) this.fire();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_ENTER:
			if(!this.live) {
				this.live = true;
				this.HP = 100;
				tc.score = 0;
				this.setLocation(TankClient.GAME_WIDTH/2-Tank.WIDTH/2, TankClient.GAME_HEIGHT*5/6-Tank.HEIGHT/2, Direction.U);
				tc.tanks.clear();
				int initTankCount = Integer.parseInt(PropertyManager.getProperty("initTankCount"));
				
				for(int i=0; i<initTankCount; i++) {
					tc.tanks.add(new Tank(200 + 50 * i, 100, false, Direction.D, tc));
				}
			}
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		}
		
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_X:
			superFire();
			break;
		case KeyEvent.VK_Z:
			shoot.play();
			fire();
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		}
		
		locateDirection();
	}
	
	public void locateDirection() {
		if(bU && !bD && !bL && !bR) dir = Direction.U; 
		else if(!bU && bD && !bL && !bR) dir = Direction.D;
		else if(!bU && !bD && bL && !bR) dir = Direction.L;
		else if(!bU && !bD && !bL && bR) dir = Direction.R;
		else if(!bU && !bD && !bL && !bR) dir = Direction.STOP;
	}
	
	public Missile fire() {
		if(!live) return null;
		
		Missile m = new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, good, barrelDir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir) {
		if(!live) return null;
		
		Missile m = new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<4; i++) {
			fire(dirs[i]);
		}
	}
	
	private class HPBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 3);
			int w = WIDTH * HP / 100;
			g.fillRect(x, y-10, w, 3);
			g.setColor(c);
		}
	}
	
	public boolean getFuel(Fuel f) {
		if(this.live && f.isLive() && this.getRect().intersects(f.getRect())) {
			this.setHP(100);
			getHP.play();
			f.setLive(false);
			return true;
		}
		return false;
	}
	
	
}
