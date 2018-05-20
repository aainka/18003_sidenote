
/*______                             ________                             __                        
 /      \                           /        |                           /  |                       
/$$$$$$  | _______    ______        $$$$$$$$/______   __    __   _______ $$ |____                   
$$ |  $$ |/       \  /      \          $$ | /      \ /  |  /  | /       |$$      \                  
$$ |  $$ |$$$$$$$  |/$$$$$$  |         $$ |/$$$$$$  |$$ |  $$ |/$$$$$$$/ $$$$$$$  |                 
$$ |  $$ |$$ |  $$ |$$    $$ |         $$ |$$ |  $$ |$$ |  $$ |$$ |      $$ |  $$ |                 
$$ \__$$ |$$ |  $$ |$$$$$$$$/          $$ |$$ \__$$ |$$ \__$$ |$$ \_____ $$ |  $$ |                 
$$    $$/ $$ |  $$ |$$       |         $$ |$$    $$/ $$    $$/ $$       |$$ |  $$ |                 
 $$$$$$/  $$/   $$/  $$$$$$$/          $$/  $$$$$$/   $$$$$$/   $$$$$$$/ $$/   $$/                  
 _______   __              __       ______                                           __     ______  
/       \ /  |            /  |     /      \                                        _/  |   /      \ 
$$$$$$$  |$$ |  ______   _$$ |_   /$$$$$$  |______    ______   _____  ____        / $$ |  /$$$$$$  |
$$ |__$$ |$$ | /      \ / $$   |  $$ |_ $$//      \  /      \ /     \/    \       $$$$ |  $$ \__$$ |
$$    $$/ $$ | $$$$$$  |$$$$$$/   $$   |  /$$$$$$  |/$$$$$$  |$$$$$$ $$$$  |        $$ |  $$    $$< 
$$$$$$$/  $$ | /    $$ |  $$ | __ $$$$/   $$ |  $$ |$$ |  $$/ $$ | $$ | $$ |        $$ |   $$$$$$  |
$$ |      $$ |/$$$$$$$ |  $$ |/  |$$ |    $$ \__$$ |$$ |      $$ | $$ | $$ |       _$$ |_ $$ \__$$ |
$$ |      $$ |$$    $$ |  $$  $$/ $$ |    $$    $$/ $$ |      $$ | $$ | $$ |      / $$   |$$    $$/ 
$$/       $$/  $$$$$$$/    $$$$/  $$/      $$$$$$/  $$/       $$/  $$/  $$/       $$$$$$/  $$$$$$/  

*/

package platform.sidenote;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import platform.sidenote.util.Debug;

public class OT_Popup extends JPopupMenu implements ActionListener {

	protected JComponent source = null;
	private JPopupMenu popupMenu = this;
	private Debug logger = Debug.getLogger(this.getClass());
	private Object target = null;
	private HashMap<String, Method> map = new HashMap<String, Method>();
	private Point p = new Point();
	protected TinyCallBackActionListener l;

	public OT_Popup() {
		this.setVisible(true);
		init();
	}

	protected void addMenu(List<String[]> valueList) {
		for (String[] values : valueList) {
			JMenuItem menuItem = new JMenuItem(values[0]);
			menuItem.addActionListener(this);
			this.add(menuItem);
		//	System.out.println("Popup.addMenu(" + values[0]);
		}
	}

	@Override
	public void show(Component invoker, int x, int y) {
		p.x = x;
		p.y = y;
		super.show(invoker, x, y);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		l.setMousePupupPoint(p);
		l.actionPerformed(arg0);
	}

	public void init() {
	}

}
