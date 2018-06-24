package platform.sidenote;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import platform.sidenote.util.Debug;

public class TinyLabel extends Component {

	Debug logger = Debug.getLogger(this.getClass());

	public TinyLabel() {
	//	this.setSize(new Dimension(100, 100));
		this.setPreferredSize(new Dimension(40, 20));
		//this.setLocation(new Point(0, 0));
	}

	public TinyLabel(String name) {
		this.setName(name);
		this.setPreferredSize(new Dimension(40, 20));
	}
	

	@Override
	public void paint(Graphics g) {
	//	logger.info("name="+this.getName());
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawString(getName(), 0, 13);
	}

//	public int getTextWidth() {
//		return g.getFontMetrics().stringWidth(getName());
//	}
	
	public boolean contains(Point p) {
		int width = getX()+getWidth();
		int height = getY()+getHeight();
		return (p.x >= getX()) && (p.x < width) && (p.y >= getY()) && (p.y < height);
	}

}
