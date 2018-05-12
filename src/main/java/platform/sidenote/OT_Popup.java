
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class OT_Popup extends JPopupMenu implements ActionListener {

	private JComponent source = null;
	private JPopupMenu popupMenu = this;
	private Debug logger = Debug.getLogger(this.getClass());
	private Object target = null;
	private HashMap<String, Method> map = new HashMap<String, Method>();

	public OT_Popup(JComponent tree) {
		init(tree);
	}

	public void init(JComponent source) {
		source.add(this); // tree attach
		source.addMouseListener(new PopupTriggerListener());
		this.setVisible(true);
	}

	void addMethodCall(String name, Object target, String sMethod) {
		this.target = target;
		Class[] parameterTypes = new Class[] { target.getClass() };
		Method method;
		try {
			method = target.getClass().getMethod(sMethod, parameterTypes);
			if (method != null) {
				JMenuItem menuItem = new JMenuItem(name);
				menuItem.addActionListener(this);
				this.add(menuItem);
				map.put(name, method);
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ###############################################################
	// ### PLATFORM AREA
	// ###############################################################

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("actionPerformed cmd=" + arg0.getActionCommand());
		Method method = map.get(arg0.getActionCommand());
		if (method != null) {
			try {
				method.invoke(target, target);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class PopupTriggerListener extends MouseAdapter {
		public void mousePressed(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				popupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		public void mouseReleased(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				popupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}
	}

}
