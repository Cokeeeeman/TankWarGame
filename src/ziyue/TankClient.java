package ziyue;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * 1. fire() method
 */
public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800, GAME_HEIGHT = 600;
	
	Tank myTank = new Tank(GAME_WIDTH/2-Tank.WIDTH/2, GAME_HEIGHT*5/6-Tank.HEIGHT/2, true, Direction.STOP, this);
	
	Panel p = new Panel();
	
	Wall w1 = new Wall(200, 200, 100, 30, this);
	Wall w2 = new Wall(200, 400, 100, 30, this);
	Wall w3 = new Wall(500, 200, 100, 30, this);
	Wall w4 = new Wall(500, 400, 100, 30, this);
	
	Fuel f = new Fuel();
	
	int score = 0;
	
	Sound myTankMove = new Sound("sound/myTankMove.wav");
	
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explosion> explosions = new ArrayList<Explosion>();
	
	Thread paintThread = new Thread(new PaintThread());
	Thread moveThread = new Thread(new MoveThread());
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		//g.drawString("Missile Count: " + missiles.size(), 10, 50);
		//g.drawString("Explosions Count: " + explosions.size(), 10, 70);
		g.drawString("Your Score: " + score, 10, 70);
		g.drawString("Enemy Tanks: " + tanks.size(), 10, 90);
		g.drawString("HP: " + myTank.getHP(), 10, 110);
		g.setColor(c);
		
		if(myTank.getHP()==0) {
			c = g.getColor();
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER", GAME_WIDTH/2-40, GAME_HEIGHT/2-10);
			g.drawString("Press ENTER to start over.", GAME_WIDTH/2-80, GAME_HEIGHT/2+10);
			g.setColor(c);
		}
		
		if(tanks.size()==0) {
			/*int reProduceTankCount = Integer.parseInt(PropertyManager.getProperty("reProduceTankCount"));
			for(int i=0; i<reProduceTankCount; i++) {
				tanks.add(new Tank(200 + 50 * i, 400, false, Direction.D, this));
			}*/
			
			tanks.add(new Tank(GAME_WIDTH/2-Tank.WIDTH/2, GAME_HEIGHT*1/6-Tank.HEIGHT/2, false, Direction.D, this));
			tanks.add(new Tank(100, GAME_HEIGHT*1/6-Tank.HEIGHT/2, false, Direction.D, this));
			tanks.add(new Tank(700, GAME_HEIGHT*1/6-Tank.HEIGHT/2, false, Direction.D, this));
			tanks.add(new Tank(100, GAME_HEIGHT/2-Tank.HEIGHT/2, false, Direction.R, this));
			tanks.add(new Tank(700, GAME_HEIGHT/2-Tank.HEIGHT/2, false, Direction.L, this));
		}
		
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithWall(w3);
			t.collidesWithWall(w4);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTank(myTank);
			if(m.hitTanks(tanks)) score++;;
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitWall(w3);
			m.hitWall(w4);
			m.draw(g);
		}
		
		for(int i=0; i<explosions.size(); i++) {
			Explosion e = explosions.get(i);
			e.draw(g);
		}
		
		myTank.draw(g);
		myTank.getFuel(f);
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithWall(w3);
		myTank.collidesWithWall(w4);
		myTank.collidesWithTanks(tanks);
		
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		w4.draw(g);
		f.draw(g);
	}

	public void launchClient() {
		Sound BGM = new Sound("sound/gameStart.wav");
		BGM.play();
		int initTankCount = Integer.parseInt(PropertyManager.getProperty("initTankCount"));
		
		for(int i=0; i<initTankCount; i++) {
			tanks.add(new Tank(200 + 50 * i, 100, false, Direction.D, this));
		}
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar"); 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		setVisible(true);
		
		this.addKeyListener(new KeyMonitor());
		
		paintThread.start();
		moveThread.start();
	}
	
	public static void main(String args[]) {
		TankClient tc = new TankClient();
		tc.launchClient();
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(true) {
				repaint();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class MoveThread implements Runnable {
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(true) {
				if(myTank.isMoving()) {
					myTankMove.play();
					
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int i = 1;
			}
		}
	}
}
