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

public class SPPopup extends JPopupMenu implements ActionListener {

	private JComponent source = null;
	private JPopupMenu popupMenu = this;

	public SPPopup(SPTree spTree) {
		init(spTree);
		this.setVisible(true);
	}

	public void init(JComponent source) {
		source.add(this); // tree attach
		source.addMouseListener(new PopupTriggerListener());
	}

	void addMethod(String name, Object target, String sMethod) {
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
	// ###############################################################
	// *** PLATFORM AREA
	// *********************************************************
	
	Object target = null;
	HashMap<String, Method> map = new HashMap<String, Method>();

	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("actionPerformed cmd="+arg0.getActionCommand());
		Method method =   map.get(arg0.getActionCommand());
		if ( method != null ) {
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

		public void mouseClicked(MouseEvent ev) {
		}
	}



}
