package com.elg.uis;

import java.awt.Color;

import javax.swing.UIManager;

import com.incors.plaf.alloy.AlloyLookAndFeel;

/**
 * 
* <pre>
* <p> Title: EmsLookAndFeel.java </p>
* <p> Description: LookAndFeel 설정 클래스</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class EmsLookAndFeel
{
    public static final void initialize() {
        UIManager.put("ClassLoader", UIManager.class.getClassLoader());
        try {
            UIManager.put("Panel.background", new Color(233, 233, 233));
            UIManager.put("TabbedPane.background", new Color(233, 233, 233));
            UIManager.put("OptionPane.background", new Color(233, 233, 233));
            AlloyLookAndFeel.setProperty("alloy.licenseCode", "1#GoodSoftwareLab_Inc#e9atmt#52ke10");
            AlloyLookAndFeel.setProperty("alloy.isLookAndFeelFrameDecoration", "true");
            AlloyLookAndFeel.setProperty("alloy.titlePaneTextAlignment", "left");
            AlloyLookAndFeel alloyLnF = new AlloyLookAndFeel();
            UIManager.setLookAndFeel(alloyLnF);
        }
        catch (Exception e) {
        }
    }
}
