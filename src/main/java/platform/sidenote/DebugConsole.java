package platform.sidenote;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DebugConsole {
	static String stext = new String("xxx");
	static JTextArea textArea = new JTextArea();

	public DebugConsole() {
		JFrame frame = new JFrame("DebugConsole");

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(BorderLayout.CENTER, textArea);
		frame.setSize(100, 100);
		frame.setVisible(true);
	}

	public void print(String msg) {
		stext += msg;
		textArea.setText(stext);
	}

	public static void println(String string) {
		stext += string +"\n";
		textArea.setText(stext );
	}

}
