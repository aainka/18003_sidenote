package platform.sidenote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SPNoteEditor extends JTextPane {
	Document blank = new DefaultStyledDocument();
	Style style0 = null;
	Style style1 = null;
	StyledDocument doc = null;

	public SPNoteEditor() {
		setDocument(blank);
		{
			doc = (StyledDocument) this.getStyledDocument();
			style0 = doc.addStyle("I'm a Style", null);
			style1 = doc.addStyle("I'm a Style1", null);
			StyleConstants.setForeground(style1, Color.BLACK);
			// StyleConstants.setBackground(style1, new Color(51, 153, 255));
			StyleConstants.setBackground(style1, new Color(232, 242, 254));
			StyleConstants.setForeground(style0, Color.BLACK);
			StyleConstants.setBackground(style0, Color.WHITE);
		}
		coloring();
		// noteEditor.setPreferredSize(new Dimension(500, 100));
		// noteEditor.setMaximumSize(new Dimension(500, 100));
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				coloring();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

				}
			}
		});
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		coloring();
	}

	private void coloring() {
		//coloring("(완료)");
		lineColor("(제목)");
	}

	public JScrollPane addScroll(int width, int height) {
		JScrollPane scrollPane = new JScrollPane(this);
		scrollPane.setPreferredSize(new Dimension(width, height));
		scrollPane.setMinimumSize(new Dimension(2, 2));
		return scrollPane;
	}

	public void lineColor(String word) {
		int sp = 0;
		try {
			String text = doc.getText(0, doc.getLength());
			String[] lines = text.split("\n");
			for (String s : lines) {
				if (s.indexOf(word) >= 0) {
					doc.remove(sp, s.length() + 1);
					doc.insertString(sp, s, style1);
					doc.insertString(sp + s.length(), "\n", style0);
				}
				sp += s.length() + 1;
			}

			//
			// while (sp < text.length()) {
			// sp = text.indexOf(word + " ", sp+1);
			// System.out.println("find coloring.sp = "+sp);
			// if (sp > 0) {
			// doc.remove(sp, word.length() + 1);
			// doc.insertString(sp, word, style1);
			// doc.insertString(sp + word.length(), " ", style0);
			// } else {
			// sp = text.length() + 1;
			// }
			// }
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void coloring(String word) {
		int sp = 0;
		String text;
		try {
			text = doc.getText(0, doc.getLength());
			while (sp < text.length()) {
				sp = text.indexOf(word + " ", sp + 1);
				System.out.println("find coloring.sp = " + sp);
				if (sp > 0) {
					doc.remove(sp, word.length() + 1);
					doc.insertString(sp, word, style1);
					doc.insertString(sp + word.length(), " ", style0);
				} else {
					sp = text.length() + 1;
				}
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// public static void xx(String[] args) {
	// JTextPane textPane = new JTextPane();
	// StyledDocument doc = textPane.getStyledDocument();
	//
	//
	// StyleContext sc = StyleContext.getDefaultStyleContext();
	// AttributeSet aset = sc.addAttribute(
	// SimpleAttributeSet.EMPTY,
	// StyleConstants.Foreground, highlightColor);
	// cobolProgram.setCharacterAttributes(offset, length, aset,
	// false);
	//
	//
	// }

}
