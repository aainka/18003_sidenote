package com.elg.uis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
* <pre>
* <p> Title: MemoFrame.java </p>
* <p> Description: Memo Main Frame</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class MemoFrame extends JFrame{
	
	//Default File Path
	public static final String DEFAULT_FILE_PATH = "C:\\tmp";
	public static final Color BACKGROUND_COLOR = new Color(237,237,237);
	public static final int DEFAULT_TREE_FONT_SIZE = 13;
	
	public static String FILE_PATH;  
	public static int TREE_FONT_SIZE;  
	
	//Tab Name
	private final String TAB_TITLE_A = "A";
	private final String TAB_TITLE_B = "B";
	private final String TAB_TITLE_C = "C";
	private final String TAB_TITLE_D = "D";
	private final String TAB_TITLE_E = "E";

	//Tree Root Name
	private final String TREE_ROOT_NAME_MEMO_1 = "memo 1";
	private final String TREE_ROOT_NAME_MEMO_2 = "memo 2";
	private final String TREE_ROOT_NAME_MEMO_3 = "memo 3";
	private final String TREE_ROOT_NAME_MEMO_4 = "memo 4";
	private final String TREE_ROOT_NAME_MEMO_5 = "memo 5";
	
	//Export XML File Name
	private final String FILE_NAME_MEMO_1 = "memo1";
	private final String FILE_NAME_MEMO_2 = "memo2";
	private final String FILE_NAME_MEMO_3 = "memo3";
	private final String FILE_NAME_MEMO_4 = "memo4";
	private final String FILE_NAME_MEMO_5 = "memo5";

	private String title;
	
	private JPanel mainPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	private MemoPanel memoPanel1;
	private MemoPanel memoPanel2;
	private MemoPanel memoPanel3;
	private MemoPanel memoPanel4;
	private MemoPanel memoPanel5;
	
	/**
	 * 생성자
	 * @param title Frame 타이틀 "Momo V1.0 - [" + argFilePath + "]"
	 * @param argFilePath Export XML File Path
	 * @param treeFontSize Tree Font Size
	 */
    public MemoFrame(String title, String argFilePath, int treeFontSize) {
    	this.title = title;
        FILE_PATH = argFilePath;
        TREE_FONT_SIZE = treeFontSize;
        try {
            jbInit();
            this.setTitle(title);
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 400;
            int height = (int)screenSize.getHeight() - 50;
            
            Dimension windowSize = new Dimension(width,height);
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * GUI를 초기화한다.
     * @throws Exception
     */
    private void jbInit() throws Exception
    {
    	memoPanel1 = new MemoPanel(TREE_ROOT_NAME_MEMO_1, FILE_NAME_MEMO_1);
    	memoPanel2 = new MemoPanel(TREE_ROOT_NAME_MEMO_2, FILE_NAME_MEMO_2);
    	memoPanel3 = new MemoPanel(TREE_ROOT_NAME_MEMO_3, FILE_NAME_MEMO_3);
    	memoPanel4 = new MemoPanel(TREE_ROOT_NAME_MEMO_4, FILE_NAME_MEMO_4);
    	memoPanel5 = new MemoPanel(TREE_ROOT_NAME_MEMO_5, FILE_NAME_MEMO_5);
    	
    	mainPanel.setLayout(gridBagLayout1);
    	tabbedPane.setBackground(BACKGROUND_COLOR);
    	mainPanel.setBackground(BACKGROUND_COLOR);

    	tabbedPane.add(TAB_TITLE_A, memoPanel1);
    	tabbedPane.add(TAB_TITLE_B, memoPanel2);
    	tabbedPane.add(TAB_TITLE_C, memoPanel3);
    	tabbedPane.add(TAB_TITLE_D, memoPanel4);
    	tabbedPane.add(TAB_TITLE_E, memoPanel5);
    	
    	mainPanel.add(tabbedPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    	
    	this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected void processWindowEvent(WindowEvent e)
    {
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
        	System.exit(0);
        }
    }
    
    /**
     * 메모 프로그램 실행 main
     * @param args	사용자 입력(filePath, fontSize)
     */   
    /**
     * MAIN 
     * @param args 
     * filePath : Export XML File Path (Default : C:\\temp)
     * FontSize : Tree Font Size (Default :13)
     */
	public static final void main(String[] args) {
		String filePath = "";

		StringBuffer message = new StringBuffer();
		message.append("-----------------------------------").append("\n");
		message.append("java MemoFrame").append("\n");
		message.append("[Default filePath] - C:\\\\temp").append("\n");
		message.append("[Default FontSize] - 13").append("\n");
		message.append("-----------------------------------").append("\n");
		message.append("java MemoFrame [filePath] [FontSize]").append("\n");
		message.append("[filePath] - H:\\\\temp").append("\n");
		message.append("[FontSize] - 14").append("\n");
		message.append("-----------------------------------");

		String argFilePath = "";
		int fontSize = 0;
		String fontSizeStr = ""; 
		if (args.length == 0) {
			argFilePath = DEFAULT_FILE_PATH;
			fontSize = DEFAULT_TREE_FONT_SIZE;
			
		}else if (args.length == 1) {
			argFilePath = args[0];
			fontSize = DEFAULT_TREE_FONT_SIZE;
			
			// filePath가 정상적인지 확인한다.
			boolean result = true;
			try {
				File file = new File(argFilePath);
				if (!file.exists()) {
					result = file.mkdir();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (result == false) {
				message.append("\n").append("Fail : invalid [FilePath]");
				System.exit(0);
			}
		}else if (args.length == 2) {
			argFilePath = args[0];
			fontSizeStr = args[1];
			// filePath가 정상적인지 확인한다.
			boolean result = true;
			try {
				File file = new File(argFilePath);
				if (!file.exists()) {
					result = file.mkdir();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (result == false) {
				message.append("\n").append("Fail : invalid [FilePath]");
				System.exit(0);
			}

			try
			{
				fontSize =  Integer.parseInt(fontSizeStr);
			}
			catch(Exception e)
			{
				result = false;
			}
			
			if (result == false) {
				message.append("\n").append("Fail : invalid [FontSize]");
				System.exit(0);
			}
			
		}else {
			System.exit(0);
		}

		try {
			EmsLookAndFeel.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String title = "Momo V1.0 - [" + argFilePath + "]";
		MemoFrame memoFrame = new MemoFrame(title, argFilePath, fontSize);
		memoFrame.show();
	}
	
}
