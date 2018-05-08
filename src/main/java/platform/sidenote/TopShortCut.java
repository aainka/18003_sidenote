package platform.sidenote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class TopShortCut extends JPanel implements MouseListener {

	List<TextButton> list = new LinkedList<TextButton>();
	ActionListener actionListener = null;

	public TopShortCut() {
		this.setSize(new Dimension(6, 6));
		this.setPreferredSize(new Dimension(50, 20)); // x.y
		this.setBackground(Color.GRAY);
		this.addMouseListener(this);
		// repaint();
		// this.addMouseListener(l);

	}

	public void add(String cmd) {
		TextButton b1 = new TextButton(cmd);
		list.add(b1);

	}

	public void paint(Graphics g) {
		int pos = 2;
		for (int i = 0; i < list.size(); i++) {
			TextButton b = list.get(i);
			pos += b.paint(pos, 2, g) + 2;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Point p = arg0.getPoint();
		for (int i = 0; i < list.size(); i++) {
			TextButton b = list.get(i);
			if (b.contains(p)) {
			//	System.out.println("match --> " + b.command);
				ActionEvent event = new ActionEvent(this, 0, b.command);
				this.actionListener.actionPerformed(event);
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

	public class TextButton {
		String command = "Button";
		int x, y;
		int width, height = 20;

		public TextButton(String title) {
			this.command = title;
		}

		public String getTitle() {
			return command;
		}

		public int paint(int x, int y, Graphics g) {
			this.x = x;
			this.y = y;
			this.width = g.getFontMetrics().stringWidth(command);
			// g.drawRect(x, y, width, 15);
			g.drawString(command, x, 15);
			return width;
		}

		public boolean contains(Point p) {
			if (p.x > x && p.y > y) {
				if (p.x < x + width && p.y < y + height) {
					return true;
				}
			}
			return false;
		}

	}

}
