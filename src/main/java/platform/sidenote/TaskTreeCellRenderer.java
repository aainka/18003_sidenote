package platform.sidenote;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class TaskTreeCellRenderer implements TreeCellRenderer {

	Debug logger = Debug.getLogger(this.getClass());
	JLabel tf = new JLabel();

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if ( (row%2) ==0 ) {
			tf.setBackground(Color.GRAY);
		}else {
			tf.setBackground(Color.YELLOW);
		}
		tf.setText(node.getUserObject().toString());
		return tf;
	}

	// setTreeCellRenderer(new TreeCellRenderer() {
	// private final TreeCellRenderer myBaseRenderer = new
	// HighlightableCellRenderer();
	// public Component getTreeCellRendererComponent(JTree tree1,
	// Object value,
	// boolean selected,
	// boolean expanded,
	// boolean leaf,
	// int row,
	// boolean hasFocus) {
	// JComponent result =
	// (JComponent)myBaseRenderer.getTreeCellRendererComponent(tree1, value,
	// selected, expanded, leaf, row, hasFocus);
	// result.setOpaque(!selected);
	// return result;
	// }
	// });
}
