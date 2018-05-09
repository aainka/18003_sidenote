package platform.sidenote;

public class Debug {

	private Class clazz;
	private boolean enabled = true;
	String infoMethod = null;
	String infoSource = null;

	public static Debug getLogger(Class clazz) {
		// TODO Auto-generated method stub
		Debug log = new Debug();
		log.clazz = clazz;
		// String infoMethod = clazz.getSimpleName() + "." + stfound.getMethodName() +
		// "()";
		// String infoSource = " (" + clazz.getSimpleName() + ".java:" +
		// stfound.getLineNumber() + ")";
		return log;
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
			String infoMethod = clazz.getSimpleName() + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + clazz.getSimpleName() + ".java:" + stfound.getLineNumber() + ")";
			System.out.printf(infoMethod + " [ERROR] " + msg + infoSource + "\n");
		}
	}

	public void info(String msg) {
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
			String infoMethod = clazz.getSimpleName() + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + clazz.getSimpleName() + ".java:" + stfound.getLineNumber() + ")";
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
			String infoMethod = clazz.getSimpleName() + "." + stfound.getMethodName() + "()";
			String infoSource = " (" + clazz.getSimpleName() + ".java:" + stfound.getLineNumber() + ")";
			System.out.printf(infoMethod + " [DEBUG] " + msg + infoSource + "\n");
		}
	}

}
