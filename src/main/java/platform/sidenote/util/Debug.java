package platform.sidenote.util;

public class Debug {

	private Class sourceClass;
	private boolean enabled = true;
	String infoMethod = null;
	String infoSource = null;
	String sClass = null;

	public static Debug getLogger(Class sourceClass) {
		// TODO Auto-generated method stub
		Debug log = new Debug();
		log.sourceClass = sourceClass;
		log.init();

		// String infoMethod = clazz.getSimpleName() + "." + stfound.getMethodName() +
		// "()";
		// String infoSource = " (" + clazz.getSimpleName() + ".java:" +
		// stfound.getLineNumber() + ")";
		return log;
	}

	public void init() {
		if (sourceClass.getName().indexOf("$") >= 0) {
			sClass = cutString(sourceClass.getName(), ".", "$");
			// banner(sClass);
		}
	}

	public String cutString(String s, String w1, String w2) {
		int a = s.lastIndexOf(w1) + 1;
		int b = s.indexOf(w2);
		return s.substring(a, b);
	}

	public void banner(String s) {
		System.out.println("##########################################");
		System.out.println("####        " + s);
		System.out.println("##########################################");
	}

	public void off() {
		enabled = false;

	}

	public void error(String msg) {
		int count = 0;
		StackTraceElement stfound = null;

		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			count++;
			if (count == 3) {
				stfound = ste;
			}
		}
		if (enabled) {
			// String info = String.format("%-30s", (clazz.getSimpleName() + "." +
			// method+"()"));
			String infoMethod = sClass + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + sClass + ".java:" + stfound.getLineNumber() + ")";
			System.out.printf(infoMethod + " [ERROR] " + msg + infoSource + "\n");
		}
	}

	public void info(String msg) {
		int count = 0;
		StackTraceElement stfound = null;
		boolean tmpFound = false;

		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			if (tmpFound) {
				stfound = ste;
				break;
			}
			if (ste.getClassName().indexOf(".Debug") > 0) {
				tmpFound = true;
			}
		}
		if (enabled) {
			// String info = String.format("%-30s", (clazz.getSimpleName() + "." +
			// method+"()"));
			String infoMethod = sClass + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + sClass + ".java:" + stfound.getLineNumber() + ")";
			System.out.printf(infoMethod + " [INFO] " + msg + infoSource + "\n");
		}

	}

	public void info7(String msg) {
		int count = 0;
		StackTraceElement stfound = null;

		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			count++;
			if (count == 3) {
				stfound = ste;
			}
		}
		if (enabled) {
			// String info = String.format("%-30s", (clazz.getSimpleName() + "." +
			// method+"()"));
			String infoMethod = sClass + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + sClass + ".java:" + stfound.getLineNumber() + ")";
			System.out.printf(infoMethod + " [DEBUG] " + msg + infoSource + "\n");
		}
	}

}
