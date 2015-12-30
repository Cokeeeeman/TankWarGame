package ziyue;
import java.awt.*;
import java.util.List;

public class Missile {
	public static final int X_SPEED = 5, Y_SPEED = 5;
	
	public static final int WIDTH = 10, HEIGHT = 10;
	
	int x, y;
	
	private boolean good;
	
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	Direction dir;

	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	
	private static Image mU = tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif"));
	private static Image mD = tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif"));
	private static Image mL = tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif"));
	private static Image mR = tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif"));

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		switch(dir) {
		case U: 
			g.drawImage(mU, x, y+2, null);
			break;
		case D:
			g.drawImage(mD, x, y+2, null);
			break;
		case L:
			g.drawImage(mL, x, y+2, null);
			break;
		case R:
			g.drawImage(mR, x, y+2, null);
			break;
		}
		
		move();
	}
	
	public void move() {
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
		
		if(x<0 || x>TankClient.GAME_WIDTH || y<0 || y>TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	Sound gotHit = new Sound("sound/somethingGotHit.wav");
	Sound bomb = new Sound("sound/bomb.wav");
	Sound gameOver = new Sound("sound/gameOver.wav");
	
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()) {
				t.setHP(t.getHP()-20);
				gotHit.play();
				if(t.getHP() <= 0) {
					t.setLive(false);
					gameOver.play();
				}
			} else {
				t.setLive(false);
				bomb.play();
			}
			
			this.live = false;
			Explosion e = new Explosion(x + Missile.WIDTH/2, y + Missile.HEIGHT/2, tc);
			tc.explosions.add(e);
			
			return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			Explosion e = new Explosion(x + Missile.WIDTH/2, y + Missile.HEIGHT/2, tc);
			tc.explosions.add(e);
			
			gotHit.play();
			
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
}
