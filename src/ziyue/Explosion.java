package ziyue;
import java.awt.*;



public class Explosion {
	int x, y;
	private boolean live = true;
	
	TankClient tc;
	
	public Explosion(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	private static boolean initial = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] images = {
		tk.getImage(Explosion.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/10.gif"))
	};
	
	int step = 0;
	
	public void draw(Graphics g) {
		if(!initial) {
			for(int i=0; i<images.length; i++) {
				g.drawImage(images[i], -100, -100, null);
			}
		}
		
		if(!live) {
			tc.explosions.remove(this);
			return;
		}
		
		if(step == images.length) {
			live = false;
			step = 0;
			return;
		}
		
		g.drawImage(images[step], x, y, null);
		
		step++;
	}
	
	public void drawCircle(int x, int y, int d, Graphics g) {
		g.fillOval(x - d/2, y - d/2, d, d);
	}
}
