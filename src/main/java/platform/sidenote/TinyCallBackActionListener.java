package platform.sidenote;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class TinyCallBackActionListener implements ActionListener {

	private Object target = null;
	private List<String[]> valueList = null;
	private Class<?> targetClass;
	private Point mousePupupPoint;

	/*
	 * cmdmsg = {"(cmd,func),(cmd,func)" }
	 */
	public TinyCallBackActionListener(Object target, List<String[]> valueList) {
		this.target = target;
		this.valueList = valueList;
		targetClass = target.getClass();
	}

	public TinyCallBackActionListener(Object target, Class<?> targetClass, List<String[]> valueList) {
		this.target = target;
		this.valueList = valueList;
		this.targetClass = targetClass;

//		for (Method m : target.getClass().getMethods()) {
//			for (Class c : m.getParameterTypes()) {
//			}
//		}
	}

	public void setMousePupupPoint(Point mousePupupPoint) {
		this.mousePupupPoint = mousePupupPoint;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("CallBackAction:: actionPerformed() cmd=" + arg0.getActionCommand());
		String command = arg0.getActionCommand();
		String func = null;
		try {
			/*
			 * find func_name
			 */
			for (String[] values : valueList) {
				if (values[0].equals(command)) {
					func = "_" + values[1];
					break;
				}
			}
			Class[] parameterTypes = new Class[] { Object.class, Point.class };
			Method method = target.getClass().getMethod(func, parameterTypes);
			method.invoke(target, target, mousePupupPoint);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			String msg = "NoCallBack \nObject = "+target.getClass()+"\nfunc="+func+"(Object, Point)";
			JOptionPane.showMessageDialog(null, msg);
		}
	}

	 

 

}
