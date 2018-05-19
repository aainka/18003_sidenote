package platform.sidenote;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TinyCallBackActionListener implements ActionListener {

	Object target = null;
	List<String[]> valueList = null;
	private Class<?> targetClass;
	private Point mousePupupPoint;

	/*
	 * cmdmsg = {"(cmd,func),(cmd,func)" }
	 */
	public TinyCallBackActionListener(Object target, List<String[]> valueList) {
		this.target = target;
		this.valueList = valueList;
		System.out.println("TT.init=" + target.getClass().getName());
		targetClass = target.getClass();
	}

	public TinyCallBackActionListener(Object target, Class<?> targetClass, List<String[]> valueList) {
		this.target = target;
		this.valueList = valueList;
		this.targetClass = targetClass;
		System.out.println("TT.init=" + target.getClass().getName());

		for (Method m : target.getClass().getMethods()) {
			System.out.print("m.name = " + m.getName());
			for (Class c : m.getParameterTypes()) {
				System.out.print("  + arg." + c.getSimpleName());
			}
			System.out.println();
		}
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
//			System.out.println("CallBackAction:: actionPerformed() func=" + func);
//			for (Method m : target.getClass().getMethods()) {
//				if (m.getName().indexOf("_") >= 0) {
//					System.out.println("### CB has call : " + m.getName());
//					for (Class c : m.getParameterTypes()) {
//						System.out.println("  + arg." + c.getSimpleName());
//					}
//				}
//			}
			Class[] parameterTypes = new Class[] { Object.class, Point.class };
			Method method = target.getClass().getMethod(func, parameterTypes);
			method.invoke(target, target, mousePupupPoint);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			  e.printStackTrace();
//			System.out.println("TinyCallBackActionListener [ERROR] " //
//					+ target.getClass().getName() + "." + func + "()");
		}
	}

	 

 

}
