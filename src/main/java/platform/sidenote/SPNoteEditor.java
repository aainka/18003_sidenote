package platform.sidenote;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SPNoteEditor extends JTextPane {
	Document blank = new DefaultStyledDocument();

	public SPNoteEditor() {
		 setDocument(blank);
			// noteEditor.setPreferredSize(new Dimension(500, 100));
			// noteEditor.setMaximumSize(new Dimension(500, 100));
	}
	
	public void coloring() {
		StyledDocument doc = (StyledDocument) this.getStyledDocument();

		Style style = doc.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.red);
		StyleConstants.setBackground(style, Color.YELLOW);
		try {
			doc.insertString(doc.getLength(), "BLAH ", style);
		} catch (BadLocationException ex) {
		}

		StyleConstants.setForeground(style, Color.blue);

		try {
			doc.insertString(doc.getLength(), "BLEH", style);
		} catch (BadLocationException e) {
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
