package platform.sidenote;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class TopShortCut extends JPanel implements MouseListener {

 	Debug logger = Debug.getLogger(this.getClass());
	ActionListener actionListener = null;
	TinyLabel first = new TinyLabel("value");
	int pos = 2;

	public TopShortCut() {
		this.setPreferredSize(new Dimension(50, 20)); // x.y
		this.addMouseListener(this);
		this.setLayout(null);
		this.setLayout(new FlowLayout());
		this.add(first);
	}

	public void add2(String name) {
		TinyLabel label = new TinyLabel(name);
		this.add(label);
	}
	
	public void valueChanged() {
	//	logger.info("");
		first.setBackground(Color.orange);
		updateUI();
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Point p = arg0.getPoint();
		for (Component c : getComponents()) {
			// logger.info(""+c.getX()+", "+c.getY()+","+c.getPreferredSize());
			if (c.contains(p)) {
				// (x >= 0) && (x < width) && (y >= 0) && (y < height)
				logger.info("name=" + c.getName());
				if ( c.getName().equals("SAVE")) {
					first.setBackground(Color.LIGHT_GRAY);
					updateUI();
				}
				ActionEvent event = new ActionEvent(this, 0, c.getName());
				actionListener.actionPerformed(event);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;

	}



	// public class TextButton {
	// String command = "Button";
	// int x, y;
	// int width, height = 20;
	//
	// public TextButton(String title) {
	// this.command = title;
	// }
	//
	// public String getTitle() {
	// return command;
	// }
	//
	// public int paint(int x, int y, Graphics g) {
	// this.x = x;
	// this.y = y;
	// this.width = g.getFontMetrics().stringWidth(command);
	// // g.drawRect(x, y, width, 15);
	// g.drawString(command, x, 15);
	// return width;
	// }
	//
	// public boolean contains(Point p) {
	// if (p.x > x && p.y > y) {
	// if (p.x < x + width && p.y < y + height) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// }

}
