package platform.sidenote;

import java.util.ArrayList;
import java.util.List;

/*
 * cmdmsg = {"(cmd,func),(cmd,func)" }
 */

public class TinyValue {
	String s = "(cmd1,func),(cmd2,func)";

	public void test() {

	}

	public List<String[]> parse(String s) {
		List<String[]> list = new ArrayList<String[]>();
		int sp = 0;
		String sArg[] = s.split("\\),");
		for (String w : sArg) {
			if (w.length() > 2) {
				w = w.replace(")", "");
				w = w.replace("(", "");
				list.add(parseArg(w));
			}
		}
		return list;
	}

	public String[] parseArg(String s) {
		System.out.println("arg[] parse =" + s);
		String sArg[] = s.split(",");
		for (String w : sArg) {
			System.out.println("arg[] parse.w =" + w);
		}
		return sArg;
	}

	public static final void main2(String[] args) {
		// DataTreeModel treeModel = new StringTreeModel();

		new TinyValue().test();
	}
}
