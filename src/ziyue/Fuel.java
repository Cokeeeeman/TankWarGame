package ziyue;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Fuel {
	int x, y, w, h;
	
	private boolean live = true;
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	TankClient tc;
	
	Random r = new Random();
	
	//private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	//private static Image heart = tk.getImage(Missile.class.getClassLoader().getResource("images/heart.gif"));
	
	public Fuel() {
		x = 390;
		y = 110;
		w = 20;
		h = 20;
	}
	
	public void draw(Graphics g) {
		if(!live) return;
		//g.drawImage(heart, x, y, null);
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
