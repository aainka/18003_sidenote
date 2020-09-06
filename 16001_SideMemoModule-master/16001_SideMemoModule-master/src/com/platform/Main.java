package com.platform;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Main {
    public static void main(String[] args) {
        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();




        Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, Color.red);

        try { doc.insertString(doc.getLength(), "BLAH ",style); }
        catch (BadLocationException e){}

        StyleConstants.setForeground(style, Color.blue);

        try { doc.insertString(doc.getLength(), "BLEH",style); }
        catch (BadLocationException e){}

        JFrame frame = new JFrame("Test");
        frame.getContentPane().add(textPane);
        frame.pack();
        frame.setVisible(true);
    }
}
