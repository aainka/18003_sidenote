package platform.sidenote;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class SPFrame extends JFrame {

	TaskTreeController controller = new TaskTreeController();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public void init() {
		
		controller.setFrame(this);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 350;
		int height = (int) screenSize.getHeight() - 50;

		Dimension windowSize = new Dimension(width, height);
		this.setSize(windowSize);

		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		this.setLocation(screenSize.width - width, 0);
		this.setResizable(true);

		build(this.getContentPane());
		this.setTitle("즉시,반드시,끝까지,5Y,80:20,마감3");

		this.setVisible(true);

	}

	public void build(Container container) {

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("aaa", controller.buildUi());
		container.add(BorderLayout.CENTER, tabbedPane);
		container.add(BorderLayout.NORTH, controller.getMenubar());
	}

	JFrame frame = this;

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			controller.closeWindow();
			System.exit(0);
		}
	}

	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

	public static final void main(String[] args) {
		new SPFrame().init();
	}

	public void alignRight() {
		Dimension frameSize = this.getSize();
		int x =  screenSize.width - frameSize.width ;
		int y = 0;
		this.setLocation(x, y);
	}
	
	public void alignCentor() {
		Dimension frameSize = this.getSize();
		int x = (screenSize.width - frameSize.width) / 2;
		int y = (screenSize.height - frameSize.height) / 2;
		this.setLocation(x, y);
	}
}
